package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.services;

import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.config.SecurityConfig;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.FoodDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.Food;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.mappers.FoodMapper;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories.FoodRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodService {
    private static final Logger logger = LoggerFactory.getLogger(FoodService.class);

    @Autowired
    private  FoodRepository foodRepository;
    @Autowired
    private  FoodMapper foodMapper;

    public List<FoodDTO> getAllFoods() {
        return foodRepository.findAll().stream()
                .map(foodMapper::toDTO)
                .collect(Collectors.toList());
    }

    public FoodDTO getFoodById(Long id) {
        return foodRepository.findById(id)
                .map(foodMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Food not found"));
    }

    public FoodDTO createFood(FoodDTO dto) {
        Food food = foodMapper.toEntity(dto);
        return foodMapper.toDTO(foodRepository.save(food));
    }

    public void deleteFood(Long id) {
        foodRepository.deleteById(id);
    }
}
