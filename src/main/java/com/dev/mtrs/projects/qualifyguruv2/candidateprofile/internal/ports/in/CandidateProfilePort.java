package com.dev.mtrs.projects.qualifyguruv2.candidateprofile.internal.ports.in;

import com.dev.mtrs.projects.qualifyguruv2.candidateprofile.internal.domain.SavedResume;

public interface CandidateProfilePort {
    SavedResume saveOptimizedResume(Long userId, byte[] pdfBytes, String originalFilename, String title);
}
