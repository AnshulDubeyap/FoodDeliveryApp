package com.Anshul.FoodDeliveryApp.service;

import org.springframework.security.core.Authentication;

public interface AuthenticationFacade {
	Authentication getAuthentication();
}
