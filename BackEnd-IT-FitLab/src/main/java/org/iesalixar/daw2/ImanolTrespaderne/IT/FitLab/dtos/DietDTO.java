package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums.Goal;

@Data
public class DietDTO {
    private Long id;
    private String name;
    private String description;
    private Integer durationWeeks;
    private Goal goal;
    private UserDTO user;
}
