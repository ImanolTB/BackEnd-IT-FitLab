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
    public List<TrainingReviewDTO> getAllReviews() {
        return reviewRepository.findAll()
                .stream()
                .map(reviewMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TrainingReviewDTO getReview(@PathVariable Long reviewId) {
        return reviewRepository.findById(reviewId)
                .map(reviewMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));
    }

    public List<TrainingReviewDTO> getReviewsByUser(@PathVariable Long userId) {
        return reviewRepository.findByUserId(userId)
                .stream()
                .map(reviewMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<TrainingReviewDTO> getReviewsByTrainingProgramme(@PathVariable Long programmeId) {
        return reviewRepository.findByTrainingProgrammeId(programmeId)
                .stream()
                .map(reviewMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TrainingReviewDTO createReview(TrainingReviewDTO dto) {
        // 1. Obtener username del token
        String username = jwtUtil.getAuthenticatedUsername();
        if (username == null) {
            throw new SecurityException("Usuario no autenticado.");
        }

        // 2. Buscar el usuario real en la BD
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + username));

        // 3. Convertir el DTO a entidad
        TrainingReview review = reviewMapper.toEntity(dto);

        // 4. Asignar el usuario a la entidad
        review.setUser(user);

        // 5. Guardar la reseña
        TrainingReview saved = reviewRepository.save(review);

        // 6. Devolver la reseña convertida a DTO
        return reviewMapper.toDTO(saved);
    }
    public TrainingReviewDTO updateReview(@PathVariable Long reviewId,@Valid @RequestBody TrainingReviewDTO dto) {


        TrainingReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Reseña no encontrada"));

        review.setScore(dto.getScore());
        review.setComment(dto.getComment());
        review.setDate(dto.getDate());

        return reviewMapper.toDTO(reviewRepository.save(review));
    }

    public void deleteReview(@PathVariable Long reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new IllegalArgumentException("Reseña no encontrada");
        }
        reviewRepository.deleteById(reviewId);
    }

    public void validateReviewAccess( Long reviewId, String username) {
        if (isAdmin(username)) return; // acceso total para admins

        TrainingReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Reseña no encontrada"));
        if (!review.getUser().getUsername().equals(username) && !isAdmin(username)) {
            throw new SecurityException("No tienes permiso para realizar esta acción.");
        }
    }
    public boolean isAdmin(String username) {
        return userRepository.findByUsername(username)
                .map(user -> user.getRoles().stream()
                        .anyMatch(role -> role.getName().equals("ROLE_ADMIN")))
                .orElse(false);
    }
}
