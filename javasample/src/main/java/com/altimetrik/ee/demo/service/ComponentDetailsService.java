package com.altimetrik.ee.demo.service;

import com.altimetrik.ee.demo.model.FoodDto;
import org.springframework.stereotype.Service;

import com.altimetrik.ee.demo.bean.PairedComponentDetailsBean;

import java.util.List;

@Service
public interface ComponentDetailsService {

    boolean createComponentDetails(final String applicationName);

    PairedComponentDetailsBean findAll(final String applicationName);

    List<String> getDestination(String origin);

    FoodDto createFood(FoodDto food);

    FoodDto getFoodById(String foodId);

    FoodDto updateFoodDetails(String foodId, FoodDto foodDetails);

    void deleteFoodItem(String id);

    List<FoodDto> getFoods();

}
