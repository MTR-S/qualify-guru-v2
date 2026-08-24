package com.dev.mtrs.projects.qualifyguruv2.documentexport.internal.domain;

public record DocumentExportResponse(
        String documentId,
        String presignedUrl,
        long expiresAtEpoch
) {
}
