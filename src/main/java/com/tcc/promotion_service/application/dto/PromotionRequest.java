package com.tcc.promotion_service.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PromotionRequest {
    @NotBlank
    private String dataSubjectId;
    @NotBlank
    private String name;
    private String description;
    @NotNull
    private BigDecimal discountPercent;
    @NotNull
    private LocalDate startDate;
    private LocalDate endDate;
    @NotBlank
    private String targetSegment;

    public String getDataSubjectId() { return dataSubjectId; }
    public void setDataSubjectId(String dataSubjectId) { this.dataSubjectId = dataSubjectId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(BigDecimal discountPercent) { this.discountPercent = discountPercent; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public String getTargetSegment() { return targetSegment; }
    public void setTargetSegment(String targetSegment) { this.targetSegment = targetSegment; }
}
