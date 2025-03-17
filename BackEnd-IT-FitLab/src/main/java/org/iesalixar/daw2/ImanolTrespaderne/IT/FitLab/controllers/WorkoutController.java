package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.WorkoutDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.services.WorkoutService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workouts")
public class WorkoutController {

    private static final Logger logger = LoggerFactory.getLogger(WorkoutController.class);

    @Autowired
    private WorkoutService workoutService;

    @GetMapping
    public ResponseEntity<List<WorkoutDTO>> getAllWorkouts() {
        logger.info("Obteniendo todos los workouts.");
        return ResponseEntity.ok(workoutService.getAllWorkouts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getWorkoutById(@PathVariable Long id) {
        logger.info("Obteniendo workout con ID: {}", id);
        try {
            return ResponseEntity.ok(workoutService.getWorkoutById(id));
        } catch (IllegalArgumentException e) {
            logger.warn("Workout no encontrado: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno.");
        }
    }

    @PostMapping
    public ResponseEntity<?> createWorkout(@Valid @RequestBody WorkoutDTO dto) {
        logger.info("Solicitud para crear workout.");
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(workoutService.createWorkout(dto));
        } catch (IllegalArgumentException e) {
            logger.warn("Datos inválidos: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateWorkout(@PathVariable Long id, @Valid @RequestBody WorkoutDTO dto) {
        logger.info("Solicitud para actualizar workout con ID: {}", id);
        try {
            return ResponseEntity.ok(workoutService.updateWorkout(id, dto));
        } catch (IllegalArgumentException e) {
            logger.warn("Datos inválidos: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWorkout(@PathVariable Long id) {
        logger.info("Solicitud para eliminar workout con ID: {}", id);
        try {
            workoutService.deleteWorkout(id);
            return ResponseEntity.ok("Workout eliminado con éxito.");
        } catch (IllegalArgumentException e) {
            logger.warn("Workout no encontrado: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno.");
        }
    }
}
