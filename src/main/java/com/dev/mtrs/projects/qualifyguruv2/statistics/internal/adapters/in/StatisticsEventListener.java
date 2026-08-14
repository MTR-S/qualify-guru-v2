package com.dev.mtrs.projects.qualifyguruv2.statistics.internal.adapters.in;
import com.dev.mtrs.projects.qualifyguruv2.resumeOptimization.ResumeOptimizedEvent;
import com.dev.mtrs.projects.qualifyguruv2.statistics.internal.adapters.out.OptimizationMetricEntity;
import com.dev.mtrs.projects.qualifyguruv2.statistics.internal.adapters.out.StatisticsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
public class StatisticsEventListener {

    private static final Logger log = LoggerFactory.getLogger(StatisticsEventListener.class);

    private final StatisticsRepository repository;

    public StatisticsEventListener(StatisticsRepository repository) {
        this.repository = repository;
    }

    @ApplicationModuleListener
    void on(ResumeOptimizedEvent event) {

        log.info("Statistics module received event! Job: {}, Match: {}%",
                event.jobTitle(),
                event.compatibilityPercentage());

        OptimizationMetricEntity metric = new OptimizationMetricEntity(
                event.jobTitle(),
                event.compatibilityPercentage(),
                event.optimizedAt()
        );

        repository.save(metric);

        log.info("Successfully saved metric to database.");
    }
}
