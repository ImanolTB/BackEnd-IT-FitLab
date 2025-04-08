package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.services;

import jakarta.transaction.Transactional;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.DietFoodDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.Diet;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.DietFood;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.DietFoodPK;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.Food;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums.DayOfTheWeek;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums.MealType;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.mappers.DietFoodMapper;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories.DietFoodRepository;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories.DietRepository;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DietFoodService {

    @Autowired
    private DietFoodRepository dietFoodRepository;

    @Autowired
    private DietFoodMapper dietFoodMapper;

    @Autowired
    private DietRepository dietRepository;

    @Autowired
    private FoodRepository foodRepository;
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

    @Transactional
    public List<DietFoodDTO> replaceFoodsForDayAndType(Long dietId, DayOfTheWeek day, MealType type, List<DietFoodDTO> nuevosAlimentos) {
        // Validaciones básicas
        if (dietId == null || day == null || type == null) {
            throw new IllegalArgumentException("Día, tipo de comida y dieta no pueden ser nulos.");
        }

        // Validación de cantidades y alimentos
        for (DietFoodDTO dto : nuevosAlimentos) {
            if (dto.getFoodId() == null || dto.getQuantity() == null || dto.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Los alimentos deben tener foodId y una cantidad válida mayor que 0.");
            }
        }

        // Cargar la dieta
        Diet diet = dietRepository.findById(dietId)
                .orElseThrow(() -> new IllegalArgumentException("Dieta no encontrada con ID: " + dietId));

        // Eliminar existentes
        dietFoodRepository.deleteByDietIdAndDayWeekAndMealType(dietId, day, type);

        // Convertir y guardar nuevos
        List<DietFood> nuevasEntidades = nuevosAlimentos.stream().map(dto -> {
            Food food = foodRepository.findById(dto.getFoodId())
                    .orElseThrow(() -> new IllegalArgumentException("Alimento no encontrado con ID: " + dto.getFoodId()));

            DietFoodPK pk = new DietFoodPK(dietId, dto.getFoodId(), day, type);

            DietFood entity = new DietFood();
            entity.setId(pk);
            entity.setDiet(diet);
            entity.setFood(food);
            entity.setQuantity(dto.getQuantity());

            return entity;
        }).toList();

        return dietFoodRepository.saveAll(nuevasEntidades).stream()
                .map(dietFoodMapper::toDTO)
                .toList();
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
