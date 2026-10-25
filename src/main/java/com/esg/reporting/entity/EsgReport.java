
package com.esg.reporting.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import jakarta.persistence.Column;

@Entity
@Data
public class EsgReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Organization name is required")
    @Size(max = 150, message = "Organization name must not exceed 150 characters")
    private String organizationName;

    @NotBlank(message = "Reporting period is required")
    private String reportingPeriod;

    private String status = "DRAFT";

    @Size(max = 2000, message = "Review comment must not exceed 2000 characters")
    private String reviewComment;

    // Revision tracking
    @Column(nullable = false)
    private Integer revisionNumber = 1;
    public EsgReport() {
    }
}
