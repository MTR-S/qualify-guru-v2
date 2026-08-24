package com.dev.mtrs.projects.qualifyguruv2.documentexport.internal.adapters.in;

import com.dev.mtrs.projects.qualifyguruv2.documentexport.internal.domain.DocumentExportRequest;
import com.dev.mtrs.projects.qualifyguruv2.documentexport.internal.domain.DocumentExportResponse;
import com.dev.mtrs.projects.qualifyguruv2.documentexport.internal.ports.in.DocumentExportUseCase;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/documents")
public class DocumentExportController {

    private final DocumentExportUseCase documentExportUseCase;

    public DocumentExportController(DocumentExportUseCase documentExportUseCase) {
        this.documentExportUseCase = documentExportUseCase;
    }


    @PostMapping("/export")
    public ResponseEntity<DocumentExportResponse> exportDocument(
            @Valid @RequestBody ExportDocumentWebRequest webRequest,
            Principal principal) {

        if (principal == null || principal.getName() == null) {
            return ResponseEntity.status(401).build();
        }
        System.out.println(principal.getName());
        String securelyExtractedCandidateId = principal.getName();

        DocumentExportRequest domainRequest = new DocumentExportRequest(
                securelyExtractedCandidateId,
                webRequest.content(),
                webRequest.format()
        );

        DocumentExportResponse response = documentExportUseCase.exportDocument(domainRequest);

        return ResponseEntity.ok(response);
    }
}
