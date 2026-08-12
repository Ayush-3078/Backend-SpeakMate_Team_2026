package com.rslsolution.speakmateai.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.rslsolution.speakmateai.security.AdminJwtAuthenticationFilter;
import com.rslsolution.speakmateai.security.JwtAuthenticationFilter;
import com.rslsolution.speakmateai.security.RequestLoggingFilter;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final RequestLoggingFilter requestLoggingFilter;
	private final AdminJwtAuthenticationFilter adminJwtAuthenticationFilter;

	public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, RequestLoggingFilter requestLoggingFilter,
			AdminJwtAuthenticationFilter adminJwtAuthenticationFilter) {
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
		this.requestLoggingFilter = requestLoggingFilter;
		this.adminJwtAuthenticationFilter = adminJwtAuthenticationFilter;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
				.cors(Customizer.withDefaults())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
						// Admin Auth
						.requestMatchers("/api/auth/admin/register", "/api/auth/admin/login",
								"/api/auth/admin/forgot-password", "/api/auth/admin/verify-otp",
								"/api/auth/admin/reset-password", "/api/auth/admin/refresh-token")
						.permitAll()
						.requestMatchers("/api/admin/**").hasAnyAuthority("ROLE_SUPER_ADMIN", "ROLE_ADMIN")
						
						// School Portal Auth and Features
						.requestMatchers("/api/auth/**").permitAll()
						.requestMatchers("/api/school/**").hasAnyAuthority("ROLE_SCHOOL_ADMIN", "ROLE_TEACHER")

						// Auth endpoints for Students
						.requestMatchers(
								"/api/users/register", "/api/users/login",
								"/api/users/google-login", "/api/users/send-registration-otp",
								"/api/users/send-delete-account-otp", "/api/users/delete-account",
								"/api/users/forgot-password", "/api/users/verify-otp",
								"/api/users/reset-password", "/api/users/reset-redirect",
								"/api/users/register-expo-url", "/error")
						.permitAll()

						// Lesson read endpoints — public browse (progress/start/complete require JWT)
						.requestMatchers(HttpMethod.GET,
								"/api/lessons", "/api/lessons/categories",
								"/api/lessons/search", "/api/lessons/recommended",
								"/api/lessons/*",
								"/api/lesson/get-all-lessons", "/api/lesson/get-active-lessons",
								"/api/lesson/get-lesson/*")
						.permitAll()
						
						// Student app features (also accessible by Teachers)
						.requestMatchers("/api/users/**", "/api/lessons/**", "/api/lesson/**", "/api/speech/**", 
								"/api/speaking/**", "/api/progress/**", "/api/achievement/**", 
								"/api/vocabulary/**", "/api/grammar/**", "/api/chat/**", "/api/chat-legacy/**", 
								"/api/ai/**", "/api/settings/**", "/api/onboarding/**", "/api/profile/**", 
								"/api/notification/**", "/api/user/**")
						.hasAnyAuthority("ROLE_USER", "ROLE_TEACHER")
						
						.anyRequest().authenticated())
				.httpBasic(Customizer.withDefaults());

		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		http.addFilterBefore(adminJwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		http.addFilterBefore(requestLoggingFilter, JwtAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOriginPatterns(List.of("*"));
		configuration.setAllowedMethods(List.of("*"));
		configuration.setAllowedHeaders(List.of("*"));
		configuration.setExposedHeaders(List.of("Authorization"));
		configuration.setAllowCredentials(false);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}
