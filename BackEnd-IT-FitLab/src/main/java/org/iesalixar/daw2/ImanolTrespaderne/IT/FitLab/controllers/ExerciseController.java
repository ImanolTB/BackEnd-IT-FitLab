package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.ExerciseCreateDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.ExerciseDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.services.ExerciseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/exercises")
@Tag(name = "Ejercicios", description = "Operaciones CRUD relacionadas con los ejercicios")

public class ExerciseController {

    private static final Logger logger = LoggerFactory.getLogger(ExerciseController.class);

    @Autowired
    private ExerciseService exerciseService;

    @Operation(summary = "Obtener todos los ejercicios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de ejercicios obtenida",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExerciseDTO.class))),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    @GetMapping
    public ResponseEntity<List<ExerciseDTO>> getAllExercises() {
        logger.info("Recibiendo solicitud para obtener todos los ejercicios.");
        return ResponseEntity.ok(exerciseService.getAllExercises());
    }
    @Operation(summary = "Obtener ejercicio por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ejercicio encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExerciseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Ejercicio no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getExerciseById(@PathVariable Long id) {
        logger.info("Recibiendo solicitud para obtener el ejercicio con ID: {}", id);
        try {
            return ResponseEntity.ok(exerciseService.getExerciseById(id));
        } catch (IllegalArgumentException e) {
            logger.warn("Error al obtener ejercicio con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al obtener el ejercicio con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al obtener el ejercicio.");
        }
    }
    @Operation(summary = "Crear nuevo ejercicio", description = "Solo administradores pueden crear ejercicios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ejercicio creado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExerciseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> createExercise(@Valid @ModelAttribute ExerciseCreateDTO dto) {
        logger.info("Recibiendo solicitud para crear un nuevo ejercicio.");
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(exerciseService.createExercise(dto));
        } catch (IllegalArgumentException e) {
            logger.warn("Datos inválidos para crear ejercicio: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al crear ejercicio: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al crear ejercicio.");
        }
    }
    @Operation(summary = "Actualizar ejercicio existente", description = "Solo administradores pueden actualizar ejercicios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ejercicio actualizado correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExerciseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<?> updateExercise(@PathVariable Long id, @Valid @ModelAttribute ExerciseCreateDTO dto) {
        logger.info("Recibiendo solicitud para actualizar ejercicio con ID: {}", id);
        try {
            return ResponseEntity.ok(exerciseService.updateExercise(id, dto));
        } catch (IllegalArgumentException e) {
            logger.warn("Error en los datos al actualizar ejercicio con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al actualizar ejercicio con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al actualizar ejercicio.");
        }
    }
    @Operation(summary = "Eliminar un ejercicio", description = "Solo administradores pueden eliminar ejercicios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ejercicio eliminado con éxito"),
            @ApiResponse(responseCode = "400", description = "Ejercicio no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExercise(@PathVariable Long id) {
        logger.info("Recibiendo solicitud para eliminar ejercicio con ID: {}", id);
        try {
            exerciseService.deleteExercise(id);
            return ResponseEntity.ok("Ejercicio eliminado con éxito.");
        } catch (IllegalArgumentException e) {
            logger.warn("Intento de eliminar un ejercicio inexistente con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al eliminar ejercicio con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al eliminar ejercicio.");
        }
    }
}
