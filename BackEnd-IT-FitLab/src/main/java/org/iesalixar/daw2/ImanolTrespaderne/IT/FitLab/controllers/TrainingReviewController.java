package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.controllers;

import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.TrainingReviewDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.services.TrainingReviewService;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
public class TrainingReviewController {

    private static final Logger logger = LoggerFactory.getLogger(TrainingReviewController.class);

    @Autowired
    private TrainingReviewService reviewService;

    @Autowired
    private JwtUtil jwtUtil;
    @GetMapping
    public ResponseEntity<?> getAllReviews() {
        logger.info("Solicitud para obtener todas las reseñas.");
        try {
            return ResponseEntity.ok(reviewService.getAllReviews());
        } catch (Exception e) {
            logger.error("Error al obtener todas las reseñas: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al obtener las reseñas.");
        }
    }


    @GetMapping("/{userId}/{programmeId}")
    public ResponseEntity<?> getReview(@PathVariable Long userId, @PathVariable Long programmeId) {
        logger.info("Solicitud para obtener reseña de userId {} y programmeId {}", userId, programmeId);
        try {
            String username = jwtUtil.getAuthenticatedUsername();
            reviewService.validateReviewAccess(userId, programmeId, username);
            return ResponseEntity.ok(reviewService.getReview(userId, programmeId));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al obtener reseña compuesta: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reseña no encontrada.");
        }
    }

    @GetMapping("/programme/{programmeId}")
    public ResponseEntity<?> getReviewsByProgramme(@PathVariable Long programmeId) {
        logger.info("Solicitud para obtener reseñas del entrenamiento ID: {}", programmeId);
        try {
            return ResponseEntity.ok(reviewService.getReviewsByTrainingProgramme(programmeId));
        } catch (Exception e) {
            logger.error("Error al obtener reseñas del entrenamiento {}: {}", programmeId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al obtener las reseñas del entrenamiento.");
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getReviewsByUser(@PathVariable Long userId) {
        logger.info("Solicitud para obtener reseñas del usuario ID: {}", userId);
        try {
            String username = jwtUtil.getAuthenticatedUsername();
            reviewService.validateUserAccess(userId, username);
            return ResponseEntity.ok(reviewService.getReviewsByUser(userId));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al obtener reseñas del usuario {}: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al obtener las reseñas del usuario.");
        }
    }


    // ✅ POST: CREAR
    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody TrainingReviewDTO dto) {
        logger.info("Solicitud para crear reseña (user: {}, programme: {})",
                dto.getUser().getId(), dto.getTrainingProgramme().getId());
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.createReview(dto));
        } catch (IllegalArgumentException e) {
            logger.warn("Error al crear reseña: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al crear reseña: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al crear la reseña.");
        }
    }

    @PutMapping("/{userId}/{programmeId}")
    public ResponseEntity<?> updateReview(@RequestBody TrainingReviewDTO dto) {
        logger.info("Solicitud para actualizar reseña ");
        try {
            String username = jwtUtil.getAuthenticatedUsername();
            reviewService.validateReviewAccess(dto.getUser().getId(), dto.getTrainingProgramme().getId(), username);
            return ResponseEntity.ok(reviewService.updateReview(dto));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.warn("Error al actualizar reseña: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al actualizar reseña: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al actualizar la reseña.");
        }
    }

    @DeleteMapping("/{userId}/{programmeId}")
    public ResponseEntity<?> deleteReview(@PathVariable Long userId,
                                          @PathVariable Long programmeId) {
        logger.info("Solicitud para eliminar reseña (user: {}, programme: {})", userId, programmeId);
        try {
            String username = jwtUtil.getAuthenticatedUsername();
            reviewService.validateReviewAccess(userId, programmeId, username);
            reviewService.deleteReview(userId, programmeId);
            return ResponseEntity.ok("Reseña eliminada con éxito.");
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.warn("Error al eliminar reseña: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al eliminar reseña: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al eliminar la reseña.");
        }
    }
}
