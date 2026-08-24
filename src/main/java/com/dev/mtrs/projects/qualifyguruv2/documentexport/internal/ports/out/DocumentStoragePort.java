package com.dev.mtrs.projects.qualifyguruv2.documentexport.internal.ports.out;

import java.io.InputStream;

public interface DocumentStoragePort {
    /**
     * Uploads the document stream and generates a time-limited download URL.
     *
     * @param fileName The unique name of the file to store.
     * @param contentType The MIME type (e.g., "text/markdown" or "application/pdf").
     * @param contentStream The stream containing the document data.
     * @param contentLength The size of the payload in bytes.
     * @return The pre-signed URL as a String.
     */
    String storeAndGenerateUrl(String fileName, String contentType,
                               InputStream contentStream, long contentLength);
}
