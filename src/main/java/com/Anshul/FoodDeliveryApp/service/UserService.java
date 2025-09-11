package com.Anshul.FoodDeliveryApp.service;

import com.Anshul.FoodDeliveryApp.io.UserRequest;
import com.Anshul.FoodDeliveryApp.io.UserResponse;

public interface UserService {

	// register user
	UserResponse registerUser(UserRequest request);
}
