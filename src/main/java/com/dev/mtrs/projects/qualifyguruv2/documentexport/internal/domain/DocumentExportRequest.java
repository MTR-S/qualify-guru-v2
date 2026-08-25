package com.dev.mtrs.projects.qualifyguruv2.documentexport.internal.domain;

public record DocumentExportRequest(
        String candidateId,
        String content,
        DocumentFormat format
) {
}
