package com.Anshul.FoodDeliveryApp.service;

import com.Anshul.FoodDeliveryApp.entity.UserEntity;
import com.Anshul.FoodDeliveryApp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

// We created this class to implement UserDetailsService interface
// This class is used to load user-specific data
// It is used by the authentication manager to load the user-specific data



@Service
@AllArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

	@Autowired
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		UserEntity user = userRepository.findByEmail(email)
		.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		return new User(user.getEmail(), user.getPassword(), Collections.emptyList());
	}
}
