package com.Anshul.FoodDeliveryApp.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JWTUtil {

	private final String SECRET_KEY;

	// Default constructor to generate a secure Base64-encoded key
	public JWTUtil() {
		SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
		SECRET_KEY = Base64.getEncoder().encodeToString(key.getEncoded());
		System.out.println("Generated JWT Secret Key: " + SECRET_KEY);
	}

	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userDetails.getUsername());
	}

	private String createToken(Map<String, Object> claims, String subject) {
		byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
		if (keyBytes.length < 32) {
			throw new IllegalArgumentException("JWT secret key must be at least 256 bits (32 bytes) long");
		}
		SecretKey key = Keys.hmacShaKeyFor(keyBytes);
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
		SecretKey key = Keys.hmacShaKeyFor(keyBytes);
		return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
	}

	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}