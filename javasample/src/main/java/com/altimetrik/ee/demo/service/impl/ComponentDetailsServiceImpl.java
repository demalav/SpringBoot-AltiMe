package com.altimetrik.ee.demo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.altimetrik.ee.demo.model.FoodDto;
import com.altimetrik.ee.demo.entity.FoodEntity;
import com.altimetrik.ee.demo.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.altimetrik.ee.demo.bean.PairedComponentDetailsBean;
import com.altimetrik.ee.demo.entity.ComponentDetailsEntity;
import com.altimetrik.ee.demo.repository.ComponentDetailsRepository;
import com.altimetrik.ee.demo.service.ComponentDetailsService;
import sun.plugin.com.Utils;

@Service
public class ComponentDetailsServiceImpl implements ComponentDetailsService {

    protected static Logger logger = LoggerFactory.getLogger(ComponentDetailsServiceImpl.class.getName());

    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    private ComponentDetailsRepository componentDetailsRepository;

    @Autowired
    FoodRepository foodRepository;

    @Autowired
    Utils utils;

    @Override
    public PairedComponentDetailsBean findAll(final String applicationName) {
        final PairedComponentDetailsBean pairedComponentDetails = new PairedComponentDetailsBean(
                this.componentDetailsRepository.getByComponentName(applicationName),
                this.componentDetailsRepository.getByComponentNameNotIn(applicationName));
        return pairedComponentDetails;
    }

    @Override
    public boolean createComponentDetails(final String applicationName) {
        if (this.componentDetailsRepository.findByComponentName(applicationName) == null) {
            this.componentDetailsRepository.save(new ComponentDetailsEntity(applicationName, UUID.randomUUID().toString()));
        }
        return true;
    }

    public List<String> getDestination(String origin) {
        List<String> h = componentDetailsRepository.getDestination(origin);
        return h;
    }


    @Override
    public FoodDto createFood(FoodDto food) {
        ModelMapper modelMapper = new ModelMapper();
        FoodEntity foodEntity = modelMapper.map(food, FoodEntity.class);
        String publicFoodId = utils.generateFoodId(30);
        foodEntity.setFoodId(publicFoodId);
        FoodEntity storedFood = foodRepository.save(foodEntity);
        FoodDto foodDto = new FoodDto();
        foodDto = modelMapper.map(storedFood, FoodDto.class);
        return foodDto;
    }

    @Override
    public FoodDto getFoodById(String foodId){
        FoodDto returnValue = new FoodDto();
        ModelMapper modelMapper = new ModelMapper();
        FoodEntity foodEntity = foodRepository.findByFoodId(foodId);
        if (foodEntity == null) {
            System.out.println(foodId +"Not found");
        }
        returnValue = modelMapper.map(foodEntity, FoodDto.class);
        return returnValue;
    }

    @Override
    public FoodDto updateFoodDetails(String foodId, FoodDto foodDetails){

        FoodDto returnValue = new FoodDto();
        ModelMapper modelMapper = new ModelMapper();
        FoodEntity foodEntity = foodRepository.findByFoodId(foodId);
        if (foodEntity == null) {
            System.out.println(foodId +"Not found");
        }
        foodEntity.setFoodCategory(foodDetails.getFoodCategory());
        foodEntity.setFoodName(foodDetails.getFoodName());
        foodEntity.setFoodPrice(foodDetails.getFoodPrice());
        FoodEntity updatedFoodEntity = foodRepository.save(foodEntity);
        returnValue = modelMapper.map(updatedFoodEntity, FoodDto.class);
        return returnValue;
    }

    @Override
    public void deleteFoodItem(String id)  {
        FoodEntity foodEntity = foodRepository.findByFoodId(id);
        if (foodEntity == null) {
            System.out.println(id +"Not found");
        }
        foodRepository.delete(foodEntity);
    }

    @Override
    public List<FoodDto> getFoods() {
        List<FoodDto> returnValue = new ArrayList<>();
        Iterable<FoodEntity> iterableObjects = foodRepository.findAll();
        for (FoodEntity foodEntity : iterableObjects) {
            FoodDto foodDto = new FoodDto();
            BeanUtils.copyProperties(foodEntity, foodDto);
            returnValue.add(foodDto);
        }
        return returnValue;
    }


    @Scheduled(cron = "${cron.component.identifier.reg-ex}")
    public void regenerateComponentIdentifier() {
        final ComponentDetailsEntity componentDetails = this.componentDetailsRepository.findByComponentName(this.applicationName);
        componentDetails.setComponentIdentifier(UUID.randomUUID().toString());
        this.componentDetailsRepository.save(componentDetails);
    }

}
