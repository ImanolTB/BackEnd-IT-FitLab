package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DietDTO {
    private Long id;
    private String name;
    private String description;
    private Integer durationWeeks;
    private UserDTO user;
}
