package com.tcc.promotion_service.application.dto.batch;

import com.tcc.promotion_service.application.dto.UserUsageProfileDTO;

import java.util.List;

public class UserUsageBatchRecord {
    private Long userId;
    private List<UserUsageProfileDTO> usageRecords;

    public UserUsageBatchRecord() {}

    public UserUsageBatchRecord(Long userId, List<UserUsageProfileDTO> usageRecords) {
        this.userId = userId;
        this.usageRecords = usageRecords;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public List<UserUsageProfileDTO> getUsageRecords() { return usageRecords; }
    public void setUsageRecords(List<UserUsageProfileDTO> usageRecords) { this.usageRecords = usageRecords; }
}
