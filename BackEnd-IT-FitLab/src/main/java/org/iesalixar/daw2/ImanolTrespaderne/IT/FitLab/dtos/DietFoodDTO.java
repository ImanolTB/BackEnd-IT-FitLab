package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos;

import lombok.Getter;
import lombok.Setter;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums.DayOfTheWeek;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums.MealType;

import java.math.BigDecimal;

@Getter
@Setter
public class DietFoodDTO {
    private Long dietId;
    private Long foodId;
    private DayOfTheWeek dayWeek;
    private MealType mealType;
    private BigDecimal quantity;
}
