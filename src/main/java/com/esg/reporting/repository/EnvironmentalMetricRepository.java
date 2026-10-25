
package com.esg.reporting.repository;

import com.esg.reporting.entity.EnvironmentalMetric;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnvironmentalMetricRepository
        extends JpaRepository<EnvironmentalMetric, Long> {

    List<EnvironmentalMetric> findByReportId(Long reportId);
}
