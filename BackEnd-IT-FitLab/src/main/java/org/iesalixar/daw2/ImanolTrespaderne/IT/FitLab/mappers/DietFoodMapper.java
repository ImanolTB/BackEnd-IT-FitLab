package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.mappers;

import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.DietFoodDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.Diet;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.DietFood;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.DietFoodPK;
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
        dto.setFoodName(dietFood.getFood().getName());

        dto.setDayWeek(dietFood.getId().getDayWeek());
        dto.setMealType(dietFood.getId().getMealType());
        dto.setQuantity(dietFood.getQuantity());
        return dto;
    }


    /**
     * Convierte un `DietFoodDTO` a una entidad `DietFood`
     */
    public DietFood toEntity(DietFoodDTO dto) {
        Diet diet = dietRepository.findById(dto.getDietId())
                .orElseThrow(() -> new RuntimeException("Error: Dieta no encontrada con ID " + dto.getDietId()));

        Food food = foodRepository.findById(dto.getFoodId())
                .orElseThrow(() -> new RuntimeException("Error: Alimento no encontrado con ID " + dto.getFoodId()));
        DietFoodPK dietFoodPK= new DietFoodPK(diet.getId(), food.getId(), dto.getDayWeek(), dto.getMealType());
        DietFood dietFood = new DietFood();
        dietFood.setId(dietFoodPK);
        dietFood.setFood(food);
        dietFood.setDiet(diet);
        dietFood.setQuantity(dto.getQuantity());

        return dietFood;
    }
}