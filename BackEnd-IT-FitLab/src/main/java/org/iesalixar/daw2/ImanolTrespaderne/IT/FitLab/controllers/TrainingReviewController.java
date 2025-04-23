package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.controllers;

import jakarta.validation.Valid;
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



    @PostMapping
    public ResponseEntity<?> createReview(@Valid @RequestBody TrainingReviewDTO reviewDTO) {
        logger.info("Solicitud para crear reseña");
        try {
            TrainingReviewDTO createdReview = reviewService.createReview(reviewDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdReview);
        } catch (IllegalArgumentException e) {
            logger.warn("Error al crear reseña: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            logger.error("Error inesperado al crear reseña: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al crear la reseña.");
        }
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<?> updateReview(@PathVariable Long reviewId, @RequestBody TrainingReviewDTO dto) {
        logger.info("Solicitud para actualizar reseña ");
        try {
            String username = jwtUtil.getAuthenticatedUsername();
            return ResponseEntity.ok(reviewService.updateReview(reviewId,dto));
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

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId) {
        logger.info("Solicitud para eliminar reseña con id: {}", reviewId);
        try {
            String username = jwtUtil.getAuthenticatedUsername();
            // Obtenemos la reseña completa para formar el usuario asociado
            TrainingReviewDTO dto = reviewService.getReview(reviewId);
            // Validamos que el username del token coincida con el del usuario de la reseña


            // Si la validación pasa, se procede a eliminar la reseña
            reviewService.deleteReview(reviewId);
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
