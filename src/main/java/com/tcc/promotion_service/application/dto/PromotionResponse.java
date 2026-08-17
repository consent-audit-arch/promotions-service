package com.tcc.promotion_service.application.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public class PromotionResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal discountPercent;
    private LocalDate startDate;
    private LocalDate endDate;
    private String targetSegment;
    private String status;
    private Instant createdAt;
    private Long dataSubjectId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Long getDataSubjectId() { return dataSubjectId; }
    public void setDataSubjectId(Long dataSubjectId) { this.dataSubjectId = dataSubjectId; }
}
