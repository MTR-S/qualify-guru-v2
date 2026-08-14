package com.dev.mtrs.projects.qualifyguruv2.resumeOptimization.internal.adapters.in;

import com.dev.mtrs.projects.qualifyguruv2.resumeOptimization.ResumeOptimizedEvent;
import com.dev.mtrs.projects.qualifyguruv2.resumeOptimization.internal.ports.in.ResumeOptimizationPort;
import com.dev.mtrs.projects.qualifyguruv2.resumeOptimization.internal.ports.out.ResumeAiOptimizationPort;
import com.dev.mtrs.projects.qualifyguruv2.resumeOptimization.internal.ports.out.ResumeTextExtractorPort;
import com.dev.mtrs.projects.qualifyguruv2.resumeOptimization.internal.domain.AdaptedResumeResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.time.LocalDateTime;

@Service
public class ResumeOptimizationService implements ResumeOptimizationPort {

    private final ResumeTextExtractorPort textExtractor;
    private final ResumeAiOptimizationPort aiAdapter;
    private final ApplicationEventPublisher eventPublisher;

    public ResumeOptimizationService(ResumeTextExtractorPort textExtractor,
                                     ResumeAiOptimizationPort aiAdapter,
                                     ApplicationEventPublisher eventPublisher) {
        this.textExtractor = textExtractor;
        this.aiAdapter = aiAdapter;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public AdaptedResumeResponse extractAndProcessResume(InputStream pdfStream, JobDescriptionRequest jobDescription) {

        String rawResumeText = textExtractor.extractText(pdfStream);

        AdaptedResumeResponse aiResponse = aiAdapter.adaptResume(rawResumeText, jobDescription);

        ResumeOptimizedEvent event = new ResumeOptimizedEvent(
                jobDescription.title(),
                aiResponse.compatibilityPercentage(),
                LocalDateTime.now()
        );

        eventPublisher.publishEvent(event);

        return aiResponse;
    }
}
