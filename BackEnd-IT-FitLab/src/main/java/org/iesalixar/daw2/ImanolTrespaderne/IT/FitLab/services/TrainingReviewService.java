package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.services;

import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.TrainingReviewDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.TrainingProgramme;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.TrainingReview;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.TrainingReviewPK;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.User;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.mappers.TrainingProgrammeMapper;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.mappers.TrainingReviewMapper;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.mappers.CreateUserMapper;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories.TrainingProgrammeRepository;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories.TrainingReviewRepository;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainingReviewService {

    @Autowired
    private CreateUserMapper userMapper;

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

    public List<TrainingReviewDTO> getAllReviews() {
        return reviewRepository.findAll()
                .stream()
                .map(reviewMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TrainingReviewDTO getReview(Long userId, Long programmeId) {
        TrainingReviewPK pk = new TrainingReviewPK(userId, programmeId);
        return reviewRepository.findById(pk)
                .map(reviewMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));
    }

    public List<TrainingReviewDTO> getReviewsByUser(Long userId) {
        return reviewRepository.findByUserId(userId)
                .stream()
                .map(reviewMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<TrainingReviewDTO> getReviewsByTrainingProgramme(Long programmeId) {
        return reviewRepository.findByTrainingProgrammeId(programmeId)
                .stream()
                .map(reviewMapper::toDTO)
                .collect(Collectors.toList());
    }

    public TrainingReviewDTO createReview(TrainingReviewDTO dto) {
        Long userId = dto.getUser().getId();
        Long programmeId = dto.getTrainingProgramme().getId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        TrainingProgramme programme = programmeRepository.findById(programmeId)
                .orElseThrow(() -> new RuntimeException("Programa de entrenamiento no encontrado"));

        TrainingReview review = reviewMapper.toEntity(dto);
        return reviewMapper.toDTO(reviewRepository.save(review));
    }

    public TrainingReviewDTO updateReview( TrainingReviewDTO dto) {
        User user = userMapper.toEntity(dto.getUser());
        TrainingProgramme trainingProgramme= trainingProgrammeMapper.toEntity(dto.getTrainingProgramme());
        Long userId= user.getId();
        Long programmeId= trainingProgramme.getId();
        TrainingReviewPK pk = new TrainingReviewPK(userId, programmeId);
        TrainingReview review = reviewRepository.findById(pk)
                .orElseThrow(() -> new IllegalArgumentException("Reseña no encontrada"));

        review.setScore(dto.getScore());
        review.setComment(dto.getComment());

        return reviewMapper.toDTO(reviewRepository.save(review));
    }

    public void deleteReview(Long userId, Long programmeId) {
        TrainingReviewPK pk = new TrainingReviewPK(userId, programmeId);
        if (!reviewRepository.existsById(pk)) {
            throw new IllegalArgumentException("Reseña no encontrada");
        }
        reviewRepository.deleteById(pk);
    }

    public void validateReviewAccess(Long userId, Long programmeId, String username) {
        if (isAdmin(username)) return; // acceso total para admins

        TrainingProgramme programme = programmeRepository.findById(programmeId)
                .orElseThrow(() -> new IllegalArgumentException("Programa de entrenamiento no encontrado"));

        if (!programme.getIsGeneric()) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

            if (!user.getUsername().equals(username)) {
                throw new SecurityException("No tienes permiso para acceder a esta reseña.");
            }
        }
    }
    public void validateUserAccess(Long requestedUserId, String username) {
        if (isAdmin(username)) return;

        User requestedUser = userRepository.findById(requestedUserId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Si el usuario autenticado está intentando acceder a otro usuario:
        if (!requestedUser.getUsername().equals(username)) {
            // Verificar que no existan reviews asociadas a programas NO genéricos
            List<TrainingReview> reviews = reviewRepository.findByUserId(requestedUserId);

            boolean hayReviewsPrivadas = reviews.stream()
                    .map(TrainingReview::getTrainingProgramme)
                    .anyMatch(programme -> !programme.getIsGeneric());

            if (hayReviewsPrivadas) {
                throw new SecurityException("No tienes permiso para acceder a estas reseñas privadas.");
            }
        }
    }
    public boolean isAdmin(String username) {
        return userRepository.findByUsername(username)
                .map(user -> user.getRoles().stream()
                        .anyMatch(role -> role.getName().equals("ROLE_ADMIN")))
                .orElse(false);
    }
}
