package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
public class TrainingReviewDTO {
    private Long id;
    private UserDTO user;
    private TrainingProgrammeDTO trainingProgramme;
    private Integer score;
    private String comment;
    private ZonedDateTime date;

}

