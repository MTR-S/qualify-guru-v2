package com.dev.mtrs.projects.qualifyguruv2.candidateprofile.internal.domain;

import java.time.LocalDateTime;

public record SavedResumeResponse(
        String title,
        String s3FileUrl,
        LocalDateTime savedAt
) {
}
