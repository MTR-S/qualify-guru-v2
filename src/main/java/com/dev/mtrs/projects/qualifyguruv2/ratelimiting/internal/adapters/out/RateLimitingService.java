package com.dev.mtrs.projects.qualifyguruv2.ratelimiting.internal.adapters.out;

import com.dev.mtrs.projects.qualifyguruv2.ratelimiting.internal.domain.ApiEndpoint;
import com.dev.mtrs.projects.qualifyguruv2.ratelimiting.internal.ports.RateLimitingPort;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitingService implements RateLimitingPort {

    private final Map<String, Bucket> individualUsersBucketCache = new ConcurrentHashMap<>();

    private final Bucket globalBucket;

    public RateLimitingService() {
        Bandwidth globalLimit = Bandwidth.builder()
                .capacity(10)
                .refillIntervally(10, Duration.ofDays(1))
                .build();

        this.globalBucket = Bucket.builder().addLimit(globalLimit).build();
    }

    @Override
    public Bucket getGlobalBucket() {
        return globalBucket;
    }

    @Override
    public Bucket resolveBucket(Long userId, ApiEndpoint endpoint) {
        String cacheKey = userId + "_" + endpoint.name();

        return individualUsersBucketCache.computeIfAbsent(cacheKey, key -> createNewBucket(endpoint));
    }

    private Bucket createNewBucket(ApiEndpoint endpoint) {

        return switch (endpoint) {

            case AI_OPTIMIZE -> {
                Bandwidth limit = Bandwidth.builder()
                        .capacity(5)
                        .refillIntervally(5, Duration.ofHours(1))
                        .build();
                yield Bucket.builder().addLimit(limit).build();
            }
            case RESUME_UPLOAD -> {
                Bandwidth limit = Bandwidth.builder()
                        .capacity(10)
                        .refillIntervally(10, Duration.ofDays(1))
                        .build();
                yield Bucket.builder().addLimit(limit).build();
            }
            case DEFAULT -> {
                Bandwidth limit = Bandwidth.builder()
                        .capacity(10000)
                        .refillIntervally(5000, Duration.ofHours(1))
                        .build();
                yield Bucket.builder().addLimit(limit).build();
            }

        };
    }
}
