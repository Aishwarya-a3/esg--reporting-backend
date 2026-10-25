
package com.esg.reporting.repository;

import com.esg.reporting.entity.GovernanceMetric;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GovernanceMetricRepository
        extends JpaRepository<GovernanceMetric, Long> {

    List<GovernanceMetric> findByReportId(Long reportId);
}
