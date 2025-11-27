package com.ecobazzar.eco.bazzar.util;

import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import org.slf4j.Logger;
import io.jsonwebtoken.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    private final SecretKey key;
    private final long expirationMs;

    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.expiration-ms}") long expirationMs) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
        log.info("JWT key initialized (len={} bytes).", secret.getBytes(StandardCharsets.UTF_8).length);
    }

    public String generateToken(String email, String role, Long userId) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key)
                .compact();
    }

    public String generateToken(String email, Collection<String> roles, Long userId, boolean includeSingleRoleClaim) {
        var now = new Date();
        var b = Jwts.builder()
                .setSubject(email)
                .claim("roles", roles)
                .claim("userId", userId)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expirationMs))
                .signWith(key);
        if (includeSingleRoleClaim && roles != null && !roles.isEmpty()) {
            b.claim("role", roles.iterator().next());
        }
        return b.compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .clockSkewSeconds(60)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);        // throws if invalid/expired
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("JWT expired for subject={}", e.getClaims() != null ? e.getClaims().getSubject() : "unknown");
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("JWT invalid: {}", e.getMessage());
            return false;
        }
    }

    public String extractUsername(String token) { return getClaims(token).getSubject(); }
    public String extractRole(String token) { return getClaims(token).get("role", String.class); }
    public List<String> extractRoles(String token) {
        Object rolesObj = getClaims(token).get("roles");
        if (rolesObj instanceof List<?> list) return list.stream().map(String::valueOf).toList();
        return List.of();
    }
    public Long extractUserId(String token) { return getClaims(token).get("userId", Long.class); }
}  
