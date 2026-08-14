package com.dev.mtrs.projects.qualifyguruv2.statistics.internal;

import com.dev.mtrs.projects.qualifyguruv2.resumeOptimization.ResumeOptimizedEvent;
import com.dev.mtrs.projects.qualifyguruv2.statistics.internal.adapters.out.StatisticsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ApplicationModuleTest
public class StatisticsModuleTest {

    @Autowired
    private StatisticsRepository repository;

    @Test
    void shouldProcessEventAndSaveToDatabase(Scenario scenario) {
        // Arrange: Create a fake event as if the other module published it
        ResumeOptimizedEvent event = new ResumeOptimizedEvent(
                "Junior Software Developer",
                95,
                LocalDateTime.now()
        );

        // Act & Assert using the Scenario API
        scenario.publish(event)
                .andWaitForStateChange(() -> repository.findAll())
                .andVerify(metrics -> {
                    assertThat(metrics).hasSize(1);
                    assertThat(metrics.get(0).getJobTitle()).isEqualTo("Junior Software Developer");
                    assertThat(metrics.get(0).getCompatibilityPercentage()).isEqualTo(95);
                });
    }

}
