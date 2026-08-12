package com.dev.mtrs.projects.qualifyguruv2.resumeOptimization;

import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class ResumeOptimizationService {

    private final ResumeTextExtractorPort textExtractor;
    private final ResumeOptimizationPort aiAdapter;

    public ResumeOptimizationService(ResumeTextExtractorPort textExtractor, ResumeOptimizationPort aiAdapter) {
        this.textExtractor = textExtractor;
        this.aiAdapter = aiAdapter;
    }

    public AdaptedResumeResponse extractAndProcessResume(InputStream pdfStream, JobDescriptionRequest jobDescription) {

        String rawResumeText = textExtractor.extractText(pdfStream);

        return aiAdapter.adaptResume(rawResumeText, jobDescription);
    }
}
