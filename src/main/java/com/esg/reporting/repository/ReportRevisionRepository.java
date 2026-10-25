
package com.esg.reporting.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.esg.reporting.entity.ReportRevision;

public interface ReportRevisionRepository
        extends JpaRepository<ReportRevision, Long> {

    List<ReportRevision> findByReportIdOrderByRevisionNumberDesc(Long reportId);
}
