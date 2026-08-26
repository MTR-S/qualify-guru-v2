package com.dev.mtrs.projects.qualifyguruv2.identity.internal.adapters.out;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
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

        final String jwt = extractToken(request);

        if (jwt == null) {
            filterChain.doFilter(request, response);

            return;
        }

        try {
            if (jwtService.isTokenValid(jwt)) {
                String userId = jwtService.extractUserId(jwt);

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


    private String extractToken(HttpServletRequest request) {
        // Strategy A: HttpOnly Cookie (Primary for Browsers)
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("access_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        // Strategy B: Authorization Header (Primary for Postman/Mobile/Server-to-Server)
        String authHeader = request.getHeader("Authorization");
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }

    private boolean verifyValidUserIdAndUnauthenticated(String userId) {
        return StringUtils.hasText(userId) && SecurityContextHolder.getContext().getAuthentication() == null;
    }
}

