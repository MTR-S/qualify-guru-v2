package com.dev.mtrs.projects.qualifyguruv2.resumeOptimization;

import java.time.LocalDateTime;

public record ResumeOptimizedEvent(
        String jobTitle,
        int compatibilityPercentage,
        LocalDateTime optimizedAt
) {
}
