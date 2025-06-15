package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.services;

import jakarta.validation.Valid;
import lombok.Data;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.FoodDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.Food;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums.DayOfTheWeek;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.mappers.FoodMapper;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories.FoodRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodService {

    // Logger para registrar información relevante y errores
    private static final Logger logger = LoggerFactory.getLogger(FoodService.class);

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private FoodMapper foodMapper;

    /**
     * Devuelve una lista de todas las comidas almacenadas en la base de datos.
     */
    public List<FoodDTO> getAllFoods() {
        try {
            logger.info("Recuperando todas las comidas.");
            return foodRepository.findAll().stream()
                    .map(foodMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error al obtener todas las comidas: {}", e.getMessage());
            throw new RuntimeException("Error al obtener las comidas.");
        }
    }

    /**
     * Devuelve una comida específica a partir de su ID.
     * Si no se encuentra, lanza una excepción.
     */
    public FoodDTO getFoodById(@PathVariable Long id) {
        try {
            logger.info("Buscando comida con ID: {}", id);
            return foodRepository.findById(id)
                    .map(foodMapper::toDTO)
                    .orElseThrow(() -> {
                        logger.warn("Comida con ID {} no encontrada.", id);
                        return new IllegalArgumentException("Comida no encontrada");
                    });
        } catch (Exception e) {
            logger.error("Error al obtener la comida con ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al obtener la comida.");
        }
    }

    /**
     * Devuelve una lista de comidas asociadas a una dieta y día de la semana concretos.
     */
    public List<FoodDTO> getFoodsByDay(@PathVariable Long dietId, @PathVariable DayOfTheWeek dayOfWeek) {
        try {
            logger.info("Buscando comidas para la dieta {} y día {}.", dietId, dayOfWeek);
            List<Food> foods = foodRepository.findFoodsByDietAndDay(dietId, dayOfWeek);
            return foods.stream()
                    .map(foodMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error al obtener comidas para la dieta {} y día {}: {}", dietId, dayOfWeek, e.getMessage());
            throw new RuntimeException("Error al obtener las comidas por día.");
        }
    }

    /**
     * Crea una nueva comida a partir de los datos recibidos en un DTO.
     * Retorna el DTO de la comida guardada.
     */
    public FoodDTO createFood(@Valid @RequestBody FoodDTO dto) {
        try {
            logger.info("Creando nueva comida: {}", dto.getName());
            Food food = foodMapper.toEntity(dto);
            Food savedFood = foodRepository.save(food);
            logger.info("Comida creada con éxito, ID: {}", savedFood.getId());
            return foodMapper.toDTO(savedFood);
        } catch (Exception e) {
            logger.error("Error al crear la comida: {}", e.getMessage());
            throw new RuntimeException("Error al crear la comida.");
        }
    }

    /**
     * Actualiza los datos de una comida existente, identificada por su ID.
     * Si no se encuentra, lanza una excepción.
     */
    public FoodDTO updateFood(@PathVariable Long id, @Valid @RequestBody FoodDTO dto) {
        try {
            logger.info("Actualizando comida con ID: {}", id);
            Food food = foodRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("Comida con ID {} no encontrada.", id);
                        return new IllegalArgumentException("Comida no encontrada");
                    });

            food.setName(dto.getName());
            food.setCalories(dto.getCalories());
            food.setProteins(dto.getProteins());
            food.setCarbohydrates(dto.getCarbohydrates());
            food.setFats(dto.getFats());

            Food updatedFood = foodRepository.save(food);
            logger.info("Comida con ID {} actualizada con éxito.", id);
            return foodMapper.toDTO(updatedFood);
        } catch (Exception e) {
            logger.error("Error al actualizar la comida con ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al actualizar la comida.");
        }
    }

    /**
     * Elimina una comida por su ID.
     * Si no se encuentra, lanza una excepción.
     */
    public void deleteFood(@PathVariable Long id) {
        try {
            logger.info("Eliminando comida con ID: {}", id);
            if (!foodRepository.existsById(id)) {
                logger.warn("Comida con ID {} no encontrada para eliminación.", id);
                throw new IllegalArgumentException("Comida no encontrada");
            }
            foodRepository.deleteById(id);
            logger.info("Comida con ID {} eliminada con éxito.", id);
        } catch (Exception e) {
            logger.error("Error al eliminar la comida con ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al eliminar la comida.");
        }
    }
}
