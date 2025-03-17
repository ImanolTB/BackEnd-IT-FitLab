package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WorkoutExerciseDTO {
    private Long workoutId;
    private Long exerciseId;
    private Integer sets;
    private Integer repetitions;
    private BigDecimal weight;
}
