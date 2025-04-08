package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.controllers;

import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.DietFoodDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums.DayOfTheWeek;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums.MealType;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.services.DietFoodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dietfood")
public class DietFoodController {

    private static final Logger logger = LoggerFactory.getLogger(DietFoodController.class);

    @Autowired
    private  DietFoodService dietFoodService;


    /**
     * Añadir un alimento a una dieta en un día y comida específicos.
     */
    @PostMapping
    public ResponseEntity<?> addFoodToDiet(@RequestBody DietFoodDTO dto) {
        logger.info("Intentando añadir alimento a la dieta: {}", dto);
        try {
            DietFoodDTO savedDietFood = dietFoodService.addFoodToDiet(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedDietFood);
        } catch (IllegalArgumentException e) {
            logger.warn("Datos inválidos al añadir alimento a la dieta: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al añadir alimento a la dieta: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al añadir alimento.");
        }
    }
    @PutMapping("/{dietId}/{dayWeek}/{mealType}")
    public ResponseEntity<?> replaceFoodsForDayAndType(@PathVariable Long dietId,
                                                       @PathVariable DayOfTheWeek dayWeek,
                                                       @PathVariable MealType mealType,
                                                       @RequestBody List<DietFoodDTO> nuevosAlimentos) {
        logger.info("Reemplazando alimentos en dieta {}, día {} y tipo {}", dietId, dayWeek, mealType);

        if (nuevosAlimentos == null) {
            return ResponseEntity.badRequest().body("Lista de alimentos no proporcionada.");
        }

        for (DietFoodDTO dto : nuevosAlimentos) {
            if (!dto.getDietId().equals(dietId) || dto.getDayWeek() != dayWeek || dto.getMealType() != mealType) {
                return ResponseEntity.badRequest().body("Todos los alimentos deben coincidir con los parámetros de la URL.");
            }
        }

        try {
            List<DietFoodDTO> actualizados = dietFoodService.replaceFoodsForDayAndType(dietId, dayWeek, mealType, nuevosAlimentos);
            return ResponseEntity.ok(actualizados);
        } catch (Exception e) {
            logger.error("Error al reemplazar alimentos", e); // <-- Aquí va el stacktrace completo
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar alimentos.");
        }
    }
    /**
     * Eliminar un alimento de una dieta para un día y tipo de comida específicos.
     */
    @DeleteMapping("/{dietId}/{foodId}")
    public ResponseEntity<?> removeFoodFromDiet(@RequestBody DietFoodDTO dto) {
        logger.info("Intentando eliminar alimento {} de la dieta {} para {} - {}", dto.getFoodId(), dto.getDietId(), dto.getDayWeek(), dto.getMealType());
        try {
            dietFoodService.removeFoodFromDiet(dto);
            return ResponseEntity.ok("Alimento eliminado con éxito de la dieta.");
        } catch (IllegalArgumentException e) {
            logger.warn("Datos no válidos al eliminar alimento de la dieta: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al eliminar alimento de la dieta: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al eliminar alimento.");
        }
    }

    /**
     * Obtener alimentos de una dieta en un día específico.
     */
    @GetMapping("/{dietId}/{dayWeek}")
    public ResponseEntity<?> getFoodsByDayOfTheWeek(@PathVariable Long dietId,
                                                    @PathVariable String dayWeek) {
        logger.info("Buscando alimentos en la dieta {} para el día {}", dietId, dayWeek);
        try {
            DayOfTheWeek dayOfTheWeek = DayOfTheWeek.fromString(dayWeek);
            List<DietFoodDTO> foods = dietFoodService.getFoodsByDayOfTheWeek(dietId,dayOfTheWeek);

            if (foods.isEmpty()) {
                logger.warn("No hay alimentos registrados en la dieta {} para el día {}", dietId, dayWeek);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron alimentos para ese día en la dieta.");
            }
            return ResponseEntity.ok(foods);
        } catch (IllegalArgumentException e) {
            logger.warn("Día de la semana inválido: {}", dayWeek);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Día de la semana no válido: " + dayWeek);
        } catch (Exception e) {
            logger.error("Error inesperado al obtener alimentos para el día {}: {}", dayWeek, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al obtener alimentos.");
        }
    }
}
