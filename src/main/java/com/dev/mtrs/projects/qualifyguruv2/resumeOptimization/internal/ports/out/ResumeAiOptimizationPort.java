package com.dev.mtrs.projects.qualifyguruv2.resumeOptimization.internal.ports.out;

import com.dev.mtrs.projects.qualifyguruv2.resumeOptimization.internal.adapters.in.JobDescriptionRequest;
import com.dev.mtrs.projects.qualifyguruv2.resumeOptimization.internal.domain.AdaptedResumeResponse;

public interface ResumeAiOptimizationPort {
    AdaptedResumeResponse adaptResume(String rawResumeText, JobDescriptionRequest jobDescriptionRequest);
}
