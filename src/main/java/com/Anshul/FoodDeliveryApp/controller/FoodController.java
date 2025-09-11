package com.Anshul.FoodDeliveryApp.controller;

import com.Anshul.FoodDeliveryApp.io.FoodRequest;
import com.Anshul.FoodDeliveryApp.io.FoodResponse;
import com.Anshul.FoodDeliveryApp.service.FoodService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/foods")
@AllArgsConstructor
@CrossOrigin("*")
public class FoodController {

	// initialize the food service
	private final FoodService foodService;

	@PostMapping
	public FoodResponse addFood(@RequestPart("food") String foodString,
	                            @RequestPart("file") MultipartFile file) {

		ObjectMapper mapper = new ObjectMapper();
		FoodRequest request = null;
		try {
			request = mapper.readValue(foodString, FoodRequest.class);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (JsonProcessingException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid JSON formate");
		}

		return foodService.addFood(request, file);
	}


	// get foods
	@GetMapping
	public List<FoodResponse> readFoods() {
		return foodService.readFoods();
	}

	// get food
	@GetMapping("/{id}")
	public FoodResponse readFood(@PathVariable String id) {
		return foodService.readFood(id);
	}

	// delete food
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteFood(@PathVariable String id){
		foodService.deleteFood(id);
	}



}
