
package com.esg.reporting.repository;

import com.esg.reporting.entity.Evidence;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EvidenceRepository
        extends JpaRepository<Evidence, Long> {

    List<Evidence> findByReportId(Long reportId);
}
