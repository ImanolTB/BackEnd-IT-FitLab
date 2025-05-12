package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/v1/workoutexercises")
@Tag(name = "Sesiones de entrenamiento", description = "Relaciones entre workouts y ejercicios. Detalle de la sesion de entrenamiento")

public class WorkoutExerciseController {

    private static final Logger logger = LoggerFactory.getLogger(WorkoutExerciseController.class);

    @Autowired
    private WorkoutExerciseService workoutExerciseService;
    @Operation(summary = "Obtener todas las relaciones Workout-Exercise")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relaciones obtenidas correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = WorkoutExerciseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    @GetMapping
    public ResponseEntity<List<WorkoutExerciseDTO>> getAllWorkoutExercises() {
        logger.info("Recibiendo solicitud para obtener todas las relaciones Workout-Exercise.");
        return ResponseEntity.ok(workoutExerciseService.getAllWorkoutExercises());
    }
    @Operation(summary = "Obtener una relación Workout-Exercise por IDs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relación encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = WorkoutExerciseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Relación no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
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
    @Operation(summary = "Obtener ejercicios por workout ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ejercicios encontrados",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = WorkoutExerciseDTO.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron ejercicios para el workout"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    @GetMapping("/workout/{workoutId}")
    public ResponseEntity<?> getExercisesByWorkoutId(@PathVariable Long workoutId) {
        logger.info("Obteniendo ejercicios para el workout con ID: {}", workoutId);
        try {
            List<WorkoutExerciseDTO> exercises = workoutExerciseService.getExercisesByWorkoutId(workoutId);
            return ResponseEntity.ok(exercises);
        } catch (IllegalArgumentException e) {
            logger.warn("Error al obtener ejercicios para el workout: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al obtener ejercicios para el workout: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno.");
        }
    }
    @Operation(summary = "Crear una relación Workout-Exercise")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Relación creada correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = WorkoutExerciseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
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
    @Operation(summary = "Actualizar una relación Workout-Exercise")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relación actualizada correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = WorkoutExerciseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
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
    @Operation(summary = "Eliminar una relación Workout-Exercise")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relación eliminada correctamente"),
            @ApiResponse(responseCode = "400", description = "Relación no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
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
