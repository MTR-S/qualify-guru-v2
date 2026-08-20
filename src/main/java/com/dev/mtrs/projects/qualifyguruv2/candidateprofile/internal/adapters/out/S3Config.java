package com.dev.mtrs.projects.qualifyguruv2.candidateprofile.internal.adapters.out;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.net.URI;

@Configuration
public class S3Config {

    private static final Logger log = LoggerFactory.getLogger(S3Config.class);

    @Bean
    @Profile("local")
    public S3Client localS3Client(@Value("${spring.minio.access-key}") String accessKey,
                                  @Value("${spring.minio.secret-key}") String secretKey,
                                  @Value("${spring.minio.endpoint}") String endpointUrl) {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

        return S3Client.builder()
                .endpointOverride(URI.create(endpointUrl))
                .region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .build())
                .build();
    }

    @Bean
    @Profile("!local")
    public S3Client prodS3Client() {
        // In production, the AWS SDK automatically fetches credentials and region
        // from the server's IAM Roles or Environment Variables.
        return S3Client.builder().build();
    }

    @Bean
    @Profile("local")
    public ApplicationRunner initializeMinioBucket(S3Client s3Client,
                                                   @Value("${spring.minio.bucket-name}") String bucketName) {
        return args -> {
            try {
                s3Client.headBucket(HeadBucketRequest.builder().bucket(bucketName).build());
                log.info("MinIO bucket '{}' already exists.", bucketName);

            } catch (S3Exception e) {
                if (e.statusCode() == 404 || e.statusCode() == 403) {
                    log.info("MinIO bucket '{}' not found. Creating it now...", bucketName);

                    s3Client.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());

                    log.info("MinIO bucket '{}' created successfully.", bucketName);
                } else {
                    log.error("Failed to communicate with MinIO: {}", e.getMessage());
                    throw e;
                }
            }
        };
    }
}