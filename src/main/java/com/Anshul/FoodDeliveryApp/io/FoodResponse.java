package com.Anshul.FoodDeliveryApp.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodResponse {
	private String id;
	private String imageUrl;
	private String name;
	private String description;
	private double price;
	private String category;
}
