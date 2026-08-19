package com.dev.mtrs.projects.qualifyguruv2.ratelimiting.internal.adapters.out;

import com.dev.mtrs.projects.qualifyguruv2.ratelimiting.internal.domain.ApiEndpoint;
import com.dev.mtrs.projects.qualifyguruv2.ratelimiting.internal.ports.RateLimitingPort;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

@Component
public class RateLimitingInterceptor implements HandlerInterceptor {

    private final RateLimitingPort rateLimitingPort;

    public RateLimitingInterceptor(RateLimitingPort rateLimitingPort) {
        this.rateLimitingPort = rateLimitingPort;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestURI = request.getRequestURI();

        if (!globalBucketCanStore()) {
            buildErrorResponse(response, "API capacity reached. Please try again later.");
            return false;
        }

        Optional<Bucket> userIndividualBucket = resolveBucket(requestURI);

        if (userIndividualBucket.isEmpty()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        if (!userIndividualBucket.get().tryConsume(1)) {
            buildErrorResponse(response, "User rate limit reached for this endpoint.");
            return false;
        }

        return true;
    }

    private ApiEndpoint detectApiEndpoint(String requestURI) {
        if (requestURI.contains("/resume-optimization")) {
            return ApiEndpoint.AI_OPTIMIZE;
        } else if (requestURI.contains("/candidates/resumes")) {
            return ApiEndpoint.RESUME_UPLOAD;
        } else {
            return ApiEndpoint.DEFAULT;
        }
    }

    private boolean globalBucketCanStore() {
        return rateLimitingPort.getGlobalBucket().tryConsume(1);
    }

    private Optional<Bucket> resolveBucket(String requestURI) {
        try {
            String userIdStr = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if (userIdStr == null) {
                return Optional.empty();
            }

            Long userId = Long.valueOf(userIdStr);
            return Optional.of(rateLimitingPort.resolveBucket(userId, detectApiEndpoint(requestURI)));

        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private void buildErrorResponse(HttpServletResponse response, String message) throws Exception {
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());

        response.setContentType("application/json;charset=UTF-8");

        response.getWriter().write("{ \"error\": \"Too Many Requests\", \"message\": \"" + message + "\" }");
    }
}