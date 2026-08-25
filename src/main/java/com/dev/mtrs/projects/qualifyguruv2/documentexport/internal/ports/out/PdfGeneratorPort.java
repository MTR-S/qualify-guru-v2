package com.dev.mtrs.projects.qualifyguruv2.documentexport.internal.ports.out;

import java.nio.file.Path;

public interface PdfGeneratorPort {
    /**
     * Converts Markdown content to a PDF file on disk.
     *
     * @param markdownContent The AI-generated markdown.
     * @return Path to the generated temporary PDF file.
     */
    Path generatePdfFromMarkdown(String markdownContent);
}
