package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.WorkoutExerciseDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.services.WorkoutExerciseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workoutexercises")
public class WorkoutExerciseController {

    private static final Logger logger = LoggerFactory.getLogger(WorkoutExerciseController.class);

    @Autowired
    private WorkoutExerciseService workoutExerciseService;

    @GetMapping
    public ResponseEntity<List<WorkoutExerciseDTO>> getAllWorkoutExercises() {
        logger.info("Recibiendo solicitud para obtener todas las relaciones Workout-Exercise.");
        return ResponseEntity.ok(workoutExerciseService.getAllWorkoutExercises());
    }

    @GetMapping("/{workoutId}/{exerciseId}")
    public ResponseEntity<?> getWorkoutExerciseById(@PathVariable Long workoutId, @PathVariable Long exerciseId) {
        logger.info("Recibiendo solicitud para obtener relación Workout-Exercise con WorkoutID: {} y ExerciseID: {}", workoutId, exerciseId);
        try {
            return ResponseEntity.ok(workoutExerciseService.getWorkoutExerciseById(workoutId, exerciseId));
        } catch (IllegalArgumentException e) {
            logger.warn("Error al obtener Workout-Exercise: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al obtener Workout-Exercise: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno.");
        }
    }

    @PostMapping
    public ResponseEntity<?> createWorkoutExercise(@Valid @RequestBody WorkoutExerciseDTO dto) {
        logger.info("Recibiendo solicitud para crear una relación Workout-Exercise.");
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(workoutExerciseService.createWorkoutExercise(dto));
        } catch (IllegalArgumentException e) {
            logger.warn("Datos inválidos para crear Workout-Exercise: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al crear Workout-Exercise: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno.");
        }
    }

    @PutMapping("/{workoutId}/{exerciseId}")
    public ResponseEntity<?> updateWorkoutExercise(@PathVariable Long workoutId, @PathVariable Long exerciseId, @Valid @RequestBody WorkoutExerciseDTO dto) {
        logger.info("Recibiendo solicitud para actualizar relación Workout-Exercise con WorkoutID: {} y ExerciseID: {}", workoutId, exerciseId);
        try {
            return ResponseEntity.ok(workoutExerciseService.updateWorkoutExercise(workoutId, exerciseId, dto));
        } catch (IllegalArgumentException e) {
            logger.warn("Error en los datos al actualizar Workout-Exercise: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al actualizar Workout-Exercise: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno.");
        }
    }

    @DeleteMapping("/{workoutId}/{exerciseId}")
    public ResponseEntity<?> deleteWorkoutExercise(@PathVariable Long workoutId, @PathVariable Long exerciseId) {
        logger.info("Recibiendo solicitud para eliminar relación Workout-Exercise con WorkoutID: {} y ExerciseID: {}", workoutId, exerciseId);
        try {
            workoutExerciseService.deleteWorkoutExercise(workoutId, exerciseId);
            return ResponseEntity.ok("Workout-Exercise eliminado con éxito.");
        } catch (IllegalArgumentException e) {
            logger.warn("Intento de eliminar una relación Workout-Exercise inexistente: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al eliminar Workout-Exercise: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno.");
        }
    }
}
