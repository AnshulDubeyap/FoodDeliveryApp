package com.Anshul.FoodDeliveryApp.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
public class OrderRequest {
	private List<OrderItem> orderedItems;
	private double amount;
	private String userAddress;
	private String email;
	private String phoneNumber;
	private String orderStatus;
}
