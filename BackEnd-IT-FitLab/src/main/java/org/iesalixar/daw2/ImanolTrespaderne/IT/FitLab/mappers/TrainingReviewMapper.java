package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.mappers;

import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.TrainingProgrammeDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.TrainingReviewDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.UserDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.TrainingProgramme;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.TrainingReview;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.TrainingReviewPK;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TrainingReviewMapper {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TrainingProgrammeMapper trainingProgrammeMapper;

    public  TrainingReviewDTO toDTO(TrainingReview review) {
        TrainingReviewDTO dto = new TrainingReviewDTO();
        dto.setUser(userMapper.toDTO(review.getUser()));
        dto.setTrainingProgramme(trainingProgrammeMapper.toDTO(review.getTrainingProgramme()));
        dto.setScore(review.getScore());
        dto.setComment(review.getComment());
        dto.setDate(review.getDate());
        return dto;
    }

    public  TrainingReview toEntity(TrainingReviewDTO dto) {
        TrainingReview review = new TrainingReview();
        User user= userMapper.toEntity(dto.getUser());
        TrainingProgramme trainingProgramme= trainingProgrammeMapper.toEntity(dto.getTrainingProgramme());
        review.setId(new TrainingReviewPK(user.getId(), trainingProgramme.getId()));
        review.setUser(user);
        review.setTrainingProgramme(trainingProgramme);
        review.setScore(dto.getScore());
        review.setComment(dto.getComment());
        review.setDate(dto.getDate() != null ? dto.getDate() : LocalDateTime.now());
        return review;
    }
}
