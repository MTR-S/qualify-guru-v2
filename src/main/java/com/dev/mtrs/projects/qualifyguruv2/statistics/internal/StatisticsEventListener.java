package com.dev.mtrs.projects.qualifyguruv2.statistics.internal;
import com.dev.mtrs.projects.qualifyguruv2.resumeOptimization.ResumeOptimizedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
public class StatisticsEventListener {

    private static final Logger log = LoggerFactory.getLogger(StatisticsEventListener.class);

    // TODO: Inject your StatisticsRepository here later to save to the database

    // This annotation automatically makes the execution ASYNCHRONOUS
    // and binds it to the completion of the main database transaction.
    @ApplicationModuleListener
    void on(ResumeOptimizedEvent event) {

        log.info("Statistics module received event! Job: {}, Match: {}%",
                event.jobTitle(),
                event.compatibilityPercentage());

        // Logic to save the metrics to the statistics database goes here
    }
}
