package com.dev.mtrs.projects.qualifyguruv2.candidateprofile.internal.adapters.out;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.net.URI;

@Configuration
public class S3Config {

    @Bean
    @Profile("local")
    public S3Client localS3Client(@Value("${minio.access-key}") String accessKey,
                                  @Value("${minio.secret-key}") String secretKey,
                                  @Value("${minio.endpoint}") String endpointUrl) {
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
}
