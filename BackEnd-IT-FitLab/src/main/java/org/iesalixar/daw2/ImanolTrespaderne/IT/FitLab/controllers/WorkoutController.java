package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
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
@RequestMapping("/api/v1/workouts")
@Tag(name = "Workouts", description = "Operaciones sobre entrenamientos individuales")

public class WorkoutController {

    private static final Logger logger = LoggerFactory.getLogger(WorkoutController.class);

    @Autowired
    private WorkoutService workoutService;

    @Operation(summary = "Obtener todos los workouts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de workouts obtenida", content = @Content(mediaType = "application/json", schema = @Schema(implementation = WorkoutDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    @GetMapping
    public ResponseEntity<List<WorkoutDTO>> getAllWorkouts() {
        logger.info("Obteniendo todos los workouts.");
        return ResponseEntity.ok(workoutService.getAllWorkouts());
    }

    @Operation(summary = "Obtener workout por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Workout obtenido con éxito", content = @Content(mediaType = "application/json", schema = @Schema(implementation = WorkoutDTO.class))),
            @ApiResponse(responseCode = "404", description = "Workout no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
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

    @Operation(summary = "Obtener workouts por ID de programa")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de workouts del programa obtenida", content = @Content(mediaType = "application/json", schema = @Schema(implementation = WorkoutDTO.class))),
            @ApiResponse(responseCode = "404", description = "Programa de entrenamiento no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    @GetMapping("/{id}/workouts")
    public ResponseEntity<?> getWorkoutsByTrainingProgramme(@PathVariable Long id) {
        try {
            List<WorkoutDTO> workouts = workoutService.getWorkoutsByTrainingProgrammeID(id);
            return ResponseEntity.ok(workouts);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            // Aquí se puede utilizar un logger para registrar el error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno.");
        }
    }

    @Operation(summary = "Crear un nuevo workout")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Workout creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = WorkoutDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
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

    @Operation(summary = "Actualizar workout existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Workout actualizado correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = WorkoutDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
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

    @Operation(summary = "Eliminar workout por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Workout eliminado con éxito"),
            @ApiResponse(responseCode = "400", description = "Workout no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
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
