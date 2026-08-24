package com.dev.mtrs.projects.qualifyguruv2.documentexport.internal.adapters.out;

import com.dev.mtrs.projects.qualifyguruv2.documentexport.internal.domain.DocumentStorageException;
import com.dev.mtrs.projects.qualifyguruv2.documentexport.internal.ports.out.DocumentStoragePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.InputStream;
import java.time.Duration;

@Component
public class S3DocumentStorageAdapter implements DocumentStoragePort {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final String bucketName;

    public S3DocumentStorageAdapter(S3Client s3Client,
                                    S3Presigner s3Presigner,
                                    @Value("${spring.minio.bucket-resumes:mybukcetname2}") String bucketName) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
        this.bucketName = bucketName;
    }

    @Override
    public String storeAndGenerateUrl(String fileName, String contentType,
                                      InputStream contentStream, long contentLength) {

        try {
            uploadDocument(fileName, contentType, contentStream, contentLength);

            return generatePresignedUrl(fileName);
        } catch (S3Exception e) {
            throw new DocumentStorageException("Failed to store document into the Object Storage or generate URL");
        }
    }

    private void uploadDocument(String fileName, String contentType,
                                InputStream contentStream, long contentLength) {

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(contentType)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(contentStream, contentLength));
    }

    private String generatePresignedUrl(String fileName) {

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(15))
                .getObjectRequest(req -> req.bucket(bucketName).key(fileName))
                .build();

        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);

        return presignedRequest.url().toString();
    }
}
