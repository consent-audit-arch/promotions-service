package com.tcc.promotion_service.application.dto.batch;

import com.tcc.promotion_service.application.dto.UserUsageProfileDTO;

import java.util.List;

public class UserEvaluationResult {
    private Long userId;
    private boolean allow;
    private String reason;
    private List<UserUsageProfileDTO> usageData;

    public UserEvaluationResult(Long userId, boolean allow, String reason, List<UserUsageProfileDTO> usageData) {
        this.userId = userId;
        this.allow = allow;
        this.reason = reason;
        this.usageData = usageData;
    }

    public Long getUserId() { return userId; }
    public boolean isAllow() { return allow; }
    public String getReason() { return reason; }
    public List<UserUsageProfileDTO> getUsageData() { return usageData; }
}
