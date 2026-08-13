package com.dev.mtrs.projects.qualifyguruv2.statistics.internal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatisticsRepository extends JpaRepository<OptimizationMetric, Long> {
}
