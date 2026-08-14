package com.dev.mtrs.projects.qualifyguruv2.identity.internal.ports.out;

public interface AuthTokenPort {
    String generateToken(Long userId);
    boolean isTokenValid(String token);
    String extractUserId(String token);
}
