package com.tcc.promotion_service.application.dto.batch;

import java.util.List;

public class BatchUsageResponse {
    private List<UserUsageBatchRecord> data;
    private List<DeniedUser> denied;
    private boolean partial;

    public List<UserUsageBatchRecord> getData() { return data; }
    public void setData(List<UserUsageBatchRecord> data) { this.data = data; }
    public List<DeniedUser> getDenied() { return denied; }
    public void setDenied(List<DeniedUser> denied) { this.denied = denied; }
    public boolean isPartial() { return partial; }
    public void setPartial(boolean partial) { this.partial = partial; }
}
