
package com.esg.reporting.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esg.reporting.entity.EsgReport;
import com.esg.reporting.repository.EsgReportRepository;
import com.esg.reporting.repository.EnvironmentalMetricRepository;
import com.esg.reporting.repository.SocialMetricRepository;
import com.esg.reporting.repository.GovernanceMetricRepository;
import com.esg.reporting.repository.EthicsMetricRepository;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final EsgReportRepository reportRepository;
    private final EnvironmentalMetricRepository environmentalRepository;
    private final SocialMetricRepository socialRepository;
    private final GovernanceMetricRepository governanceRepository;
    private final EthicsMetricRepository ethicsRepository;

    public DashboardController(
            EsgReportRepository reportRepository,
            EnvironmentalMetricRepository environmentalRepository,
            SocialMetricRepository socialRepository,
            GovernanceMetricRepository governanceRepository,
            EthicsMetricRepository ethicsRepository) {
        this.reportRepository = reportRepository;
        this.environmentalRepository = environmentalRepository;
        this.socialRepository = socialRepository;
        this.governanceRepository = governanceRepository;
        this.ethicsRepository = ethicsRepository;
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<Map<String, Object>> getDashboard(
            @PathVariable Long reportId) {

        EsgReport report = reportRepository.findById(reportId).orElse(null);

        if (report == null) {
            Map<String, Object> error = new LinkedHashMap<>();
            error.put("error", "Report not found");
            error.put("reportId", reportId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        boolean environmentalComplete =
                !environmentalRepository.findByReportId(reportId).isEmpty();
        boolean socialComplete =
                !socialRepository.findByReportId(reportId).isEmpty();
        boolean governanceComplete =
                !governanceRepository.findByReportId(reportId).isEmpty();
        boolean ethicsComplete =
                !ethicsRepository.findByReportId(reportId).isEmpty();

        int completedSections = 0;
        if (environmentalComplete) completedSections++;
        if (socialComplete) completedSections++;
        if (governanceComplete) completedSections++;
        if (ethicsComplete) completedSections++;

        int completenessPercentage = completedSections * 25;

        Map<String, Object> sections = new LinkedHashMap<>();
        sections.put("environmental", environmentalComplete);
        sections.put("social", socialComplete);
        sections.put("governance", governanceComplete);
        sections.put("ethics", ethicsComplete);

        Map<String, Object> dashboard = new LinkedHashMap<>();
        dashboard.put("reportId", report.getId());
        dashboard.put("organizationName", report.getOrganizationName());
        dashboard.put("reportingPeriod", report.getReportingPeriod());
        dashboard.put("status", report.getStatus());
        dashboard.put("revisionNumber", report.getRevisionNumber());
        dashboard.put("completedSections", completedSections);
        dashboard.put("totalSections", 4);
        dashboard.put("completenessPercentage", completenessPercentage);
        dashboard.put("sections", sections);

        return ResponseEntity.ok(dashboard);
    }
}
