package com.ecobazzar.eco.bazzar.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
		.csrf(csrf -> csrf.disable())
		.authorizeHttpRequests(auth -> auth
				.requestMatchers("/api/auth/resister" , "/api/auth/login").permitAll()
				.anyRequest().authenticated()
				)
		.httpBasic();
		
		return http.build();
	}
	



@Bean
public InMemoryUserDetailsManager userDetailsService() {
	UserDetails admin = User.withUsername("admin")
			.password("{noop}admin123")
			.roles("ADMIN")
			.build();
	return new InMemoryUserDetailsManager(admin);
}
}
