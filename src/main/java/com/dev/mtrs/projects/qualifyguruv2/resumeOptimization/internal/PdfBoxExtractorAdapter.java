package com.dev.mtrs.projects.qualifyguruv2.resumeOptimization.internal;

import com.dev.mtrs.projects.qualifyguruv2.resumeOptimization.ResumeExtractionException;
import com.dev.mtrs.projects.qualifyguruv2.resumeOptimization.ResumeTextExtractor;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class PdfBoxExtractorAdapter implements ResumeTextExtractor {

    @Override
    public String extractText(InputStream fileStream) {
        try(PDDocument document = Loader.loadPDF(new RandomAccessReadBuffer(fileStream))) {

            PDFTextStripper stripper = new PDFTextStripper();

            return stripper.getText(document);

        } catch(IOException e) {
            throw new ResumeExtractionException("Failed to extract raw text from the provided PDF file." , e);
        }
    }
}
