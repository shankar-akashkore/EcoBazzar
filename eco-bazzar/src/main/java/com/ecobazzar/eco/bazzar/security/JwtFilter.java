package com.ecobazzar.eco.bazzar.security;

import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ecobazzar.eco.bazzar.util.JwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	
	public JwtFilter(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		String header = request.getHeader("Authorization");
		
		if(header == null || !header.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		
		String token = header.substring(7);
		
		if(!jwtUtil.validateToken(token)) {
			filterChain.doFilter(request, response);
			return;
		}
		
		Claims claims = jwtUtil.getClaims(token);
		String email = claims.getSubject();
		String role = claims.get("role", String.class);
		Long userId = claims.get("userId", Long.class);
		
		var authority = new SimpleGrantedAuthority(role);
		var auth = new UsernamePasswordAuthenticationToken(email,null,Collections.singletonList(authority));
		
		auth.setDetails(userId);
		
		SecurityContextHolder.getContext().setAuthentication(auth);
		
		filterChain.doFilter(request, response);
	}
}
