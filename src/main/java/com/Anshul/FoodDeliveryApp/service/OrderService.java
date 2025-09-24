package com.Anshul.FoodDeliveryApp.service;

import com.Anshul.FoodDeliveryApp.io.OrderRequest;
import com.Anshul.FoodDeliveryApp.io.OrderResponse;
import com.razorpay.RazorpayException;

import java.util.List;
import java.util.Map;

public interface OrderService {

	// create order with payment
	OrderResponse createOrderWithPayment(OrderRequest request) throws RazorpayException;

	// verify the payment
	void verifyPayment(Map<String, String> paymentData, String status) throws RazorpayException;

	// get the order list by userId
	List<OrderResponse> getUserOrders();

	// remove the order
	void removeOrder(String orderId);


	//============ for admin===========//
	List<OrderResponse> getAllOrders();

	//============ for admin===========//
	void updateOrderStatus(String orderId, String status);
}
