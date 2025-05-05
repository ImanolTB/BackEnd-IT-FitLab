package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.controllers;

import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.TrainingProgrammeDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.CreateUserDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.UpdateUserDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.services.TrainingProgrammeService;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.services.UserService;
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
@RequestMapping("/api/v1/trainingProgrammes")
public class TrainingProgrammeController {

    private static final Logger logger = LoggerFactory.getLogger(TrainingProgrammeController.class);

    @Autowired
    private TrainingProgrammeService programmeService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

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
     * Obtener una lsita de programas de entrenamiento por ID del usuario.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getTrainingProgrammesByUserId(@PathVariable Long userId) {
        logger.info("Solicitud GET: Obtener programas de entrenamiento del usuario con ID {}", userId);
        try {
            String username = jwtUtil.getAuthenticatedUsername();

            // Verificar que el usuario autenticado corresponde con el solicitado
            Optional<UpdateUserDTO> user = Optional.ofNullable(userService.getUserById(userId));
            if (user.isEmpty() || !user.get().getUsername().equals(username)) {
                logger.warn("El usuario autenticado no tiene permiso para acceder a estos programas de entrenamiento.");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso para acceder a estos programas de entrenamiento.");
            }

            List<TrainingProgrammeDTO> programmes = programmeService.getTrainingProgrammesByUserId(userId);
            if (programmes.isEmpty()) {
                logger.warn("No se encontraron programas de entrenamiento para el usuario con ID {}", userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontraron programas de entrenamiento para este usuario.");
            }

            logger.info("Programas de entrenamiento encontrados para el usuario con ID {}: {}", userId, programmes);
            return ResponseEntity.ok(programmes);

        } catch (IllegalArgumentException e) {
            logger.warn("Usuario no encontrado en GET: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado en GET por usuario: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }
    @GetMapping("/generic")
    public ResponseEntity<?> getGenericTrainingProgrammes() {
        logger.info("Solicitud para listar programas genéricos ordenados por trainingLevel.");
        try {
            List<TrainingProgrammeDTO> programmes = programmeService.getGenericTrainingProgrammesSortedByTrainingLevel();
            return ResponseEntity.ok(programmes);
        } catch (IllegalArgumentException ex) {
            logger.warn("Error en la solicitud: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (RuntimeException ex) {
            logger.error("Error interno en el servidor: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al obtener los programas genéricos.");
        }
    }
    /**
     * Obtener programa de entrenamiento por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getTrainingProgrammesById(@PathVariable Long id) {
        logger.info("Solicitud GET: Obtener programa de entrenamiento con ID {}", id);
        try {

            String username = jwtUtil.getAuthenticatedUsername();
            programmeService.validateOwnership(id, username);
            TrainingProgrammeDTO program = programmeService.getTrainingProgrammeById(id);
            if (program == null) {
                logger.warn("No se encontró el programa de entrenamiento con ID {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el programa de entrenamiento con ID: " + id);
            }
            logger.info("Programa de entrenamiento con ID {} encontrado: {}", id, program);
            return ResponseEntity.ok(program);

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
            String username = jwtUtil.getAuthenticatedUsername();

            // Validamos propiedad del programa
            programmeService.validateOwnership(id, username);

            // Obtener el programa de entrenamiento
            Optional<TrainingProgrammeDTO> existingProgramme = Optional.ofNullable(programmeService.getTrainingProgrammeById(id));
            if (existingProgramme.isEmpty()) {
                logger.warn("No se encontró el programa de entrenamiento con ID {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el programa de entrenamiento con ID: " + id);
            }

            // Validar que el usuario autenticado es el propietario del programa
            if (!existingProgramme.get().getUser().getUsername().equals(username)) {
                logger.warn("El usuario autenticado no tiene permiso para actualizar el programa con ID {}", id);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso para actualizar este programa de entrenamiento.");
            }

            // Realizar la actualización
            TrainingProgrammeDTO updatedProgramme = programmeService.updateTrainingProgramme(id, dto);
            logger.info("Programa de entrenamiento con ID {} actualizado exitosamente", id);
            return ResponseEntity.ok(updatedProgramme);

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
            String username = jwtUtil.getAuthenticatedUsername();
            programmeService.validateOwnership(id, username);

            // Obtener el programa de entrenamiento
            Optional<TrainingProgrammeDTO> existingProgramme = Optional.ofNullable(programmeService.getTrainingProgrammeById(id));
            if (existingProgramme.isEmpty()) {
                logger.warn("No se encontró el programa de entrenamiento con ID {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el programa de entrenamiento con ID: " + id);
            }
            // Eliminar el programa
            programmeService.deleteTrainingProgramme(id);
            logger.info("Programa de entrenamiento con ID {} eliminado exitosamente", id);
            return ResponseEntity.ok("Programa de entrenamiento eliminado correctamente.");

        } catch (IllegalArgumentException e) {
            logger.warn("Intento de eliminar programa inexistente: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado en DELETE: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al eliminar programa de entrenamiento.");
        }
    }

}
