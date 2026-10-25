
package com.esg.reporting.repository;

import com.esg.reporting.entity.EvidenceDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EvidenceDocumentRepository
        extends JpaRepository<EvidenceDocument, Long> {

    List<EvidenceDocument> findByReportId(Long reportId);
}
