package com.Anshul.FoodDeliveryApp.repository;

import com.Anshul.FoodDeliveryApp.entity.CartEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends MongoRepository<CartEntity, String> {

	// get cart by user id
	Optional<CartEntity> findByUserId(String userId);

	// delete cart by user id
	void deleteByUserId(String userId);
}
