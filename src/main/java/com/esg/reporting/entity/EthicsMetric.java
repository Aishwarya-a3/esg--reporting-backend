
package com.esg.reporting.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;

@Entity
@Data
public class EthicsMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ethics metrics
    private Integer reportedCases;
    private Integer openCases;
    private Integer closedCases;

    // Distinguishes no cases from missing data
    private Boolean dataReported;

    // Metadata fields
    private String unit;
    private String dataSource;
    private String calculationMethod;
    private String dataQuality;
    private String reportingPeriod;
    private String reportingBoundary;
    private String dataOwner;
    private String notes;
    private String assumptions;
    private String exclusions;

    @ManyToOne
    @JoinColumn(name = "report_id")
    private EsgReport report;

    public EthicsMetric() {
    }

    @PrePersist
    @PreUpdate
    public void validateMetrics() {

        if (reportedCases != null && reportedCases < 0) {
            throw new IllegalArgumentException(
                    "Reported cases cannot be negative");
        }

        if (openCases != null && openCases < 0) {
            throw new IllegalArgumentException(
                    "Open cases cannot be negative");
        }

        if (closedCases != null && closedCases < 0) {
            throw new IllegalArgumentException(
                    "Closed cases cannot be negative");
        }

        if (reportedCases != null && openCases != null
                && closedCases != null
                && (long) openCases + closedCases > reportedCases) {
            throw new IllegalArgumentException(
                    "Open and closed cases cannot exceed reported cases");
        }

        if (Boolean.TRUE.equals(dataReported)
                && reportedCases == null) {
            throw new IllegalArgumentException(
                    "Reported cases must be provided when data is reported");
        }
    }
}
