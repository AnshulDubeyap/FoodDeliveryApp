package com.Anshul.FoodDeliveryApp.service;

import com.Anshul.FoodDeliveryApp.entity.FoodEntity;
import com.Anshul.FoodDeliveryApp.io.FoodRequest;
import com.Anshul.FoodDeliveryApp.io.FoodResponse;
import com.Anshul.FoodDeliveryApp.repository.FoodRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class FoodServiceImpl implements FoodService {


	// autowire food repository
	@Autowired
	private FoodRepository foodRepo;

	@Autowired
	private S3Client s3Client;

	@Value( "${aws.s3.bucketname}")
	private String bucketName;

	@Override
	public String uploadFile(MultipartFile file) {

		// get the file extension
		String filenameExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);

		// generate unique
		String key = UUID.randomUUID().toString()+"."+filenameExtension;

		try{
			PutObjectRequest putObjectRequest = PutObjectRequest.builder()
					.bucket(bucketName)
					.key(key)
					.acl("public-read")
					.contentType(file.getContentType())
					.build();

			PutObjectResponse response = s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

			if(response.sdkHttpResponse().isSuccessful()){
				return "https://"+bucketName+".s3.amazonaws.com/"+key;
			}else{
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "File upload failed");
			}
		}catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while uploading file to S3 bucket");
		}
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
		DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
				.bucket(bucketName)
				.key(filename)
				.build();

		s3Client.deleteObject(deleteObjectRequest);
		return true;
	}

	@Override
	public void deleteFood(String id) {
		// get the food
		FoodResponse response = readFood(id);

		// get the url
		String imageurl = response.getImageUrl();

		// get the filename from the image url
		String filename = imageurl.substring(imageurl.lastIndexOf("/") + 1);

		// delete the image from aws
		Boolean isDeleted = deleteFile(filename);

		// if deleted successfully, then only delete the food from the database
		if(
				isDeleted
		){
			foodRepo.deleteById(response.getId());
		}
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
