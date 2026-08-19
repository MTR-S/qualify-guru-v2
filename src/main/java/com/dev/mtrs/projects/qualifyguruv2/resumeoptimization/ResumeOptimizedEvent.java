package com.dev.mtrs.projects.qualifyguruv2.resumeoptimization;

import java.time.LocalDateTime;

public record ResumeOptimizedEvent(
        String jobTitle,
        int compatibilityPercentage,
        LocalDateTime optimizedAt
) {
}
