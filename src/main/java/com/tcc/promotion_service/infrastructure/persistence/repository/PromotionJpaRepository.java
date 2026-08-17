package com.tcc.promotion_service.infrastructure.persistence.repository;

import com.tcc.promotion_service.infrastructure.persistence.entity.PromotionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionJpaRepository extends JpaRepository<PromotionJpaEntity, Long> {
    List<PromotionJpaEntity> findByTargetSegment(String targetSegment);
    List<PromotionJpaEntity> findByStatus(String status);
    List<PromotionJpaEntity> findByDataSubjectId(Long dataSubjectId);
}
