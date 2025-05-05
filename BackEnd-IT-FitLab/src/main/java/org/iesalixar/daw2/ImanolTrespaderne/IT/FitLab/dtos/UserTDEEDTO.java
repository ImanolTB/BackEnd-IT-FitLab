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
public class UserTDEEDTO {
    private BigDecimal weight;
    private Integer height;
    private Integer age;
    private Gender gender;
    private ActivityLevel activityLevel;
}
