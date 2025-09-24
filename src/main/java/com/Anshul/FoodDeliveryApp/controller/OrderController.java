package com.Anshul.FoodDeliveryApp.controller;


import com.Anshul.FoodDeliveryApp.io.OrderRequest;
import com.Anshul.FoodDeliveryApp.io.OrderResponse;
import com.Anshul.FoodDeliveryApp.service.OrderService;
import com.razorpay.RazorpayException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@PostMapping("/create")
	@ResponseStatus(HttpStatus.CREATED)
	public OrderResponse createOrderWithPayment(@RequestBody OrderRequest request) {
		try {
			return orderService.createOrderWithPayment(request);
		} catch (RazorpayException e) {
			throw new RuntimeException(e);
		}
	}

	@PostMapping("/verify")
	public void verifyPayment(@RequestBody Map<String, String> paymentData) {
		try {
			orderService.verifyPayment(paymentData, "paid");
		} catch (RazorpayException e) {
			throw new RuntimeException(e);
		}
	}


	@GetMapping
	public List<OrderResponse> getOrders() {
		return orderService.getUserOrders();
	}

	@DeleteMapping("/{orderId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteOrder(@PathVariable String orderId) {
		orderService.removeOrder(orderId);
	}

	//============ for admin===========//

	@GetMapping("/all")
	public List<OrderResponse> getAllOrders() {
		return orderService.getAllOrders();
	}

	@PatchMapping("/status/{orderId}")
	public void updateOrderStatus(@PathVariable String orderId, @RequestParam String status ) {
		orderService.updateOrderStatus(orderId, status);
	}
}
