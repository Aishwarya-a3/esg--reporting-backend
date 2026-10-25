
package com.esg.reporting.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import lombok.Data;

@Entity
@Data
public class EnvironmentalMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Environmental metrics
    private Double scope1Emissions;
    private Double scope2Emissions;
    private Double scope3Emissions;
    private Double energyConsumption;
    private Double waterWithdrawal;
    private Double totalWaste;
    private Double recycledWaste;

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

    public EnvironmentalMetric() {
    }

    // Set ID when needed
    public void setId(Long id) {
        this.id = id;
    }

    // Calculate recycling percentage
    public Double getRecyclingPercentage() {
        if (totalWaste == null || totalWaste == 0) {
            return null;
        }

        if (recycledWaste == null) {
            return 0.0;
        }

        return (recycledWaste / totalWaste) * 100.0;
    }
}
