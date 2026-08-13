package com.dev.mtrs.projects.qualifyguruv2.statistics.internal;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "STAT_OPTIMIZATION_METRICS")
public class OptimizationMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String jobTitle;

    @Column(nullable = false)
    private int compatibilityPercentage;

    @Column(nullable = false)
    private LocalDateTime optimizedAt;

    protected OptimizationMetric() {}

    public OptimizationMetric(String jobTitle, int compatibilityPercentage, LocalDateTime optimizedAt) {
        this.jobTitle = jobTitle;
        this.compatibilityPercentage = compatibilityPercentage;
        this.optimizedAt = optimizedAt;
    }

    public Long getId() { return id; }
    public String getJobTitle() { return jobTitle; }
    public int getCompatibilityPercentage() { return compatibilityPercentage; }
    public LocalDateTime getOptimizedAt() { return optimizedAt; }
}
