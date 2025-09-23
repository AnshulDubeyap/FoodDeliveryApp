package com.Anshul.FoodDeliveryApp.service;

import com.Anshul.FoodDeliveryApp.entity.CartEntity;
import com.Anshul.FoodDeliveryApp.io.CartRequest;
import com.Anshul.FoodDeliveryApp.io.CartResponse;
import com.Anshul.FoodDeliveryApp.repository.CartRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {



	private final CartRepository cartRepo;

	// to get current user id
	private final UserService userService;

	@Override
	public CartResponse addToCart(CartRequest request) {
		String loggedInUserId = userService.findByUserId();

		// first check if the cart exists for the user
		Optional<CartEntity> cartOptional = cartRepo.findByUserId(loggedInUserId);

		// if not exists, create a new cart
		CartEntity entity = cartOptional.orElseGet(()-> new CartEntity(loggedInUserId, new HashMap<>()));

		// if a cart already exists, there are items in the cart
		Map<String, Integer> cartItems = entity.getItems();

		// if the food is already in the cart, increase the quantity
		cartItems.put(request.getFoodId(), cartItems.getOrDefault(request.getFoodId(), 0) + 1);

		// update the cart
		entity.setItems(cartItems);

		// save the cart
		CartEntity savedCart = cartRepo.save(entity);

		// convert cartEntity to cartResponse
		return convertToCartResponse(savedCart);

	}

	@Override
	public CartResponse getCart() {
		// get current user id
		String loggedInUserId = userService.findByUserId();

		// get cart by user id
		CartEntity entity = cartRepo.findByUserId(loggedInUserId)
				.orElse(new CartEntity(null, loggedInUserId, new HashMap<>()));

		return convertToCartResponse(entity);
	}

	@Override
	public void clearCart() {
		// get current user id
		String loggedInUserId = userService.findByUserId();

		// delete cart by user id
		cartRepo.deleteByUserId(loggedInUserId);

	}

	@Override
	public CartResponse removeFromCart(CartRequest request) {
		// get current user id
		String loggedInUserId = userService.findByUserId();

		// get the existing cart
		CartEntity entity = cartRepo.findByUserId(loggedInUserId)
				.orElseThrow(() -> new RuntimeException("Cart not found for user"));

		// get the cart items
		Map<String, Integer> items = entity.getItems();

		// check if the food is in the cart
		if (items.containsKey(request.getFoodId())) {
			int currentQty = items.get(request.getFoodId());

			if (currentQty > 1) {
				// decrement and update
				items.put(request.getFoodId(), currentQty - 1);
			} else {
				// remove item if qty becomes 0
				items.remove(request.getFoodId());
			}

			// update the cart
			entity = cartRepo.save(entity);
		}

		// convert cartEntity to cartResponse
		return convertToCartResponse(entity);
	}


	private CartResponse convertToCartResponse(CartEntity entity) {
		return CartResponse.builder()
				.id(entity.getId())
				.userId(entity.getUserId())
				.items(entity.getItems())
				.build();
	}

}
