
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
public class GovernanceMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Governance metrics
    private Integer totalBoardMembers;
    private Integer independentBoardMembers;
    private Integer eligibleForAntiCorruptionTraining;
    private Integer completedAntiCorruptionTraining;

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

    public GovernanceMetric() {
    }

    @PrePersist
    @PreUpdate
    public void validateMetrics() {

        validateNonNegative("Total board members", totalBoardMembers);
        validateNonNegative("Independent board members",
                independentBoardMembers);
        validateNonNegative("Eligible training count",
                eligibleForAntiCorruptionTraining);
        validateNonNegative("Completed training count",
                completedAntiCorruptionTraining);

        if (totalBoardMembers != null
                && independentBoardMembers != null
                && independentBoardMembers > totalBoardMembers) {
            throw new IllegalArgumentException(
                    "Independent board members cannot exceed total board members");
        }

        if (eligibleForAntiCorruptionTraining != null
                && completedAntiCorruptionTraining != null
                && completedAntiCorruptionTraining
                > eligibleForAntiCorruptionTraining) {
            throw new IllegalArgumentException(
                    "Completed training cannot exceed eligible people");
        }
    }

    private void validateNonNegative(String fieldName, Integer value) {
        if (value != null && value < 0) {
            throw new IllegalArgumentException(
                    fieldName + " cannot be negative");
        }
    }

    public Double getIndependentBoardPercentage() {
        if (totalBoardMembers == null || totalBoardMembers == 0
                || independentBoardMembers == null) {
            return null;
        }

        return independentBoardMembers * 100.0 / totalBoardMembers;
    }

    public Double getAntiCorruptionTrainingCompletionPercentage() {
        if (eligibleForAntiCorruptionTraining == null
                || eligibleForAntiCorruptionTraining == 0
                || completedAntiCorruptionTraining == null) {
            return null;
        }

        return completedAntiCorruptionTraining * 100.0
                / eligibleForAntiCorruptionTraining;
    }
}
