package com.dev.mtrs.projects.qualifyguruv2.resumeOptimization.internal.ports.in;

import com.dev.mtrs.projects.qualifyguruv2.resumeOptimization.internal.adapters.in.JobDescriptionRequest;
import com.dev.mtrs.projects.qualifyguruv2.resumeOptimization.internal.domain.AdaptedResumeResponse;

import java.io.InputStream;

public interface ResumeOptimizationPort {
    AdaptedResumeResponse extractAndProcessResume(InputStream pdfStream, JobDescriptionRequest jobDescription);
}
