package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.DietDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.UserDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.services.DietService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/diets")
public class DietController {
    private static final Logger logger = LoggerFactory.getLogger(DietController.class);

    @Autowired
    private DietService dietService;



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
            Optional<DietDTO> diet = Optional.ofNullable(dietService.getDietById(id));
            if (diet.isPresent()) {
                logger.info("Dieta con ID {} encontrado: {}", id, diet.get());
                return ResponseEntity.ok(diet.get());
            } else {
                logger.warn("No se encontró la dieta con ID: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró la dieta con ID: " + id);
            }
        } catch (Exception e) {
            logger.error("Error al buscar la dieta con ID {}: {}", id, e.getMessage());
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
            DietDTO updatedDiet = dietService.updateDiet(id, dto);
            return ResponseEntity.ok(updatedDiet);
        } catch (IllegalArgumentException e) {
            logger.warn("Error en los datos proporcionados de la dieta: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al actualizar la dieta: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la dieta.");
        }
    }

    /**
     * Eliminar dieta por ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        logger.info("Intentando eliminar la dieta con ID {}", id);
        try {
            dietService.deleteDiet(id);

            return ResponseEntity.ok("Usuario con id: "+id+" eliminado con éxito.");
        } catch (Exception e) {
            logger.error("Error al eliminar el usuario con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el usuario.");
        }
    }
}
