package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.DietDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.FoodDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.UserDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.Diet;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.Food;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums.DayOfTheWeek;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.services.DietService;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.services.FoodService;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/diets")
public class DietController {
    private static final Logger logger = LoggerFactory.getLogger(DietController.class);

    @Autowired
    private DietService dietService;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private FoodService foodService;

    /**
     * Obtener todas las dietas.
     */
    @GetMapping
    public ResponseEntity<List<DietDTO>> getAllDiets() {
        logger.info("Solicitando la lista de todas las dietas...");
        try {
            List<DietDTO> diets = dietService.getAllDiets();
            if (diets.isEmpty()) {
                logger.warn("No hay dietas registradas.");
                return ResponseEntity.noContent().build();
            }
            logger.info("Se han encontrado {} dietas.", diets.size());
            return ResponseEntity.ok(diets);
        } catch (Exception e) {
            logger.error("Error al listar las dietas: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Obtener una dieta por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getDietById(@PathVariable Long id) {
        logger.info("Buscando usuario con ID: {}", id);
        try {
            String username = jwtUtil.getAuthenticatedUsername();

            // Obtener la dieta por ID
            Optional<DietDTO> diet = Optional.ofNullable(dietService.getDietById(id));

            if (!diet.isPresent()) {
                logger.warn("No se encontró la dieta con ID: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró la dieta con ID: " + id);
            }

            // Validar si el usuario autenticado es el propietario de la dieta
            if (!diet.get().getUser().getUsername().equals(username)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso para acceder a esta dieta.");
            }

            logger.info("Dieta con ID {} encontrada: {}", id, diet);
            return ResponseEntity.ok(diet);

        } catch (Exception e) {
            logger.error("Error al buscar la dieta con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }

    @GetMapping("/{id}/day/{dayOfWeek}")
    public ResponseEntity<?> getFoodsByDietAndDay(@PathVariable Long id, @PathVariable String dayOfWeek) {
        logger.info("Solicitud GET: Obtener alimentos de la dieta con ID {} para el día {}", id, dayOfWeek);
        try {
            String username = jwtUtil.getAuthenticatedUsername();

            // Convertir el String a un valor del enum DayOfTheWeek
            DayOfTheWeek dayEnum;
            try {
                dayEnum = DayOfTheWeek.valueOf(dayOfWeek.toUpperCase()); // Convierte el String a Enum (en mayúsculas)
            } catch (IllegalArgumentException e) {
                logger.warn("Día de la semana inválido: {}", dayOfWeek);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Día de la semana inválido: " + dayOfWeek);
            }

            // Obtener la dieta por ID
            Optional<DietDTO> diet = Optional.ofNullable(dietService.getDietById(id));

            if (diet.isEmpty()) {
                logger.warn("No se encontró la dieta con ID: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró la dieta con ID: " + id);
            }

            // Validar si el usuario autenticado es el propietario de la dieta
            if (!diet.get().getUser().getUsername().equals(username)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso para acceder a esta dieta.");
            }

            // Obtener alimentos de la dieta para el día solicitado
            List<FoodDTO> foodsForDay = foodService.getFoodsByDay(id, dayEnum);

            if (foodsForDay.isEmpty()) {
                logger.warn("No se encontraron alimentos para la dieta con ID {} en el día {}", id, dayOfWeek);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron alimentos para el día " + dayOfWeek);
            }

            logger.info("Dieta con ID {} encontrada para el día {}: {}", id, dayOfWeek, foodsForDay);
            return ResponseEntity.ok(foodsForDay);

        } catch (Exception e) {
            logger.error("Error al obtener la dieta con ID {} para el día {}: {}", id, dayOfWeek, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }


    /**
     * Crear una nueva dieta.
     */
    @PostMapping
    public ResponseEntity<?> createDiet(@Valid @RequestBody DietDTO dto) {
        logger.info("Intentando registrar una nueva dieta con nombre: {}", dto.getName());
        try {
            DietDTO createdDiet = dietService.createDiet(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDiet);
        } catch (IllegalArgumentException e) {
            logger.warn("Error en los datos proporcionados de la dieta: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al crear la dieta: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear al crear la dieta.");
        }
    }

    /**
     * Actualizar una dieta existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDiet(@PathVariable Long id, @Valid @RequestBody DietDTO dto) {
        logger.info("Intentando actualizar la dieta con ID {}", id);
        try {
            String username = jwtUtil.getAuthenticatedUsername();

            // Obtener la dieta existente
            Optional<DietDTO> existingDiet = Optional.ofNullable(dietService.getDietById(id));
            if (existingDiet.isEmpty()) {
                logger.warn("No se encontró la dieta con ID {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró la dieta con ID: " + id);
            }

            // Validar que el usuario autenticado es el propietario de la dieta
            if (!existingDiet.get().getUser().getUsername().equals(username)) {
                logger.warn("El usuario autenticado no tiene permiso para actualizar la dieta con ID {}", id);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso para actualizar esta dieta.");
            }

            // Realizar la actualización
            DietDTO updatedDiet = dietService.updateDiet(id, dto);
            logger.info("Dieta con ID {} actualizada exitosamente", id);
            return ResponseEntity.ok(updatedDiet);

        } catch (IllegalArgumentException e) {
            logger.warn("Error en los datos proporcionados de la dieta: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al actualizar la dieta: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al actualizar la dieta.");
        }
    }


    /**
     * Eliminar dieta por ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDiet(@PathVariable Long id) {
        logger.info("Intentando eliminar la dieta con ID {}", id);
        try {
            String username = jwtUtil.getAuthenticatedUsername();

            // Obtener la dieta existente
            Optional<DietDTO> existingDiet = Optional.ofNullable(dietService.getDietById(id));
            if (existingDiet.isEmpty()) {
                logger.warn("No se encontró la dieta con ID {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró la dieta con ID: " + id);
            }

            // Validar que el usuario autenticado es el propietario de la dieta
            if (!existingDiet.get().getUser().getUsername().equals(username)) {
                logger.warn("El usuario autenticado no tiene permiso para eliminar la dieta con ID {}", id);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso para eliminar esta dieta.");
            }

            // Eliminar la dieta
            dietService.deleteDiet(id);
            logger.info("Dieta con ID {} eliminada exitosamente", id);
            return ResponseEntity.ok("Dieta con ID " + id + " eliminada con éxito.");

        } catch (Exception e) {
            logger.error("Error al eliminar la dieta con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al eliminar la dieta.");
        }
    }

}
