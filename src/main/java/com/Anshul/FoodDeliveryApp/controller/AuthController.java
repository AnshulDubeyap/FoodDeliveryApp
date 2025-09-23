package com.Anshul.FoodDeliveryApp.controller;

import com.Anshul.FoodDeliveryApp.io.AuthenticationRequest;
import com.Anshul.FoodDeliveryApp.io.AuthenticationResponse;
import com.Anshul.FoodDeliveryApp.service.AppUserDetailsService;
import com.Anshul.FoodDeliveryApp.util.JWTUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class AuthController {


	private final AuthenticationManager authenticationManager;


	private final JWTUtil jwtUtil;


	private final AppUserDetailsService userDetailsService;

	@PostMapping("/login")
	public AuthenticationResponse login(@RequestBody AuthenticationRequest request) {

		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						request.getEmail(),
						request.getPassword()
				)
		);

		final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

		final String jwt = jwtUtil.generateToken(userDetails);

		return new AuthenticationResponse(jwt, request.getEmail());
	}
}
