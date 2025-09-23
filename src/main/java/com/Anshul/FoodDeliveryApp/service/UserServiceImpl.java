package com.Anshul.FoodDeliveryApp.service;

import com.Anshul.FoodDeliveryApp.entity.UserEntity;
import com.Anshul.FoodDeliveryApp.io.UserRequest;
import com.Anshul.FoodDeliveryApp.io.UserResponse;
import com.Anshul.FoodDeliveryApp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepo;

	@Override
	public UserResponse registerUser(UserRequest request) {

		// convert userRequest to Entity
		UserEntity newUser = convertToEntity(request);

		// save userEntity to a database
		newUser = userRepo.save(newUser);

		// convert userEntity to userResponse
		return convertToResponse(newUser);
	}

	// convert userRequest to Entity
	private UserEntity convertToEntity(UserRequest request) {
			return UserEntity.builder().name(request.getName()).email(request.getEmail()).password(passwordEncoder.encode(request.getPassword())).build();
	}

	// convert userEntity to userResponse
	private UserResponse convertToResponse(UserEntity registeredUser) {
		return UserResponse.builder().name(registeredUser.getName()).email(registeredUser.getEmail()).id(registeredUser.getId()).build();
	}
}
