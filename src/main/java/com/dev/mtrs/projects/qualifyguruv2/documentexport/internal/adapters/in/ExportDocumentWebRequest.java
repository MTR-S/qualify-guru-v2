package com.dev.mtrs.projects.qualifyguruv2.documentexport.internal.adapters.in;

import com.dev.mtrs.projects.qualifyguruv2.documentexport.internal.domain.DocumentFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ExportDocumentWebRequest(

        @NotBlank(message = "Content cannot be empty")
        String content,

        @NotNull(message = "Format is required")
        DocumentFormat format
) {
}
