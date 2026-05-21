package com.tcc.promotion_service.interfaces.rest;

import com.tcc.security.annotation.RequiresConsent;
import com.tcc.promotion_service.application.dto.PromotionRequest;
import com.tcc.promotion_service.application.dto.PromotionResponse;
import com.tcc.promotion_service.application.service.PromotionService;
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
            dataCategories = {"PERSONAL_DATA"})
    public ResponseEntity<PromotionResponse> create(@Valid @RequestBody PromotionRequest request) {
        PromotionResponse response = promotionService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
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
    @RequiresConsent(resource = "PROMOTION", action = "READ",
            dataCategories = {"PERSONAL_DATA"})
    public ResponseEntity<List<PromotionResponse>> findBySegment(@PathVariable String segment) {
        return ResponseEntity.ok(promotionService.findByTargetSegment(segment));
    }
}
