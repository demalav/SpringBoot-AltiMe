package com.altimetrik.ee.demo.controller;

import com.altimetrik.ee.demo.bean.*;
import com.altimetrik.ee.demo.model.FoodDetailsResponse;
import com.altimetrik.ee.demo.model.FoodDto;
import com.altimetrik.ee.demo.request.FoodDetailsRequestModel;
import com.altimetrik.ee.demo.request.OperationStatusModel;
import com.altimetrik.ee.demo.response.RequestOperationName;
import com.altimetrik.ee.demo.response.RequestOperationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.altimetrik.ee.demo.service.ComponentDetailsService;

import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/service")
public class ServiceController {

    protected static Logger logger = LoggerFactory.getLogger(ServiceController.class.getName());

    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    private ComponentDetailsService componentDetailsService;

    @GetMapping(value = "/")
    @ApiOperation(value = "Get service name and identifier", notes = "Get service details and its corresponding values for all paired services", response = PairedComponentDetailsBean.class)
    public PairedComponentDetailsBean findAll() {
        return componentDetailsService.findAll(this.applicationName);
    }

    @GetMapping("/{destination}")
    public List<String> getDestination(@PathVariable String origin) {
		List<String> list=  componentDetailsService.getDestination(origin);
		return list;
    }

	@GetMapping(path="/{id}")
	public FoodDetailsResponse getFood(@PathVariable String id) {
		FoodDetailsResponse returnValue = new FoodDetailsResponse();
		ModelMapper modelMapper = new ModelMapper();
		FoodDto foodDto = componentDetailsService.getFoodById(id);
		returnValue = modelMapper.map(foodDto, FoodDetailsResponse.class);
		return returnValue;
	}

	@PostMapping("/foodDetails")
	public FoodDetailsResponse createFood(@RequestBody FoodDetailsRequestModel foodDetails) {
		FoodDetailsResponse returnValue = new FoodDetailsResponse();
		ModelMapper modelMapper = new ModelMapper();
		FoodDto foodDto = modelMapper.map(foodDetails, FoodDto.class);
		FoodDto createFood = componentDetailsService.createFood(foodDto);
		returnValue = modelMapper.map(createFood, FoodDetailsResponse.class);
		return returnValue;
	}

	@PutMapping(path="/{id}")
	public FoodDetailsResponse updateFood(@PathVariable String id, @RequestBody FoodDetailsRequestModel foodDetails) {
		FoodDetailsResponse returnValue = new FoodDetailsResponse();
		ModelMapper modelMapper = new ModelMapper();
		FoodDto foodDto = new FoodDto();
		foodDto = modelMapper.map(foodDetails, FoodDto.class);
		FoodDto updatedUser = componentDetailsService.updateFoodDetails(id, foodDto);
		returnValue = modelMapper.map(updatedUser, FoodDetailsResponse.class);
		return returnValue;
	}

	@DeleteMapping(path = "/{id}")
	public OperationStatusModel deleteFood(@PathVariable String id) {
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.DELETE.name());
		componentDetailsService.deleteFoodItem(id);
		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return returnValue;
	}

	@GetMapping("/getFoods")
	public List<FoodDetailsResponse> getFoods() {
		List<FoodDetailsResponse> returnValue = new ArrayList<>();
		List<FoodDto> foods = componentDetailsService.getFoods();
		for(FoodDto foodDto: foods) {
			FoodDetailsResponse response = new FoodDetailsResponse();
			BeanUtils.copyProperties(foodDto, response);
			returnValue.add(response);
		}
		return returnValue;
	}

}
