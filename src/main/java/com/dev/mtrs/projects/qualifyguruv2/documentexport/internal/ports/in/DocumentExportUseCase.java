package com.dev.mtrs.projects.qualifyguruv2.documentexport.internal.ports.in;

import com.dev.mtrs.projects.qualifyguruv2.documentexport.internal.domain.DocumentExportRequest;
import com.dev.mtrs.projects.qualifyguruv2.documentexport.internal.domain.DocumentExportResponse;

public interface DocumentExportUseCase {

    DocumentExportResponse exportDocument(DocumentExportRequest request);
}
