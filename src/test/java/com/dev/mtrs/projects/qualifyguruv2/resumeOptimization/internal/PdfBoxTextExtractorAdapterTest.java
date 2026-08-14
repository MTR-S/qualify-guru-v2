package com.dev.mtrs.projects.qualifyguruv2.resumeOptimization.internal;

import com.dev.mtrs.projects.qualifyguruv2.resumeOptimization.internal.adapters.out.PdfBoxExtractorPortAdapter;
import com.dev.mtrs.projects.qualifyguruv2.resumeOptimization.internal.ports.out.ResumeTextExtractorPort;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PdfBoxTextExtractorAdapterTest {

    private final String TEST_FILE_NAME = "qualifyguruv2-testfile.pdf";
    private final ResumeTextExtractorPort extractor = new PdfBoxExtractorPortAdapter();


    @Test
    void shouldExtractTextFromPdf() throws Exception {
        // 1. Arrange: Load the file dynamically from src/test/resources
        InputStream testPdfStream = getClass().getClassLoader().getResourceAsStream(TEST_FILE_NAME);

        assertNotNull(testPdfStream, "Test PDF file not found in resources!");

        // 2. Act: Pass the stream to your adapter
        String extractedText = extractor.extractText(testPdfStream);

        // 3. Assert: Verify the text
        assertThat(extractedText)
                .isNotNull()
                .isNotEmpty()
                .contains("This is a simple Test")
                .contains(", to check the PDF Extraction")
                .contains("on qualify-guru-v2 .")
                .contains("signed by Matheus Almeida")
                .doesNotContain("NullPointerException");
    }
}
