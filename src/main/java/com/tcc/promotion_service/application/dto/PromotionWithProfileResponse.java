package com.tcc.promotion_service.application.dto;

import java.util.List;

public class PromotionWithProfileResponse {
    private UserProfileResponse userProfile;
    private List<UserUsageProfileDTO> usageData;
    private List<PromotionResponse> availablePromotions;

    public PromotionWithProfileResponse(UserProfileResponse userProfile,
                                        List<UserUsageProfileDTO> usageData,
                                        List<PromotionResponse> availablePromotions) {
        this.userProfile = userProfile;
        this.usageData = usageData;
        this.availablePromotions = availablePromotions;
    }

    public UserProfileResponse getUserProfile() { return userProfile; }
    public List<UserUsageProfileDTO> getUsageData() { return usageData; }
    public List<PromotionResponse> getAvailablePromotions() { return availablePromotions; }
}
