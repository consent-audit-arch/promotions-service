package com.tcc.promotion_service.application.service;

import com.tcc.promotion_service.application.dto.PromotionRequest;
import com.tcc.promotion_service.application.dto.PromotionResponse;
import com.tcc.promotion_service.infrastructure.persistence.entity.PromotionJpaEntity;
import com.tcc.promotion_service.infrastructure.persistence.repository.PromotionJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PromotionService {

    private final PromotionJpaRepository repository;

    public PromotionService(PromotionJpaRepository repository) {
        this.repository = repository;
    }

    public PromotionResponse create(PromotionRequest request) {
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
