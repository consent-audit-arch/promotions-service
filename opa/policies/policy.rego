package promotion.authz

import future.keywords.in
import future.keywords.if

default decision := {"allow": false, "reason": "Denied by default"}

allowed_callers := {"dashboard-service", "postman"}
allowed_purposes := {"PROMOTION", "ANALYTICS"}

token_uri := opa.runtime().env.OPA_KEYCLOAK_TOKEN_URI
client_id := opa.runtime().env.OPA_CLIENT_ID
client_secret := opa.runtime().env.OPA_CLIENT_SECRET

token_response := http.send({
    "method": "POST",
    "url": token_uri,
    "headers": {
        "Content-Type": "application/x-www-form-urlencoded"
    },
    "raw_body": concat("", [
        "grant_type=client_credentials",
        "&client_id=", client_id,
        "&client_secret=", client_secret
    ]),
    "timeout": "5s",
    "raise_error": false,
    "force_cache": true,
    "force_cache_duration_seconds": 240
})

access_token := token_response.body.access_token

# ──────────────────────────────────────
# SINGLE REQUEST via consent-query-service
# ──────────────────────────────────────
consent_response := http.send({
    "method": "GET",
    "url": concat("", [
        "http://consent-query-service:8080/api/v1/consent/",
        input.dataSubjectId
    ]),
    "headers": {
        "Authorization": concat(" ", ["Bearer", access_token]),
        "X-Purpose": input.purpose,
        "X-Data-Categories": concat(",", input.dataCategories),
        "X-Data-Subject-Id": input.dataSubjectId
    },
    "timeout": "3s",
    "raise_error": false
})

consent_data := consent_response.body.authorizations

decision_has_active_consent(category) if {
    token_response.status_code == 200
    consent_response.status_code == 200
    some auth in consent_data
    auth.purpose == input.purpose
    auth.dataCategory == category
    auth.status == "GRANTED"
}

# ── PROMOTION / CREATE (requires PERSONAL_DATA consent) ──
decision := {"allow": true, "reason": "Access granted"} if {
    "PROMOTION_READ" in input.caller.roles
    input.caller.clientId in allowed_callers
    input.purpose in allowed_purposes
    input.resource == "PROMOTION"
    input.action == "CREATE"
    input.dataSubjectId != null
    input.dataSubjectId != ""
    "PERSONAL_DATA" in input.dataCategories
    decision_has_active_consent("PERSONAL_DATA")
}

# ── PROMOTION / READ (no consent needed for listing, but role required) ──
decision := {"allow": true, "reason": "Access granted"} if {
    "PROMOTION_READ" in input.caller.roles
    input.caller.clientId in allowed_callers
    input.purpose in allowed_purposes
    input.resource == "PROMOTION"
    input.action == "READ"
}

# ── PROMOTION / READ with PERSONAL_DATA ──
decision := {"allow": true, "reason": "Access granted"} if {
    "PROMOTION_READ" in input.caller.roles
    input.caller.clientId in allowed_callers
    input.purpose in allowed_purposes
    input.resource == "PROMOTION"
    input.action == "READ"
    "PERSONAL_DATA" in input.dataCategories
    decision_has_active_consent("PERSONAL_DATA")
}

# ──────────────────────────────────────
# DENIAL REASONS
# ──────────────────────────────────────
decision := {"allow": false, "reason": "Caller does not have PROMOTION_READ role"} if {
    not ("PROMOTION_READ" in input.caller.roles)
}

decision := {"allow": false, "reason": "Caller not authorized"} if {
    "PROMOTION_READ" in input.caller.roles
    not input.caller.clientId in allowed_callers
}

decision := {"allow": false, "reason": "Purpose not allowed"} if {
    "PROMOTION_READ" in input.caller.roles
    input.caller.clientId in allowed_callers
    not input.purpose in allowed_purposes
}

decision := {"allow": false, "reason": "Active consent not found"} if {
    "PROMOTION_READ" in input.caller.roles
    input.caller.clientId in allowed_callers
    input.purpose in allowed_purposes
    input.resource == "PROMOTION"
    "PERSONAL_DATA" in input.dataCategories
    not decision_has_active_consent("PERSONAL_DATA")
}
