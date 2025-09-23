package com.Anshul.FoodDeliveryApp.service;

import com.Anshul.FoodDeliveryApp.io.CartRequest;
import com.Anshul.FoodDeliveryApp.io.CartResponse;

public interface CartService {

	// add to cart
	public CartResponse addToCart(CartRequest request);

	// get cart
	CartResponse getCart();

	// clear cart
	void clearCart();

	// remove from cart
	 CartResponse removeFromCart(CartRequest request);
}
