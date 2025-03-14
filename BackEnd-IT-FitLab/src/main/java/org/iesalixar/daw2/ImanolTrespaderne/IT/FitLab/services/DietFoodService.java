package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.services;

import jakarta.transaction.Transactional;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.DietFoodDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.DietFood;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.DietFoodPK;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums.DayOfTheWeek;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums.MealType;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.mappers.DietFoodMapper;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories.DietFoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DietFoodService {

    @Autowired
    private DietFoodRepository dietFoodRepository;

    @Autowired
    private DietFoodMapper dietFoodMapper;

    /**
     * Añade un alimento a una dieta con la cantidad, día de la semana y tipo de comida especificados.
     */
    @Transactional
    public DietFoodDTO addFoodToDiet(DietFoodDTO dto) {
        DietFood dietFood = dietFoodMapper.toEntity(dto);
        return dietFoodMapper.toDTO(dietFoodRepository.save(dietFood));
    }

    /**
     * Elimina un alimento de una dieta para un día y tipo de comida específicos.
     */
    @Transactional
    public void removeFoodFromDiet(DietFoodDTO dto) {
        DietFoodPK id = new DietFoodPK(dto.getDietId(),dto.getFoodId() , dto.getDayWeek(), dto.getMealType());

        Optional<DietFood> dietFood = dietFoodRepository.findById(id);

        if (dietFood.isPresent()) {
            System.out.println(dietFood.get().getId());
            dietFoodRepository.deleteById(dietFood.get().getId());
        } else {
            throw new IllegalArgumentException("No se encontró la comida en la dieta para eliminarla.");
        }
    }


    /**
     * Obtiene todos los alimentos de una dieta específica.
     */
    public List<DietFoodDTO> getFoodsByDayOfTheWeek(Long dietId, DayOfTheWeek dayWeek) {
        return dietFoodRepository.findById_DietIdAndId_DayWeek(dietId, dayWeek)
                .stream()
                .map(dietFoodMapper::toDTO)
                .collect(Collectors.toList());
    }
}
