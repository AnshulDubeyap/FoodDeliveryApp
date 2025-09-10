package com.Anshul.FoodDeliveryApp.service;

import com.Anshul.FoodDeliveryApp.entity.FoodEntity;
import com.Anshul.FoodDeliveryApp.io.FoodRequest;
import com.Anshul.FoodDeliveryApp.io.FoodResponse;
import com.Anshul.FoodDeliveryApp.repository.FoodRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FoodServiceImpl implements FoodService {


	// autowire food repository
	@Autowired
	private FoodRepository foodRepo;

	@Override
	public String uploadFile(MultipartFile file) {
		return "url";
	}

	// add food
	@Override
	public FoodResponse addFood(FoodRequest request, MultipartFile file) {

		// convert foodRequest to foodEntity
		FoodEntity newfoodEntity = convertToFoodEntity(request);

		// add imageUrl to foodEntity
		String imageUrl = uploadFile(file);
		newfoodEntity.setImageUrl(imageUrl);

		// save foodEntity to a database
		FoodEntity savedFoodEntity = foodRepo.save(newfoodEntity);

		// convert foodEntity to foodResponse and return it
		return convertToFoodResponse(savedFoodEntity);
	}

	@Override
	public List<FoodResponse> readFoods() {

		// get all foods
		List<FoodEntity> databaseEntities = foodRepo.findAll();

		// convert into FoodResponse
		return databaseEntities.stream().map(object -> convertToFoodResponse(object)).collect(Collectors.toList());
	}

	@Override
	public FoodResponse readFood(String id) {
		// get the food From the database
		FoodEntity food = foodRepo.findById(id).orElseThrow(()-> new RuntimeException("Food not found with id: " + id));

		// Convert FoodEntity to FoodResponse
		return convertToFoodResponse(food);
	}

	@Override
	public Boolean deleteFile(String filename) {
		return null;
	}

	@Override
	public void deleteFood(String id) {
		// get the food
		FoodResponse response = readFood(id);

		// get the url
		String imageurl = response.getImageUrl();

		// get the filename from the image url

		// delete the image from aws

		// if deleted successfully, then only,

		foodRepo.deleteById(response.getId());
	}

	// Convert FoodRequest to FoodEntity
	private FoodEntity convertToFoodEntity(FoodRequest request) {
		return FoodEntity.builder()
				.name(request.getName())
				.description(request.getDescription())
				.category(request.getCategory())
				.price(request.getPrice())
				.build();
	}

	// Convert FoodEntity to FoodResponse
	private FoodResponse convertToFoodResponse(FoodEntity entity) {
		return FoodResponse.builder()
				.id(entity.getId())
				.name(entity.getName())
				.description(entity.getDescription())
				.category(entity.getCategory())
				.price(entity.getPrice())
				.imageUrl(entity.getImageUrl())
				.build();
	}
}
