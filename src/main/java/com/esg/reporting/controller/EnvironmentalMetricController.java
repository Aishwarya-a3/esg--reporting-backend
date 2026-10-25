
package com.esg.reporting.controller;

import com.esg.reporting.entity.EsgReport;
import com.esg.reporting.entity.EnvironmentalMetric;
import com.esg.reporting.repository.EsgReportRepository;
import com.esg.reporting.repository.EnvironmentalMetricRepository;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/reports/{reportId}/environmental")
public class EnvironmentalMetricController {

    private final EnvironmentalMetricRepository metricRepository;
    private final EsgReportRepository reportRepository;

    public EnvironmentalMetricController(
            EnvironmentalMetricRepository metricRepository,
            EsgReportRepository reportRepository) {
        this.metricRepository = metricRepository;
        this.reportRepository = reportRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EnvironmentalMetric createMetric(
            @PathVariable Long reportId,
            @RequestBody EnvironmentalMetric metric) {

        EsgReport report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "ESG report not found"));

        metric.setId(null);
        metric.setReport(report);

        if (metric.getTotalWaste() != null
                && metric.getRecycledWaste() != null
                && metric.getRecycledWaste() > metric.getTotalWaste()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Recycled waste cannot exceed total waste");
        }


if (metric.getTotalWaste() != null
        && metric.getRecycledWaste() != null
        && metric.getRecycledWaste() > metric.getTotalWaste()) {
    throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "Recycled waste cannot exceed total waste");
}

        return metricRepository.save(metric);
    }

    @GetMapping
    public List<EnvironmentalMetric> getMetrics(
            @PathVariable Long reportId) {

        if (!reportRepository.existsById(reportId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "ESG report not found");
        }

        return metricRepository.findByReportId(reportId);
    }
}
