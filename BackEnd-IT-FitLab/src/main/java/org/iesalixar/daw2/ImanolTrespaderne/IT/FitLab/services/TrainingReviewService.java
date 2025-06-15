package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.services;

import jakarta.validation.Valid;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.TrainingReviewDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.TrainingProgramme;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.TrainingReview;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.User;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.mappers.TrainingProgrammeMapper;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.mappers.TrainingReviewMapper;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.mappers.CreateUserMapper;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.mappers.UserMapper;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories.TrainingProgrammeRepository;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories.TrainingReviewRepository;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories.UserRepository;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainingReviewService {

    // Logger para registrar eventos e incidencias
    private static final Logger logger = LoggerFactory.getLogger(TrainingReview.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TrainingProgrammeMapper trainingProgrammeMapper;

    @Autowired
    private TrainingReviewRepository reviewRepository;

    @Autowired
    private TrainingReviewMapper reviewMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrainingProgrammeRepository programmeRepository;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Devuelve todas las reseñas almacenadas en la base de datos.
     */
    public List<TrainingReviewDTO> getAllReviews() {
        return reviewRepository.findAll()
                .stream()
                .map(reviewMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Devuelve una reseña específica por su ID.
     * Lanza excepción si no existe.
     */
    public TrainingReviewDTO getReview(@PathVariable Long reviewId) {
        return reviewRepository.findById(reviewId)
                .map(reviewMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));
    }

    /**
     * Devuelve todas las reseñas realizadas por un usuario concreto.
     */
    public List<TrainingReviewDTO> getReviewsByUser(@PathVariable Long userId) {
        return reviewRepository.findByUserId(userId)
                .stream()
                .map(reviewMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Devuelve todas las reseñas asociadas a un programa de entrenamiento concreto.
     */
    public List<TrainingReviewDTO> getReviewsByTrainingProgramme(@PathVariable Long programmeId) {
        return reviewRepository.findByTrainingProgrammeId(programmeId)
                .stream()
                .map(reviewMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Crea una nueva reseña asociándola automáticamente al usuario autenticado.
     * Utiliza el token JWT para obtener el usuario.
     */
    public TrainingReviewDTO createReview(TrainingReviewDTO dto) {
        // Obtener nombre de usuario autenticado a través del token
        String username = jwtUtil.getAuthenticatedUsername();
        if (username == null) {
            throw new SecurityException("Usuario no autenticado.");
        }

        // Buscar usuario por username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + username));

        // Convertir DTO a entidad y asignar el usuario
        TrainingReview review = reviewMapper.toEntity(dto);
        review.setUser(user);

        // Guardar reseña en la base de datos
        TrainingReview saved = reviewRepository.save(review);

        // Devolver la reseña guardada en formato DTO
        return reviewMapper.toDTO(saved);
    }

    /**
     * Actualiza una reseña existente con los nuevos valores proporcionados en el DTO.
     */
    public TrainingReviewDTO updateReview(@PathVariable Long reviewId, @Valid @RequestBody TrainingReviewDTO dto) {
        TrainingReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Reseña no encontrada"));

        review.setScore(dto.getScore());
        review.setComment(dto.getComment());
        review.setDate(dto.getDate());

        return reviewMapper.toDTO(reviewRepository.save(review));
    }

    /**
     * Elimina una reseña por su ID si existe.
     */
    public void deleteReview(@PathVariable Long reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new IllegalArgumentException("Reseña no encontrada");
        }
        reviewRepository.deleteById(reviewId);
    }

    /**
     * Valida que el usuario tenga permiso para acceder o modificar una reseña.
     * El propietario de la reseña o un administrador pueden hacerlo.
     */
    public void validateReviewAccess(Long reviewId, String username) {
        if (isAdmin(username)) return;

        TrainingReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Reseña no encontrada"));

        if (!review.getUser().getUsername().equals(username) && !isAdmin(username)) {
            throw new SecurityException("No tienes permiso para realizar esta acción.");
        }
    }

    /**
     * Verifica si un usuario tiene rol de administrador.
     */
    public boolean isAdmin(String username) {
        return userRepository.findByUsername(username)
                .map(user -> user.getRoles().stream()
                        .anyMatch(role -> role.getName().equals("ROLE_ADMIN")))
                .orElse(false);
    }
}
