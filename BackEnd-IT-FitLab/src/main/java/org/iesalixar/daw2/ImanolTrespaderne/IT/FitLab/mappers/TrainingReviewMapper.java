package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.mappers;

import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.TrainingReviewDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.TrainingReview;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.TrainingProgramme;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.User;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class TrainingReviewMapper {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository; // Para obtener el usuario real en toEntity

    @Autowired
    private TrainingProgrammeMapper trainingProgrammeMapper;

    public TrainingReviewDTO toDTO(TrainingReview review) {
        TrainingReviewDTO dto = new TrainingReviewDTO();
        dto.setId(review.getId());
        dto.setUser(userMapper.toDTO(review.getUser()));
        dto.setTrainingProgramme(trainingProgrammeMapper.toDTO(review.getTrainingProgramme()));
        dto.setScore(review.getScore());
        dto.setComment(review.getComment());
        dto.setDate(review.getDate());
        return dto;
    }

    /**
     * Convierte el DTO a entidad.
     * Se espera que el DTO contenga, al menos, un UserDTO con un id válido y un TrainingProgrammeDTO.
     */
    public TrainingReview toEntity(TrainingReviewDTO dto) {
        TrainingReview review = new TrainingReview();
        // Verificamos y obtenemos el usuario real a partir del ID del DTO.

        // Convertir el trainingProgramme
        TrainingProgramme trainingProgramme = trainingProgrammeMapper.toEntity(dto.getTrainingProgramme());
        User user = userMapper.toEntity(dto.getUser());
        review.setUser(user);
        review.setTrainingProgramme(trainingProgramme);
        review.setId(dto.getId());
        review.setScore(dto.getScore());
        review.setComment(dto.getComment());
        review.setDate(dto.getDate() != null ? dto.getDate() : ZonedDateTime.now(ZoneId.of("Europe/Madrid")));
        return review;
    }
}
