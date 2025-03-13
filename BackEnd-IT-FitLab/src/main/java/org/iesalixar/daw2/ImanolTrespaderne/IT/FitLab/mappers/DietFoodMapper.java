package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.mappers;

import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.DietFoodDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.Diet;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.DietFood;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.Food;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums.DayOfTheWeek;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums.MealType;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories.DietRepository;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DietFoodMapper {

    @Autowired
    private DietRepository dietRepository;
    @Autowired
    private FoodRepository foodRepository;

    public DietFoodDTO toDTO(DietFood dietFood) {
        DietFoodDTO dto = new DietFoodDTO();
        dto.setDietId(dietFood.getDiet().getId());
        dto.setFoodId(dietFood.getFood().getId());
        dto.setDayWeek(dietFood.getDayWeek().name());
        dto.setMealType(dietFood.getMealType().name());
        dto.setQuantity(dietFood.getQuantity());
        return dto;
    }

    public DietFood toEntity(DietFoodDTO dto) {

        Diet diet = dietRepository.findById(dto.getDietId())
                .orElseThrow(() -> new RuntimeException("Dieta no encontrada"));

        Food food = foodRepository.findById(dto.getFoodId())
                .orElseThrow(() -> new RuntimeException("Alimento no encontrado"));

        DietFood dietFood = new DietFood();
        dietFood.setDiet(diet);
        dietFood.setFood(food);
        dietFood.setDayWeek(DayOfTheWeek.valueOf(dto.getDayWeek().toUpperCase()));
        dietFood.setMealType(MealType.valueOf(dto.getMealType().toUpperCase()));
        dietFood.setQuantity(dto.getQuantity());

        return dietFood;
    }
}
