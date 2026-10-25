
package com.esg.reporting.controller;

import com.esg.reporting.entity.EsgReport;
import com.esg.reporting.entity.EthicsMetric;
import com.esg.reporting.repository.EsgReportRepository;
import com.esg.reporting.repository.EthicsMetricRepository;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/reports/{reportId}/ethics")
public class EthicsMetricController {

    private final EthicsMetricRepository metricRepository;
    private final EsgReportRepository reportRepository;

    public EthicsMetricController(
            EthicsMetricRepository metricRepository,
            EsgReportRepository reportRepository) {
        this.metricRepository = metricRepository;
        this.reportRepository = reportRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EthicsMetric createMetric(
            @PathVariable Long reportId,
            @RequestBody EthicsMetric metric) {

        EsgReport report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "ESG report not found"));

        if (metric.getReportedCases() != null
                && metric.getOpenCases() != null
                && metric.getClosedCases() != null
                && metric.getOpenCases() + metric.getClosedCases()
                > metric.getReportedCases()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Open and closed cases cannot exceed reported cases");
        }

        metric.setReport(report);
        return metricRepository.save(metric);
    }

    @GetMapping
    public List<EthicsMetric> getMetrics(
            @PathVariable Long reportId) {

        if (!reportRepository.existsById(reportId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "ESG report not found");
        }

        return metricRepository.findByReportId(reportId);
    }
}
