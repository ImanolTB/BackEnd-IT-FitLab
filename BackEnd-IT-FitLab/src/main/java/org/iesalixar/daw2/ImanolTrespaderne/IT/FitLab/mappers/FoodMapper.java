package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.mappers;

import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.FoodDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.Food;
import org.springframework.stereotype.Component;

@Component
public class FoodMapper {

    public FoodDTO toDTO(Food food) {
        FoodDTO dto = new FoodDTO();
        dto.setId(food.getId());
        dto.setName(food.getName());
        dto.setCalories(food.getCalories());
        dto.setProteins(food.getProteins());
        dto.setCarbohydrates(food.getCarbohydrates());
        dto.setFats(food.getFats());
        return dto;
    }

    public Food toEntity(FoodDTO dto) {
        Food food = new Food();
        food.setId(dto.getId());
        food.setName(dto.getName());
        food.setCalories(dto.getCalories());
        food.setProteins(dto.getProteins());
        food.setCarbohydrates(dto.getCarbohydrates());
        food.setFats(dto.getFats());
        return food;
    }
}
