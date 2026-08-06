package com.dev.mtrs.projects.qualifyguruv2.resumeOptimization;

import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class ResumeOptimizationService {

    private final ResumeTextExtractor textExtractor;

    public ResumeOptimizationService(ResumeTextExtractor textExtractor) {
        this.textExtractor = textExtractor;
    }

    public String processResume(InputStream pdfStream) {

        return textExtractor.extractText(pdfStream);
    }
}
