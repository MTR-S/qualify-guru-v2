package com.dev.mtrs.projects.qualifyguruv2.documentexport.internal.adapters.in;

import com.dev.mtrs.projects.qualifyguruv2.documentexport.internal.domain.DocumentExportRequest;
import com.dev.mtrs.projects.qualifyguruv2.documentexport.internal.domain.DocumentExportResponse;
import com.dev.mtrs.projects.qualifyguruv2.documentexport.internal.domain.DocumentFormat;
import com.dev.mtrs.projects.qualifyguruv2.documentexport.internal.ports.in.DocumentExportUseCase;
import com.dev.mtrs.projects.qualifyguruv2.documentexport.internal.ports.out.DocumentStoragePort;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class DocumentExportService implements DocumentExportUseCase {

    private final DocumentStoragePort documentStoragePort;

    public DocumentExportService(DocumentStoragePort documentStoragePort) {
        this.documentStoragePort = documentStoragePort;
    }

    @Override
    public DocumentExportResponse exportDocument(DocumentExportRequest request) {
        if (request.format() != DocumentFormat.MARKDOWN) {
            throw new UnsupportedOperationException("Format not yet supported: " + request.format());
        }

        String documentId = UUID.randomUUID().toString();
        String objectKey = request.candidateId() + "/" + documentId + ".md";

        byte[] contentBytes = request.content().getBytes(StandardCharsets.UTF_8);

        String presignedUrl;
        try (InputStream inputStream = new ByteArrayInputStream(contentBytes)) {
            presignedUrl = documentStoragePort.storeAndGenerateUrl(
                    objectKey,
                    "text/markdown",
                    inputStream,
                    contentBytes.length
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to stream document to storage", e);
        }

        long expiresAt = Instant.now().plus(15, ChronoUnit.MINUTES).toEpochMilli();

        return new DocumentExportResponse(documentId, presignedUrl, expiresAt);
    }
}
