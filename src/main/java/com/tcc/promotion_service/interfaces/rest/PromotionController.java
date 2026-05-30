package com.tcc.promotion_service.interfaces.rest;

import com.tcc.security.annotation.RequiresConsent;
import com.tcc.promotion_service.application.dto.PromotionRequest;
import com.tcc.promotion_service.application.dto.PromotionResponse;
import com.tcc.promotion_service.application.dto.PromotionWithProfileResponse;
import com.tcc.promotion_service.application.dto.batch.BatchPromotionEvaluateRequest;
import com.tcc.promotion_service.application.dto.batch.BatchPromotionEvaluateResponse;
import com.tcc.promotion_service.application.service.PromotionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/promotions")
public class PromotionController {

    private final PromotionService promotionService;

    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @PostMapping
    @RequiresConsent(resource = "PROMOTION", action = "CREATE",
            dataCategories = {"USAGE_DATA"},
            dataSubjectIdParam = "request.dataSubjectId")
    public ResponseEntity<PromotionResponse> create(
            @Valid @RequestBody PromotionRequest request,
            HttpServletRequest httpRequest) {
        String purpose = httpRequest.getHeader("X-Purpose");
        String correlationId = httpRequest.getHeader("X-Correlation-Id");
        PromotionResponse response = promotionService.create(request, purpose, correlationId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/evaluate-batch")
    @RequiresConsent(resource = "PROMOTION", action = "READ",
            dataCategories = {"USAGE_DATA"},
            dataSubjectIdsParam = "request.ids")
    public ResponseEntity<BatchPromotionEvaluateResponse> evaluateBatch(
            @Valid @RequestBody BatchPromotionEvaluateRequest request,
            HttpServletRequest httpRequest) {
        String purpose = httpRequest.getHeader("X-Purpose");
        String correlationId = httpRequest.getHeader("X-Correlation-Id");
        BatchPromotionEvaluateResponse response = promotionService.evaluateBatch(
                request.getIds(), purpose, correlationId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PromotionResponse>> findAll() {
        return ResponseEntity.ok(promotionService.findAll());
    }

    @GetMapping("/{id}")
    @RequiresConsent(resource = "PROMOTION", action = "READ")
    public ResponseEntity<PromotionResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(promotionService.findById(id));
    }

    @GetMapping("/segment/{segment}")
    @RequiresConsent(resource = "PROMOTION", action = "READ")
    public ResponseEntity<List<PromotionResponse>> findBySegment(@PathVariable String segment) {
        return ResponseEntity.ok(promotionService.findByTargetSegment(segment));
    }

    @GetMapping("/{userId}/with-profile")
    @RequiresConsent(resource = "PROMOTION", action = "READ",
            dataCategories = {"USAGE_DATA"},
            dataSubjectIdParam = "userId")
    public ResponseEntity<PromotionWithProfileResponse> findWithProfile(
            @PathVariable Long userId,
            HttpServletRequest request) {
        String purpose = request.getHeader("X-Purpose");
        String correlationId = request.getHeader("X-Correlation-Id");
        return ResponseEntity.ok(promotionService.findWithProfile(userId, purpose, correlationId));
    }
}
