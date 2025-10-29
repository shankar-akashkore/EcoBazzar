package com.ecobazzar.eco.bazzar.util;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Date;


@Component
public class JwtUtil {

	 private final SecretKey key;
	 
	 private final long expirationMs;
	 
	 public JwtUtil(@Value("${jwt.secret}")String secret,
             @Value("${jwt.expiration-ms}")long expirationMs){
           	  this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
           	  this.expirationMs = expirationMs;
           	  }
	 
	 public String generateToken(String email, String role, long userId) {
		 return Jwts.builder()
				 .subject(email)
				 .claim("role", role)
				 .claim("userId", userId)
				 .issuedAt(new Date())
				 .expiration(new Date(System.currentTimeMillis() + expirationMs))
				 .signWith(key, SignatureAlgorithm.HS256)
				 .compact();
	 }
	 
	 public Boolean validateToken(String token) {
		 try {
			 Jwts.parser()
			 .verifyWith(key)
			 .build()
			 .parseSignedClaims(token);
			 return true;
		 }catch(JwtException | IllegalArgumentException e) {
			 return false;
		 } 
	 }
	 
	 
	 public Claims getClaims(String token) {
		 Jws<Claims> jws = Jwts.parser()
				 .verifyWith(key)
				 .build()
				 .parseSignedClaims(token);
		 return jws.getPayload();
	 }
}  
