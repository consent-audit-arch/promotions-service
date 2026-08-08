package com.tcc.promotion_service.infrastructure.client;

import com.tcc.promotion_service.application.dto.UserProfileResponse;
import com.tcc.promotion_service.application.dto.UserUsageProfileDTO;
import com.tcc.promotion_service.application.dto.batch.BatchUsageResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class UserServiceClient {

    private static final String REGISTRATION_ID = "keycloak";

    private final RestClient restClient;
    private final OAuth2AuthorizedClientManager authorizedClientManager;

    private static RuntimeException mapUserServiceError(org.springframework.http.HttpStatusCode status, Long userId) {
        if (status.isSameCodeAs(org.springframework.http.HttpStatus.FORBIDDEN)) {
            return new com.tcc.security.exception.DataAccessDeniedException(
                    "Acesso negado pelo serviço de dados do titular para o usuário " + userId);
        }
        return new UserServiceCommunicationException(
                "User service returned " + status + " for user " + userId, null);
    }

    public UserServiceClient(@Value("${user-service.url}") String userServiceUrl,
                             OAuth2AuthorizedClientManager authorizedClientManager) {
        this.restClient = RestClient.builder()
                .baseUrl(userServiceUrl)
                .build();
        this.authorizedClientManager = authorizedClientManager;
    }

    public List<UserUsageProfileDTO> fetchUsageData(Long userId, String purpose, String correlationId) {
        String token = fetchClientCredentialsToken()
                .orElseThrow(() -> new RuntimeException("Failed to obtain client credentials token"));

        return restClient.get()
                .uri("/api/v1/users/{id}/usage", userId)
                .header("Authorization", "Bearer " + token)
                .header("X-Purpose", purpose)
                .header("X-Data-Categories", "USAGE_DATA")
                .header("X-Data-Subject-Id", String.valueOf(userId))
                .header("X-Correlation-Id", correlationId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw mapUserServiceError(response.getStatusCode(), userId);
                })
                .body(new org.springframework.core.ParameterizedTypeReference<>() {});
    }

    public UserProfileResponse fetchUserProfile(Long userId, String purpose, String correlationId) {
        String token = fetchClientCredentialsToken()
                .orElseThrow(() -> new UserServiceCommunicationException(
                        "Failed to obtain client credentials token", null));

        return restClient.get()
                .uri("/api/v1/users/{id}", userId)
                .header("Authorization", "Bearer " + token)
                .header("X-Purpose", purpose)
                .header("X-Data-Categories", "PERSONAL_DATA")
                .header("X-Data-Subject-Id", String.valueOf(userId))
                .header("X-Correlation-Id", correlationId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw mapUserServiceError(response.getStatusCode(), userId);
                })
                .body(UserProfileResponse.class);
    }

    public boolean checkUserExists(Long userId, String purpose, String correlationId) {
        String token = fetchClientCredentialsToken()
                .orElseThrow(() -> new RuntimeException("Failed to obtain client credentials token"));

        try {
            restClient.get()
                    .uri("/api/v1/users/{id}/exists", userId)
                    .header("Authorization", "Bearer " + token)
                    .header("X-Purpose", purpose)
                    .header("X-Correlation-Id", correlationId)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                        throw new UserServiceCommunicationException(
                                "User service returned " + response.getStatusCode() + " for user " + userId,
                                null);
                    })
                    .toBodilessEntity();
            return true;
        } catch (UserServiceCommunicationException e) {
            return false;
        }
    }

    public BatchUsageResponse fetchUsageDataBatch(List<Long> ids, String purpose, String correlationId) {
        String token = fetchClientCredentialsToken()
                .orElseThrow(() -> new UserServiceCommunicationException(
                        "Failed to obtain client credentials token", null));

        return restClient.post()
                .uri("/api/v1/users/batch/usage")
                .header("Authorization", "Bearer " + token)
                .header("X-Purpose", purpose)
                .header("X-Data-Categories", "USAGE_DATA")
                .header("X-Data-Subject-Ids", String.join(",", ids.stream().map(String::valueOf).toList()))
                .header("X-Correlation-Id", correlationId)
                .body(Map.of("ids", ids))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new UserServiceCommunicationException(
                            "User service batch/usage returned " + response.getStatusCode(), null);
                })
                .body(BatchUsageResponse.class);
    }

    private Optional<String> fetchClientCredentialsToken() {
        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId(REGISTRATION_ID)
                .principal("promotion-service")
                .build();

        OAuth2AuthorizedClient authorizedClient = authorizedClientManager.authorize(authorizeRequest);

        if (authorizedClient == null || authorizedClient.getAccessToken() == null) {
            return Optional.empty();
        }

        return Optional.of(authorizedClient.getAccessToken().getTokenValue());
    }
}
