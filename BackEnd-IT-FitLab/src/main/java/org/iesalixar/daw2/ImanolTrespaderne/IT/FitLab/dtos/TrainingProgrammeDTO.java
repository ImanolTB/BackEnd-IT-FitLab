package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class TrainingProgrammeDTO {
    private Long id;
    private String name;
    private Integer durationWeeks;
    private Long userId;
}
