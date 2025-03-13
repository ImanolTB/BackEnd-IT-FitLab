package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DietFoodDTO {
    private Long dietId;
    private Long foodId;
    private String dayWeek;
    private String mealType;
    private BigDecimal quantity;
}
