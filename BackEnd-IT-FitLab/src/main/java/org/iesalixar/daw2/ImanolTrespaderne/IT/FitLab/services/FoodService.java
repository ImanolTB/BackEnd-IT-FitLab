package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.services;

import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.FoodDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.Food;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.mappers.FoodMapper;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories.FoodRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodService {

    private final FoodRepository foodRepository;
    private final FoodMapper foodMapper;

    public FoodService(FoodRepository foodRepository, FoodMapper foodMapper) {
        this.foodRepository = foodRepository;
        this.foodMapper = foodMapper;
    }

    public List<FoodDTO> getAllFoods() {
        return foodRepository.findAll().stream()
                .map(foodMapper::toDTO)
                .collect(Collectors.toList());
    }

    public FoodDTO getFoodById(Long id) {
        return foodRepository.findById(id)
                .map(foodMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Comida no encontrada"));
    }

    public FoodDTO createFood(FoodDTO dto) {
        Food food = foodMapper.toEntity(dto);
        return foodMapper.toDTO(foodRepository.save(food));
    }

    public FoodDTO updateFood(Long id, FoodDTO dto) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comida no encontrada"));

        food.setName(dto.getName());
        food.setCalories(dto.getCalories());
        food.setProteins(dto.getProteins());
        food.setCarbohydrates(dto.getCarbohydrates());
        food.setFats(dto.getFats());

        return foodMapper.toDTO(foodRepository.save(food));
    }

    public void deleteFood(Long id) {
        if (!foodRepository.existsById(id)) {
            throw new IllegalArgumentException("Comida no encontrada");
        }
        foodRepository.deleteById(id);
    }
}
