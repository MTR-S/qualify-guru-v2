package com.dev.mtrs.projects.qualifyguruv2.resumeoptimization.internal.ports.out;

import com.dev.mtrs.projects.qualifyguruv2.resumeoptimization.internal.adapters.in.JobDescriptionRequest;
import com.dev.mtrs.projects.qualifyguruv2.resumeoptimization.internal.domain.AdaptedResumeResponse;

public interface ResumeAiOptimizationPort {
    AdaptedResumeResponse adaptResume(String rawResumeText, JobDescriptionRequest jobDescriptionRequest);
}
