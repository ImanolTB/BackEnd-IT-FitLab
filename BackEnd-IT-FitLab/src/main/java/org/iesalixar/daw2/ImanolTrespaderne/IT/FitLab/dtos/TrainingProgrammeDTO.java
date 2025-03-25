package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums.TrainingLevel;

@Data
public class TrainingProgrammeDTO {
    private Long id;
    private String name;
    private Boolean isGeneric;
    private TrainingLevel trainingLevel;
    private Integer durationWeeks;
    private UserDTO user;
}
