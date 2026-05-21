CREATE TABLE promotions (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    discount_percent NUMERIC(5,2) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    target_segment VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_promotions_target_segment ON promotions (target_segment);
CREATE INDEX idx_promotions_status ON promotions (status);
