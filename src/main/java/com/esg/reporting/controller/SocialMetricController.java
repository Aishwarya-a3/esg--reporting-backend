
package com.esg.reporting.controller;

import com.esg.reporting.entity.EsgReport;
import com.esg.reporting.entity.SocialMetric;
import com.esg.reporting.repository.EsgReportRepository;
import com.esg.reporting.repository.SocialMetricRepository;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/reports/{reportId}/social")
public class SocialMetricController {

    private final SocialMetricRepository metricRepository;
    private final EsgReportRepository reportRepository;

    public SocialMetricController(
            SocialMetricRepository metricRepository,
            EsgReportRepository reportRepository) {
        this.metricRepository = metricRepository;
        this.reportRepository = reportRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SocialMetric createMetric(
            @PathVariable Long reportId,
            @RequestBody SocialMetric metric) {

        EsgReport report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "ESG report not found"));

        metric.setReport(report);

        return metricRepository.save(metric);
    }

    @GetMapping
    public List<SocialMetric> getMetrics(
            @PathVariable Long reportId) {

        if (!reportRepository.existsById(reportId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "ESG report not found");
        }

        return metricRepository.findByReportId(reportId);
    }
}
