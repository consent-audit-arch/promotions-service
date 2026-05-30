package com.tcc.promotion_service.application.dto.batch;

import java.util.List;

public class BatchPromotionEvaluateResponse {
    private List<UserEvaluationResult> results;
    private int totalProcessed;
    private int totalAllowed;
    private int totalDenied;

    public BatchPromotionEvaluateResponse(List<UserEvaluationResult> results) {
        this.results = results;
        this.totalProcessed = results.size();
        this.totalAllowed = (int) results.stream().filter(UserEvaluationResult::isAllow).count();
        this.totalDenied = this.totalProcessed - this.totalAllowed;
    }

    public List<UserEvaluationResult> getResults() { return results; }
    public int getTotalProcessed() { return totalProcessed; }
    public int getTotalAllowed() { return totalAllowed; }
    public int getTotalDenied() { return totalDenied; }
}
