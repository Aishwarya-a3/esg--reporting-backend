
package com.esg.reporting.repository;

import com.esg.reporting.entity.SocialMetric;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SocialMetricRepository
        extends JpaRepository<SocialMetric, Long> {

    List<SocialMetric> findByReportId(Long reportId);
}
