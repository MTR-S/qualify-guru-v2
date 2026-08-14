package com.dev.mtrs.projects.qualifyguruv2.identity.internal.adapters.out;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userId;

        if (verifyHeader(authHeader)) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = extractToken(authHeader);

        try {
            if (jwtService.isTokenValid(jwt)) {
                userId = jwtService.extractUserId(jwt);

                if (verifyValidUserIdAndUnauthenticated(userId)) {

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userId,
                            null,
                            Collections.emptyList()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            logger.warn("Invalid JWT token processed in filter: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private boolean verifyHeader(String authHeader) {
        return authHeader == null || !authHeader.startsWith("Bearer ");
    }

    private String extractToken(String authHeader) {
        return authHeader.substring(7);
    }

    private boolean verifyValidUserIdAndUnauthenticated(String userId) {
        return userId != null && SecurityContextHolder.getContext().getAuthentication() == null;
    }
}

