package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.services;

import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.DietFoodDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.DietFood;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums.DayOfTheWeek;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.mappers.DietFoodMapper;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories.DietFoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DietFoodService {

    @Autowired
    private  DietFoodRepository dietFoodRepository;

    @Autowired
    private  DietFoodMapper dietFoodMapper;



    /**
     * Añade un alimento a una dieta con la cantidad, día de la semana y tipo de comida especificados.
     */
    @Transactional
    public DietFoodDTO addFoodToDiet(DietFoodDTO dto) {
        // Convertir DTO a entidad utilizando el Mapper
        DietFood dietFood = dietFoodMapper.toEntity(dto);

        // Guardar en la base de datos y devolver como DTO
        return dietFoodMapper.toDTO(dietFoodRepository.save(dietFood));
    }

    /**
     * Elimina un alimento de una dieta para un día y tipo de comida específicos.
     */
    @Transactional
    public void removeFoodFromDiet(Long dietId, Long foodId, String dayWeek, String mealType) {
        dietFoodRepository.deleteByDietIdAndFoodIdAndDayWeekAndMealType(
                dietId,
                foodId,
                dayWeek.toUpperCase(),
                mealType.toUpperCase()
        );
    }

    /**
     * Obtiene todos los alimentos asociados a una dieta específica.
     */
    public List<DietFoodDTO> getFoodsByDayOfTheWeek(Long dietId, String dayWeek) {
        DayOfTheWeek dayEnum = DayOfTheWeek.valueOf(dayWeek.toUpperCase());

        return dietFoodRepository.findByDietIdAndDayWeek(dietId, dayEnum)
                .stream()
                .map(dietFoodMapper::toDTO)
                .collect(Collectors.toList());
    }
}
