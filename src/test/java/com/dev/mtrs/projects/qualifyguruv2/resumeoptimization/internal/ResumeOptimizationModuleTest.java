package com.dev.mtrs.projects.qualifyguruv2.resumeoptimization.internal;

import com.dev.mtrs.projects.qualifyguruv2.resumeoptimization.*;

import com.dev.mtrs.projects.qualifyguruv2.resumeoptimization.internal.adapters.in.JobDescriptionRequest;
import com.dev.mtrs.projects.qualifyguruv2.resumeoptimization.internal.adapters.in.ResumeOptimizationService;
import com.dev.mtrs.projects.qualifyguruv2.resumeoptimization.internal.ports.out.ResumeAiOptimizationPort;
import com.dev.mtrs.projects.qualifyguruv2.resumeoptimization.internal.ports.out.ResumeTextExtractorPort;
import com.dev.mtrs.projects.qualifyguruv2.resumeoptimization.internal.domain.AdaptedResumeResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.PublishedEvents;

import java.io.ByteArrayInputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import org.springframework.security.test.context.support.WithMockUser;

@ActiveProfiles("test")
@ApplicationModuleTest
@WithMockUser
public class ResumeOptimizationModuleTest {

    @Autowired
    private ResumeOptimizationService service;

    @MockitoBean
    private ResumeTextExtractorPort textExtractor;

    @MockitoBean
    private ResumeAiOptimizationPort aiAdapter;

    @Test
    void shouldPublishEventWhenResumeIsProcessed(PublishedEvents events) {
        // Arrange
        JobDescriptionRequest job = new JobDescriptionRequest("Junior Software Developer", "Requires Java and Spring Boot");
        ByteArrayInputStream dummyPdfStream = new ByteArrayInputStream("mock pdf content".getBytes());

        when(textExtractor.extractText(any())).thenReturn("Fake resume text");

        when(aiAdapter.adaptResume(anyString(), any())).thenReturn(
                new AdaptedResumeResponse("Mocked text", 95, List.of())
        );

        // Act
        service.extractAndProcessResume(dummyPdfStream, job);

        // Assert
        var matchingEvents = events.ofType(ResumeOptimizedEvent.class);
        assertThat(matchingEvents).hasSize(1);
        assertThat(matchingEvents.iterator().next().jobTitle()).isEqualTo("Junior Software Developer");
    }
}