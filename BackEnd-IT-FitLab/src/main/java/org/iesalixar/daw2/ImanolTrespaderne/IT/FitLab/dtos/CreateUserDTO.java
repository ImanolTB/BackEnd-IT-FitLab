package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos;

import lombok.Data;

import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums.ActivityLevel;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums.Gender;

import java.math.BigDecimal;

@Data
public class CreateUserDTO {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String name;
    private String lastName;
    private Integer age;
    private Integer height;
    private BigDecimal weight;
    private boolean enabled;
    private Gender gender;
    private ActivityLevel activityLevel;
}
