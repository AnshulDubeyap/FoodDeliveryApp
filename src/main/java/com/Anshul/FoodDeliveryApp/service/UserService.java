package com.Anshul.FoodDeliveryApp.service;

import com.Anshul.FoodDeliveryApp.io.UserRequest;
import com.Anshul.FoodDeliveryApp.io.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface UserService {




	// register user
	UserResponse registerUser(UserRequest request);
}
