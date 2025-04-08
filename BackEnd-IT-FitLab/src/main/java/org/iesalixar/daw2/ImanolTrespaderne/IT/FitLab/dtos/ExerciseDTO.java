package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums.MuscleGroup;

@Data
public class ExerciseDTO {
    private Long id;
    private String name;
    private String videoUrl;
    private MuscleGroup muscleGroup;
}
