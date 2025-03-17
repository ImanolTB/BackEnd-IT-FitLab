package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.controllers;

import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.TrainingProgrammeDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.services.TrainingProgrammeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trainingprogrammes")
public class TrainingProgrammeController {

    private static final Logger logger = LoggerFactory.getLogger(TrainingProgrammeController.class);

    @Autowired
    private TrainingProgrammeService programmeService;

    /**
     * Obtener todos los programas de entrenamiento.
     */
    @GetMapping
    public ResponseEntity<?> getAllTrainingProgrammes() {
        logger.info("Solicitud GET: Obtener todos los programas de entrenamiento.");
        try {
            List<TrainingProgrammeDTO> programmes = programmeService.getAllTrainingProgrammes();
            return ResponseEntity.ok(programmes);
        } catch (Exception e) {
            logger.error("Error inesperado al obtener programas de entrenamiento: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }

    /**
     * Obtener un programa de entrenamiento por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getTrainingProgrammeById(@PathVariable Long id) {
        logger.info("Solicitud GET: Obtener programa de entrenamiento con ID {}", id);
        try {
            return ResponseEntity.ok(programmeService.getTrainingProgrammeById(id));
        } catch (IllegalArgumentException e) {
            logger.warn("Error de datos en GET: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }

    /**
     * Obtener programas de entrenamiento por ID de usuario.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getTrainingProgrammesByUserId(@PathVariable Long userId) {
        logger.info("Solicitud GET: Obtener programas de entrenamiento del usuario con ID {}", userId);
        try {
            List<TrainingProgrammeDTO> programmes = programmeService.getTrainingProgrammesByUserId(userId);
            if (programmes.isEmpty()) {
                logger.warn("No se encontraron programas de entrenamiento para el usuario con ID {}", userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron programas de entrenamiento para este usuario.");
            }
            return ResponseEntity.ok(programmes);
        } catch (IllegalArgumentException e) {
            logger.warn("Usuario no encontrado en GET: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado en GET por usuario: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }

    /**
     * Crear un nuevo programa de entrenamiento.
     */
    @PostMapping
    public ResponseEntity<?> createTrainingProgramme(@RequestBody TrainingProgrammeDTO dto) {
        logger.info("Solicitud POST: Crear nuevo programa de entrenamiento.");
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(programmeService.createTrainingProgramme(dto));
        } catch (IllegalArgumentException e) {
            logger.warn("Datos inválidos en POST: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado en POST: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al crear programa de entrenamiento.");
        }
    }

    /**
     * Actualizar un programa de entrenamiento existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTrainingProgramme(@PathVariable Long id, @RequestBody TrainingProgrammeDTO dto) {
        logger.info("Solicitud PUT: Actualizar programa de entrenamiento con ID {}", id);
        try {
            return ResponseEntity.ok(programmeService.updateTrainingProgramme(id, dto));
        } catch (IllegalArgumentException e) {
            logger.warn("Datos inválidos en PUT: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado en PUT: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al actualizar programa de entrenamiento.");
        }
    }

    /**
     * Eliminar un programa de entrenamiento por ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTrainingProgramme(@PathVariable Long id) {
        logger.info("Solicitud DELETE: Eliminar programa de entrenamiento con ID {}", id);
        try {
            programmeService.deleteTrainingProgramme(id);
            return ResponseEntity.ok("Programa de entrenamiento eliminado correctamente.");
        } catch (IllegalArgumentException e) {
            logger.warn("Intento de eliminar programa inexistente: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado en DELETE: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al eliminar programa de entrenamiento.");
        }
    }
}
