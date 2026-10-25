
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
public class SocialMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Social metrics
    private Integer startingWorkforce;
    private Integer endingWorkforce;
    private Integer employeeDepartures;
    private Double trainingHours;
    private Integer workplaceInjuryIncidents;

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

    public SocialMetric() {
    }

    @PrePersist
    @PreUpdate
    public void validateMetrics() {

        if (startingWorkforce != null && startingWorkforce < 0)
            throw new IllegalArgumentException(
                "Starting workforce cannot be negative");

        if (endingWorkforce != null && endingWorkforce < 0)
            throw new IllegalArgumentException(
                "Ending workforce cannot be negative");

        if (employeeDepartures != null && employeeDepartures < 0)
            throw new IllegalArgumentException(
                "Employee departures cannot be negative");

        if (trainingHours != null &&
                (!Double.isFinite(trainingHours) || trainingHours < 0))
            throw new IllegalArgumentException(
                "Training hours must be non-negative");

        if (workplaceInjuryIncidents != null &&
                workplaceInjuryIncidents < 0)
            throw new IllegalArgumentException(
                "Workplace injury incidents cannot be negative");
    }

    public Double getAverageHeadcount() {
        if (startingWorkforce == null || endingWorkforce == null) {
            return null;
        }

        return (startingWorkforce + endingWorkforce) / 2.0;
    }

    public Double getTurnoverPercentage() {
        Double average = getAverageHeadcount();

        if (average == null || average == 0 ||
                employeeDepartures == null) {
            return null;
        }

        return (employeeDepartures / average) * 100.0;
    }

    public Double getTrainingHoursPerEmployee() {
        Double average = getAverageHeadcount();

        if (average == null || average == 0 ||
                trainingHours == null) {
            return null;
        }

        return trainingHours / average;
    }
}
