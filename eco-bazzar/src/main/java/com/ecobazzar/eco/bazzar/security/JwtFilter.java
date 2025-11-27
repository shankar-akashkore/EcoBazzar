package com.ecobazzar.eco.bazzar.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.ecobazzar.eco.bazzar.util.JwtUtil;
import io.jsonwebtoken.Claims;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);
    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) { this.jwtUtil = jwtUtil; }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String path = request.getRequestURI();

        if (path.startsWith("/api/auth/") || path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui")) {
            chain.doFilter(request, response); return;
        }
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            if (path.equals("/api/products") || path.matches("^/api/products/\\d+$")) {
                chain.doFilter(request, response); return;
            }
        }

        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response); return;
        }

        final String token = authHeader.substring(7);

        try {
            if (!jwtUtil.validateToken(token)) {
                log.warn("JWT validation failed for path {}", path);
                chain.doFilter(request, response); return;
            }

            Claims claims = jwtUtil.getClaims(token);
            String email = claims.getSubject();
            Collection<SimpleGrantedAuthority> authorities = extractAuthorities(claims);

            var authentication = new UsernamePasswordAuthenticationToken(email, null, authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.info("Authenticated {} with authorities {} for {}", email, authorities, path);
        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT for subject={} path={}", e.getClaims() != null ? e.getClaims().getSubject() : "unknown", path);
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Invalid JWT: {} path={}", e.getMessage(), path);
        } catch (Exception e) {
            log.warn("JWT processing error: {} path={}", e.getMessage(), path);
        }

        chain.doFilter(request, response);
    }

    private Collection<SimpleGrantedAuthority> extractAuthorities(Claims claims) {
        List<SimpleGrantedAuthority> result = new ArrayList<>();
        Object rolesObj = claims.get("roles");
        if (rolesObj instanceof List<?> list) {
            for (Object o : list) result.add(new SimpleGrantedAuthority(normalizeRole(String.valueOf(o))));
        }
        String singleRole = claims.get("role", String.class);
        if (singleRole != null && !singleRole.isBlank()) {
            result.add(new SimpleGrantedAuthority(normalizeRole(singleRole)));
        }
        if (result.isEmpty()) result.add(new SimpleGrantedAuthority("ROLE_USER"));
        return result;
    }

    private String normalizeRole(String role) {
        if (role == null || role.isBlank()) return "ROLE_USER";
        role = role.trim().toUpperCase();
        return role.startsWith("ROLE_") ? role : "ROLE_" + role;
    }
}
