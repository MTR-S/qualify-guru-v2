package com.dev.mtrs.projects.qualifyguruv2.documentexport.internal.ports.out;

import java.io.InputStream;

public interface DocumentStoragePort {

    String storeAndGenerateUrl(String fileName, String contentType,
                               InputStream contentStream, long contentLength);
}
