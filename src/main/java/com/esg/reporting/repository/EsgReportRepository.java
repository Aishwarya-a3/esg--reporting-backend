
package com.esg.reporting.repository;

import com.esg.reporting.entity.EsgReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EsgReportRepository
        extends JpaRepository<EsgReport, Long> {
}
