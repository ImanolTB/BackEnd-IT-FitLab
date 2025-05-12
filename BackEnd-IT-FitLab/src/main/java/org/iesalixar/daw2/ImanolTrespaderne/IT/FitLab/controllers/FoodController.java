package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.FoodDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.services.FoodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/food")
@Tag(name = "Alimentos", description = "Operaciones relacionadas con los alimentos")

public class FoodController {

    private static final Logger logger = LoggerFactory.getLogger(FoodController.class);
    private final FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @Operation(summary = "Obtener todos los alimentos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de alimentos encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = FoodDTO.class))),
            @ApiResponse(responseCode = "204", description = "No hay alimentos registrados"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    @GetMapping
    public ResponseEntity<List<FoodDTO>> getAllFoods() {
        try {
            List<FoodDTO> foods = foodService.getAllFoods();
            if (foods.isEmpty()) {
                logger.warn("No hay alimentos registrados.");
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(foods);
        } catch (Exception e) {
            logger.error("Error al listar los alimentos: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Obtener un alimento por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alimento encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = FoodDTO.class))),
            @ApiResponse(responseCode = "404", description = "Alimento no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getFoodById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(foodService.getFoodById(id));
        } catch (RuntimeException e) {
            logger.warn("Alimento con ID {} no encontrado.", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el alimento con ID " + id);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear un nuevo alimento", description = "Requiere rol ADMIN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Alimento creado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = FoodDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno al crear alimento")
    })
    @PostMapping

    public ResponseEntity<?> createFood(@RequestBody FoodDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(foodService.createFood(dto));
        } catch (Exception e) {
            logger.error("Error al crear el alimento: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el alimento.");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar un alimento existente", description = "Requiere rol ADMIN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alimento actualizado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = FoodDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Alimento no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno al actualizar alimento")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFood(@PathVariable Long id, @RequestBody FoodDTO dto) {
        try {
            return ResponseEntity.ok(foodService.updateFood(id, dto));
        } catch (IllegalArgumentException e) {
            logger.warn("Datos inválidos para el alimento con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            logger.warn("Alimento con ID {} no encontrado.", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el alimento con ID " + id);
        } catch (Exception e) {
            logger.error("Error al actualizar el alimento: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el alimento.");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar un alimento", description = "Requiere rol ADMIN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alimento eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Alimento no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno al eliminar alimento")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFood(@PathVariable Long id) {
        try {
            foodService.deleteFood(id);
            return ResponseEntity.ok("Alimento eliminado con éxito.");
        } catch (IllegalArgumentException e) {
            logger.warn("Intento de eliminar un alimento inexistente con ID {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al eliminar el alimento: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el alimento.");
        }
    }
}
