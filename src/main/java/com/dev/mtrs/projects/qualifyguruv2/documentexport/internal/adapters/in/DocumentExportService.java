package com.dev.mtrs.projects.qualifyguruv2.documentexport.internal.adapters.in;

import com.dev.mtrs.projects.qualifyguruv2.documentexport.internal.domain.DocumentExportRequest;
import com.dev.mtrs.projects.qualifyguruv2.documentexport.internal.domain.DocumentExportResponse;
import com.dev.mtrs.projects.qualifyguruv2.documentexport.internal.domain.DocumentFormat;
import com.dev.mtrs.projects.qualifyguruv2.documentexport.internal.domain.DocumentStorageException;
import com.dev.mtrs.projects.qualifyguruv2.documentexport.internal.ports.in.DocumentExportUseCase;
import com.dev.mtrs.projects.qualifyguruv2.documentexport.internal.ports.out.DocumentStoragePort;
import com.dev.mtrs.projects.qualifyguruv2.documentexport.internal.ports.out.PdfGeneratorPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class DocumentExportService implements DocumentExportUseCase {

    private static final Logger log = LoggerFactory.getLogger(DocumentExportService.class);

    private final DocumentStoragePort documentStoragePort;
    private final PdfGeneratorPort pdfGeneratorPort;

    public DocumentExportService(DocumentStoragePort documentStoragePort, PdfGeneratorPort pdfGeneratorPort) {
        this.documentStoragePort = documentStoragePort;
        this.pdfGeneratorPort = pdfGeneratorPort;
    }


    @Override
    public DocumentExportResponse exportDocument(DocumentExportRequest request) {
        String documentId = UUID.randomUUID().toString();
        long expiresAt = Instant.now().plus(15, ChronoUnit.MINUTES).toEpochMilli();

        return handleFileFormat(request, documentId, expiresAt);

    }


    private DocumentExportResponse handleFileFormat(DocumentExportRequest request, String documentId, long expiresAt)
            throws UnsupportedOperationException {

        if (request.format() == DocumentFormat.MARKDOWN) {
            return handleMarkdownExport(request, documentId, expiresAt);
        } else if (request.format() == DocumentFormat.PDF) {
            return handlePdfExport(request, documentId, expiresAt);
        } else {
            throw new DocumentStorageException("Format not supported: " + request.format());
        }
    }

    private DocumentExportResponse handleMarkdownExport(DocumentExportRequest request, String documentId, long expiresAt) {
        String objectKey = request.candidateId() + "/" + documentId + ".md";
        byte[] contentBytes = request.content().getBytes(StandardCharsets.UTF_8);

        try (InputStream inputStream = new ByteArrayInputStream(contentBytes)) {
            String url = documentStoragePort.storeAndGenerateUrl(objectKey, "text/markdown", inputStream, contentBytes.length);
            return new DocumentExportResponse(documentId, url, expiresAt);
        } catch (Exception e) {
            throw new DocumentStorageException("Failed to upload Markdown document to storage", e);
        }
    }

    private DocumentExportResponse handlePdfExport(DocumentExportRequest request, String documentId, long expiresAt) {
        String objectKey = request.candidateId() + "/" + documentId + ".pdf";
        Path tempPdfPath = null;

        try {
            tempPdfPath = pdfGeneratorPort.generatePdfFromMarkdown(request.content());
            long fileSize = Files.size(tempPdfPath);

            try (InputStream inputStream = Files.newInputStream(tempPdfPath)) {
                String url = documentStoragePort.storeAndGenerateUrl(objectKey, "application/pdf", inputStream, fileSize);
                return new DocumentExportResponse(documentId, url, expiresAt);
            }
        } catch (Exception e) {
            throw new DocumentStorageException("Failed to upload Pdf document to storage", e);

        } finally {
            if (tempPdfPath != null) {
                try {
                    Files.deleteIfExists(tempPdfPath);
                } catch (Exception e) {

                    log.error("Failed to delete temporary PDF file at path: {}", tempPdfPath, e);
                }
            }
        }
    }
}
