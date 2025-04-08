package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TrainingReviewDTO {
    private UserDTO user;
    private TrainingProgrammeDTO trainingProgramme;
    private Integer score;
    private String comment;
    private LocalDateTime date;

}

