package com.dev.mtrs.projects.qualifyguruv2.resumeOptimization;

import java.util.List;

public record AdaptedResumeResponse(
        String  optimizedResumeText,
        int compatibilityPercentage,
        List<String> missingSkills
) {
}
