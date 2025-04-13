package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.DietDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.FoodDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums.DayOfTheWeek;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.services.DietService;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.services.FoodService;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<DietDTO>> getAllDiets() {
        logger.info("Solicitando la lista de todas las dietas...");
        try {
            List<DietDTO> diets = dietService.getAllDiets();
            if (diets.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(diets);
        } catch (Exception e) {
            logger.error("Error al listar las dietas: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDietById(@PathVariable Long id) {
        logger.info("Buscando dieta con ID: {}", id);
        try {
            String username = jwtUtil.getAuthenticatedUsername();
            dietService.validateOwnership(id, username);
            return ResponseEntity.ok(dietService.getDietById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error interno al obtener la dieta: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getDietByUsername() {
        try {
            String username = jwtUtil.getAuthenticatedUsername();
            List<DietDTO> diets = dietService.getDietByUsername(username);

            if (diets.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontraron dietas para el usuario.");
            }

            return ResponseEntity.ok(diets);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acceso denegado: " + e.getMessage());

        } catch (Exception e) {
            logger.error("Error al obtener dietas por username: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor.");
        }
    }


    @GetMapping("/{id}/day/{dayOfWeek}")
    public ResponseEntity<?> getFoodsByDietAndDay(@PathVariable Long id, @PathVariable String dayOfWeek) {
        logger.info("Obteniendo alimentos de la dieta {} para el día {}", id, dayOfWeek);
        try {
            String username = jwtUtil.getAuthenticatedUsername();
            dietService.validateOwnership(id, username);

            DayOfTheWeek dayEnum;
            try {
                dayEnum = DayOfTheWeek.valueOf(dayOfWeek.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Día inválido: " + dayOfWeek);
            }

            List<FoodDTO> foods = foodService.getFoodsByDay(id, dayEnum);
            return foods.isEmpty()
                    ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron alimentos para el día " + dayOfWeek)
                    : ResponseEntity.ok(foods);

        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al obtener alimentos: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno.");
        }
    }

    @PostMapping
    public ResponseEntity<?> createDiet(@Valid @RequestBody DietDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(dietService.createDiet(dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la dieta.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDiet(@PathVariable Long id, @Valid @RequestBody DietDTO dto) {
        try {
            String username = jwtUtil.getAuthenticatedUsername();
            dietService.validateOwnership(id, username);
            return ResponseEntity.ok(dietService.updateDiet(id, dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al actualizar la dieta.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDiet(@PathVariable Long id) {
        try {
            String username = jwtUtil.getAuthenticatedUsername();
            dietService.validateOwnership(id, username);
            dietService.deleteDiet(id);
            return ResponseEntity.ok("Dieta con ID " + id + " eliminada con éxito.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al eliminar la dieta.");
        }
    }

}
