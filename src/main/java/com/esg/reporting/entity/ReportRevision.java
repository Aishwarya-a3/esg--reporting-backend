
package com.esg.reporting.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Column;

import lombok.Data;

@Entity
@Data
public class ReportRevision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "report_id", nullable = false)
    private EsgReport report;

    private Integer revisionNumber;

    private String previousStatus;

    private String newStatus;

    @Column(length = 2000)
    private String reviewerComment;

    private String changedBy;

    private LocalDateTime changedAt;

    public ReportRevision() {
    }

    @PrePersist
    public void setChangeTime() {
        if (changedAt == null) {
            changedAt = LocalDateTime.now();
        }
    }
}
