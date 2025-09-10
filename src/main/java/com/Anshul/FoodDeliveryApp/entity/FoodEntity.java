package com.Anshul.FoodDeliveryApp.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "foods")
@Builder // Builder method helps create objects step by step in a clean and readable way, (FoodEntity to FoodResponse, or, FoodRequest to FoodEntity)
public class FoodEntity {

	@Id
	private String id;
	private String name;
	private String description;
	private double price;
	private String category;
	private String imageUrl;

}
