package com.dev.mtrs.projects.qualifyguruv2.resumeOptimization;

public interface ResumeOptimizationPort {
    AdaptedResumeResponse adaptResume(String rawResumeText, JobDescriptionRequest jobDescriptionRequest);
}
