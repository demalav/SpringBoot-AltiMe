package com.altimetrik.ee.demo.repository;

import com.altimetrik.ee.demo.entity.FoodEntity;
import com.food.delivery.app.ws.io.entity.FoodEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends CrudRepository<FoodEntity, Long> {

	FoodEntity findByFoodId(String foodId);
}
