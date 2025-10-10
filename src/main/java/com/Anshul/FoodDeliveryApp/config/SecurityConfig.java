package com.Anshul.FoodDeliveryApp.config;

import com.Anshul.FoodDeliveryApp.filters.JWTAuthenticationFilter;
import com.Anshul.FoodDeliveryApp.service.AppUserDetailsService;
import com.Anshul.FoodDeliveryApp.util.JWTUtil;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

	// UserDetailsService
	private final AppUserDetailsService userDetailsService;

	// JWT Util
	private final JWTUtil jwtUtil;

	// JWT Authentication Filter
	private final JWTAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// Enable CORS
		http.cors(Customizer.withDefaults());

		// Disable CSRF
		http.csrf(AbstractHttpConfigurer::disable);

		// Disable form login
		http.formLogin(AbstractHttpConfigurer::disable);

		// Allow register, login, foods APIs, admin routes, and preflight OPTIONS requests
		http.authorizeHttpRequests(auth -> auth
				.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // allow preflight
				.requestMatchers("/api/register", "/api/login", "/api/foods/**").permitAll()
				.requestMatchers("/api/orders/all", "/api/orders/status/**").permitAll() // allow admin routes
				.anyRequest().authenticated()
		);

		// Stateless session management
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		// Add JWT Authentication Filter
		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	// Password Encoder
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// CORS Filter allowing all origins (fixes forbidden login from frontend)
	@Bean
	public CorsFilter corsFilter() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:5174"));
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
		configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return new CorsFilter(source);
	}

	// AuthenticationManager for JWT and login
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}
}