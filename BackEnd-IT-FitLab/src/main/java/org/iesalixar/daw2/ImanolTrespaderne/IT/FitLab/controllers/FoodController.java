package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.controllers;

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
public class FoodController {

    private static final Logger logger = LoggerFactory.getLogger(FoodController.class);
    private final FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

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
