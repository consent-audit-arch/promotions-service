package com.tcc.promotion_service.application.dto.batch;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class BatchPromotionEvaluateRequest {
    @NotEmpty
    private List<Long> ids;
    private String targetSegment;

    public List<Long> getIds() { return ids; }
    public void setIds(List<Long> ids) { this.ids = ids; }
    public String getTargetSegment() { return targetSegment; }
    public void setTargetSegment(String targetSegment) { this.targetSegment = targetSegment; }
}
