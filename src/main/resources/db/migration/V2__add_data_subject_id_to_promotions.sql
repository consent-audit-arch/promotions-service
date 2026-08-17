ALTER TABLE promotions ADD COLUMN data_subject_id BIGINT;

CREATE INDEX idx_promotions_data_subject_id ON promotions (data_subject_id);
