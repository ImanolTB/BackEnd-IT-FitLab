package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/v1/dietfood")
@Tag(name = "DietFood", description = "Gestión de alimentos en dietas por día y tipo de comida")

public class DietFoodController {

    private static final Logger logger = LoggerFactory.getLogger(DietFoodController.class);

    @Autowired
    private  DietFoodService dietFoodService;


    /**
     * Añadir un alimento a una dieta en un día y comida específicos.
     */
    @Operation(summary = "Añadir alimento a dieta", description = "Añade un alimento a una dieta para un día y tipo de comida específicos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Alimento añadido con éxito", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DietFoodDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
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

    @Operation(summary = "Reemplazar alimentos en una dieta", description = "Reemplaza los alimentos de una dieta en un día y tipo de comida específicos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alimentos reemplazados con éxito", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DietFoodDTO.class))),
            @ApiResponse(responseCode = "400", description = "Parámetros no coinciden o lista vacía"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
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
            logger.error("Error al reemplazar alimentos", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar alimentos.");
        }
    }
    /**
     * Eliminar un alimento de una dieta para un día y tipo de comida específicos.
     */
    @Operation(summary = "Eliminar alimento de dieta", description = "Elimina un alimento de una dieta específica por día y tipo de comida")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alimento eliminado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
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
    @Operation(summary = "Obtener alimentos de dieta por día", description = "Devuelve todos los alimentos de una dieta para un día específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de alimentos obtenida", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DietFoodDTO.class))),
            @ApiResponse(responseCode = "400", description = "Día de la semana inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    @GetMapping("/{dietId}/{dayWeek}")
    public ResponseEntity<?> getFoodsByDayOfTheWeek(@PathVariable Long dietId,
                                                    @PathVariable DayOfTheWeek dayWeek) {
        logger.info("Buscando alimentos en la dieta {} para el día {}", dietId, dayWeek);
        try {

            List<DietFoodDTO> foods = dietFoodService.getFoodsByDayOfTheWeek(dietId,dayWeek);


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
