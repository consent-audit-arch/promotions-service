package com.tcc.promotion_service.application.service;

import com.tcc.promotion_service.application.dto.PromotionRequest;
import com.tcc.promotion_service.application.dto.PromotionResponse;
import com.tcc.promotion_service.application.dto.PromotionWithProfileResponse;
import com.tcc.promotion_service.application.dto.UserProfileResponse;
import com.tcc.promotion_service.application.dto.UserUsageProfileDTO;
import com.tcc.promotion_service.application.dto.batch.BatchPromotionEvaluateResponse;
import com.tcc.promotion_service.application.dto.batch.BatchUsageResponse;
import com.tcc.promotion_service.application.dto.batch.DeniedUser;
import com.tcc.promotion_service.application.dto.batch.UserEvaluationResult;
import com.tcc.promotion_service.application.dto.batch.UserUsageBatchRecord;
import com.tcc.promotion_service.infrastructure.client.UserServiceClient;
import com.tcc.promotion_service.infrastructure.client.UserServiceCommunicationException;
import com.tcc.promotion_service.infrastructure.persistence.entity.PromotionJpaEntity;
import com.tcc.promotion_service.infrastructure.persistence.repository.PromotionJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class PromotionService {

    private final PromotionJpaRepository repository;
    private final UserServiceClient userServiceClient;

    public PromotionService(PromotionJpaRepository repository, UserServiceClient userServiceClient) {
        this.repository = repository;
        this.userServiceClient = userServiceClient;
    }

    public PromotionResponse create(PromotionRequest request, String purpose, String correlationId) {
        userServiceClient.fetchUsageData(
                Long.parseLong(request.getDataSubjectId()), purpose, correlationId);

        PromotionJpaEntity entity = new PromotionJpaEntity();
        entity.setName(request.getName());
        entity.setDescription(request.getDescription());
        entity.setDiscountPercent(request.getDiscountPercent());
        entity.setStartDate(request.getStartDate());
        entity.setEndDate(request.getEndDate());
        entity.setTargetSegment(request.getTargetSegment());
        PromotionJpaEntity saved = repository.save(entity);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public BatchPromotionEvaluateResponse evaluateBatch(List<Long> ids, String purpose, String correlationId) {
        List<UserEvaluationResult> results = new ArrayList<>();

        try {
            BatchUsageResponse batch = userServiceClient.fetchUsageDataBatch(ids, purpose, correlationId);

            Map<Long, List<UserUsageProfileDTO>> usageMap = batch.getData().stream()
                    .collect(Collectors.toMap(
                            UserUsageBatchRecord::getUserId,
                            UserUsageBatchRecord::getUsageRecords
                    ));

            for (Long userId : ids) {
                List<UserUsageProfileDTO> usageData = usageMap.getOrDefault(userId, Collections.emptyList());
                results.add(new UserEvaluationResult(userId, true, "Access granted", usageData));
            }

            for (DeniedUser denied : batch.getDenied()) {
                results.add(new UserEvaluationResult(denied.getId(), false, denied.getReason(), Collections.emptyList()));
            }
        } catch (UserServiceCommunicationException e) {
            for (Long userId : ids) {
                results.add(new UserEvaluationResult(userId, false, e.getMessage(), Collections.emptyList()));
            }
        }

        return new BatchPromotionEvaluateResponse(results);
    }

    @Transactional(readOnly = true)
    public List<PromotionResponse> findAll() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PromotionResponse findById(Long id) {
        PromotionJpaEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion not found: " + id));
        return toResponse(entity);
    }

    @Transactional(readOnly = true)
    public List<PromotionResponse> findByTargetSegment(String segment) {
        return repository.findByTargetSegment(segment).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PromotionWithProfileResponse findWithProfile(Long userId, String purpose, String correlationId) {
        String resolvedCorrelationId = correlationId != null ? correlationId : java.util.UUID.randomUUID().toString();

        UserProfileResponse profile = userServiceClient.fetchUserProfile(userId, purpose, resolvedCorrelationId);
        List<UserUsageProfileDTO> usageData = userServiceClient.fetchUsageData(userId, purpose, resolvedCorrelationId);
        List<PromotionResponse> availablePromotions = findAll();

        return new PromotionWithProfileResponse(profile, usageData, availablePromotions);
    }

    private PromotionResponse toResponse(PromotionJpaEntity entity) {
        PromotionResponse response = new PromotionResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        response.setDiscountPercent(entity.getDiscountPercent());
        response.setStartDate(entity.getStartDate());
        response.setEndDate(entity.getEndDate());
        response.setTargetSegment(entity.getTargetSegment());
        response.setStatus(entity.getStatus());
        response.setCreatedAt(entity.getCreatedAt());
        return response;
    }
}
