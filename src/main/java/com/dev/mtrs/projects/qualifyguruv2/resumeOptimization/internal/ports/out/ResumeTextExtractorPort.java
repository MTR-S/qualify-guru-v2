package com.dev.mtrs.projects.qualifyguruv2.resumeOptimization.internal.ports.out;

import java.io.InputStream;

public interface ResumeTextExtractorPort {
    String extractText(InputStream fileStream);
}
