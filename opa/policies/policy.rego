package promotion.authz

import future.keywords.in
import future.keywords.if

default decision := {"allow": false, "reason": "Denied by default"}

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

# ── PROMOTION / CREATE ──
decision := {"allow": true, "reason": "Access granted"} if {
    "PROMOTION_READ" in input.caller.roles
    input.resource == "PROMOTION"
    input.action == "CREATE"
    input.dataSubjectId != null
    input.dataSubjectId != ""
}

# ── PROMOTION / READ ──
decision := {"allow": true, "reason": "Access granted"} if {
    "PROMOTION_READ" in input.caller.roles
    input.resource == "PROMOTION"
    input.action == "READ"
}

# ── DENIAL REASONS ──
decision := {"allow": false, "reason": "Caller does not have PROMOTION_READ role"} if {
    not ("PROMOTION_READ" in input.caller.roles)
}
