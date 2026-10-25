
package com.esg.reporting.controller;

import com.esg.reporting.entity.EsgReport;
import com.esg.reporting.entity.Evidence;
import com.esg.reporting.repository.EsgReportRepository;
import com.esg.reporting.repository.EvidenceRepository;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/reports/{reportId}/evidence")
public class EvidenceController {

    private final EvidenceRepository evidenceRepository;
    private final EsgReportRepository reportRepository;

    public EvidenceController(
            EvidenceRepository evidenceRepository,
            EsgReportRepository reportRepository) {
        this.evidenceRepository = evidenceRepository;
        this.reportRepository = reportRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Evidence addEvidence(
            @PathVariable Long reportId,
            @RequestBody Evidence evidence) {

        EsgReport report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "ESG report not found"));

        evidence.setReport(report);

        return evidenceRepository.save(evidence);
    }

    @GetMapping
    public List<Evidence> getEvidence(
            @PathVariable Long reportId) {

        if (!reportRepository.existsById(reportId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "ESG report not found");
        }

        return evidenceRepository.findByReportId(reportId);
    }
}
