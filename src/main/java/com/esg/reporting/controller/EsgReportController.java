
package com.esg.reporting.controller;

import com.esg.reporting.entity.EsgReport;
import com.esg.reporting.entity.ReportRevision;
import com.esg.reporting.repository.EsgReportRepository;
import com.esg.reporting.repository.ReportRevisionRepository;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class EsgReportController {

    private final EsgReportRepository repository;
    private final ReportRevisionRepository revisionRepository;

    public EsgReportController(
            EsgReportRepository repository,
            ReportRevisionRepository revisionRepository) {
        this.repository = repository;
        this.revisionRepository = revisionRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public EsgReport createReport(@Valid @RequestBody EsgReport report) {
        report.setStatus("DRAFT");
        report.setRevisionNumber(1);
        report.setReviewComment(null);

        EsgReport saved = repository.save(report);

        recordRevision(saved, null, "DRAFT", null, "SYSTEM");
        return saved;
    }

    @GetMapping
    public List<EsgReport> getAllReports() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public EsgReport getReportById(@PathVariable Long id) {
        return findReport(id);
    }

    @PutMapping("/{id}/submit")
    @Transactional
    public EsgReport submitReport(@PathVariable Long id) {
        EsgReport report = findReport(id);

        String oldStatus = report.getStatus();

        if (!"DRAFT".equals(oldStatus)
                && !"CHANGES_REQUESTED".equals(oldStatus)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Only draft or changes-requested reports can be submitted");
        }

        report.setStatus("SUBMITTED");
        report.setRevisionNumber(nextRevision(report));

        EsgReport saved = repository.save(report);
        recordRevision(saved, oldStatus, "SUBMITTED", null, "SYSTEM");

        return saved;
    }

    @PutMapping("/{id}/approve")
    @Transactional
    public EsgReport approveReport(@PathVariable Long id) {
        EsgReport report = findReport(id);

        String oldStatus = report.getStatus();

        if (!"SUBMITTED".equals(oldStatus)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Only submitted reports can be approved");
        }

        report.setStatus("APPROVED");
        report.setRevisionNumber(nextRevision(report));

        EsgReport saved = repository.save(report);
        recordRevision(saved, oldStatus, "APPROVED",
                report.getReviewComment(), "SYSTEM");

        return saved;
    }

    @PutMapping("/{id}/request-changes")
    @Transactional
    public EsgReport requestChanges(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {

        EsgReport report = findReport(id);
        String oldStatus = report.getStatus();

        if (!"SUBMITTED".equals(oldStatus)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Only submitted reports can have changes requested");
        }

        String comment = request.get("reviewComment");

        if (comment == null || comment.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Review comment is required");
        }

        if (comment.trim().length() > 2000) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Review comment cannot exceed 2000 characters");
        }

        report.setReviewComment(comment.trim());
        report.setStatus("CHANGES_REQUESTED");
        report.setRevisionNumber(nextRevision(report));

        EsgReport saved = repository.save(report);
        recordRevision(saved, oldStatus, "CHANGES_REQUESTED",
                comment.trim(), "SYSTEM");

        return saved;
    }

    @GetMapping("/{id}/history")
    public List<ReportRevision> getReportHistory(@PathVariable Long id) {
        findReport(id);
        return revisionRepository
                .findByReportIdOrderByRevisionNumberDesc(id);
    }

    private EsgReport findReport(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "ESG report not found"));
    }

    private int nextRevision(EsgReport report) {
        Integer current = report.getRevisionNumber();
        return (current == null ? 1 : current) + 1;
    }

    private void recordRevision(
            EsgReport report,
            String previousStatus,
            String newStatus,
            String comment,
            String changedBy) {

        ReportRevision revision = new ReportRevision();
        revision.setReport(report);
        revision.setRevisionNumber(report.getRevisionNumber());
        revision.setPreviousStatus(previousStatus);
        revision.setNewStatus(newStatus);
        revision.setReviewerComment(comment);
        revision.setChangedBy(changedBy);

        revisionRepository.save(revision);
    }
}
