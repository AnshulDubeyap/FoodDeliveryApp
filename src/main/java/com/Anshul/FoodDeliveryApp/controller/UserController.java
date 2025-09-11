package com.Anshul.FoodDeliveryApp.controller;

import com.Anshul.FoodDeliveryApp.io.UserRequest;
import com.Anshul.FoodDeliveryApp.io.UserResponse;
import com.Anshul.FoodDeliveryApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
	private UserService service;

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public UserResponse register(UserRequest request) {
		return service.registerUser(request);
	}


}
