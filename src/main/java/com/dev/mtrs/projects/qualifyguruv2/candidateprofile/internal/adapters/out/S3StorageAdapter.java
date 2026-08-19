package com.dev.mtrs.projects.qualifyguruv2.candidateprofile.internal.adapters.out;

import com.dev.mtrs.projects.qualifyguruv2.candidateprofile.internal.ports.out.ObjectStoragePort;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.UUID;

@Service
public class S3StorageAdapter implements ObjectStoragePort {

    private final S3Client s3Client;
    private static final String BUCKET_NAME = "qualify-guru-resumes";

    public S3StorageAdapter(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public String putObjectAndGetURl(byte[] fileBytes, String originalFilename) {

        String uniqueFileName = generateUniqueFileName(originalFilename);

        PutObjectRequest putObjectRequest = buildObjectRequest(uniqueFileName);

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(fileBytes));

        return getBucketUrl(uniqueFileName);

    }

    private String generateUniqueFileName(String originalFilename) {

        String fileExtension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : ".pdf";

        return  UUID.randomUUID().toString() + fileExtension;
    }

    private PutObjectRequest buildObjectRequest(String uniqueFileName) {

       return PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(uniqueFileName)
                .contentType("application/pdf")
                .build();
    }

    private String getBucketUrl(String uniqueFileName) {
        return s3Client.utilities()
                .getUrl(builder -> builder.bucket(BUCKET_NAME).key(uniqueFileName))
                .toExternalForm();
    }
}
