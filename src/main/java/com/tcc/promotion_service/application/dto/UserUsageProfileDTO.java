package com.tcc.promotion_service.application.dto;

import java.math.BigDecimal;
import java.time.Instant;

public class UserUsageProfileDTO {
    private Long id;
    private String serviceType;
    private BigDecimal usageAmount;
    private String usageUnit;
    private Instant recordedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }
    public BigDecimal getUsageAmount() { return usageAmount; }
    public void setUsageAmount(BigDecimal usageAmount) { this.usageAmount = usageAmount; }
    public String getUsageUnit() { return usageUnit; }
    public void setUsageUnit(String usageUnit) { this.usageUnit = usageUnit; }
    public Instant getRecordedAt() { return recordedAt; }
    public void setRecordedAt(Instant recordedAt) { this.recordedAt = recordedAt; }
}
