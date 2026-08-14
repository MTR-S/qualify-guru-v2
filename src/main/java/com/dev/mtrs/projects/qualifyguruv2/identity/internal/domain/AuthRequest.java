package com.dev.mtrs.projects.qualifyguruv2.identity.internal.domain;

public record AuthRequest(
        String email,
        String password
) {
}
