package com.dev.mtrs.projects.qualifyguruv2.resumeOptimization.internal;

import com.dev.mtrs.projects.qualifyguruv2.resumeOptimization.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.PublishedEvents;

import java.io.ByteArrayInputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ApplicationModuleTest
public class ResumeOptimizationModuleTest {

    @Autowired
    private ResumeOptimizationService service;

    @MockitoBean
    private ResumeTextExtractorPort textExtractor;

    @MockitoBean
    private ResumeOptimizationPort aiAdapter;

    @Test
    void shouldPublishEventWhenResumeIsProcessed(PublishedEvents events) {
// Arrange
        JobDescriptionRequest job = new JobDescriptionRequest("Junior Software Developer", "Requires Java and Spring Boot");
        ByteArrayInputStream dummyPdfStream = new ByteArrayInputStream("mock pdf content".getBytes());

        // Tell Mockito to intercept the calls and return our fake data instantly
        when(textExtractor.extractText(any())).thenReturn("Fake resume text");

        // NOTE: Adjust the arguments here if your AdaptedResumeResponse constructor looks different!
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