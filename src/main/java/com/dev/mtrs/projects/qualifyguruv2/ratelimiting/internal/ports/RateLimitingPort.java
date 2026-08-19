package com.dev.mtrs.projects.qualifyguruv2.ratelimiting.internal.ports;

import com.dev.mtrs.projects.qualifyguruv2.ratelimiting.internal.domain.ApiEndpoint;
import io.github.bucket4j.Bucket;

public interface RateLimitingPort {
    Bucket getGlobalBucket();
    Bucket resolveBucket(Long userId, ApiEndpoint endpoint);
}
