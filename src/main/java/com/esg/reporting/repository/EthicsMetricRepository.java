
package com.esg.reporting.repository;

import com.esg.reporting.entity.EthicsMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EthicsMetricRepository
        extends JpaRepository<EthicsMetric, Long> {

    List<EthicsMetric> findByReportId(Long reportId);
}
