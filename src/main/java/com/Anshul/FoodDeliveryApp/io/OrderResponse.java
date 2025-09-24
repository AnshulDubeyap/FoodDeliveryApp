package com.Anshul.FoodDeliveryApp.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
public class OrderResponse {

	private String id;

	private String userId;

	private String userAddress;

	private String phoneNumber;

	private String email;

	private double amount;

	private String paymentStatus;

	private String razorpayOrderId;

	private List<OrderItem> orderedItems;

	private String orderStatus;
}
