package com.Anshul.FoodDeliveryApp.service;

import com.Anshul.FoodDeliveryApp.io.FoodRequest;
import com.Anshul.FoodDeliveryApp.io.FoodResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface FoodService {

	// add image to aws
	String uploadFile(MultipartFile file);

	// add food
	FoodResponse addFood(FoodRequest foodRequest, MultipartFile file);

	// get all foods
	List<FoodResponse> readFoods();

	// get Food
	FoodResponse readFood(String id);

	// delete File from the bucket
	Boolean deleteFile(String filename);

	// delete Food
	void deleteFood(String id);
}
