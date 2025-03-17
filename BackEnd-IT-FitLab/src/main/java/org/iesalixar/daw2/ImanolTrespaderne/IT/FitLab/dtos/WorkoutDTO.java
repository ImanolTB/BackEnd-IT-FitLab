package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos;

import lombok.Data;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.TrainingProgramme;

@Data
public class WorkoutDTO {
    private Long id;
    private String name;
    private String description;
    private Integer sessionNumber;
    private Long trainingProgramme;
}
