package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums.ActivityLevel;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums.Gender;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDTO {
    private Long id;
    private String username;
    private String email;
    private String name;
    private String lastName;
    private Integer age;
    private Integer height;
    private BigDecimal weight;
    private boolean enabled;
    private ActivityLevel activityLevel;
}
