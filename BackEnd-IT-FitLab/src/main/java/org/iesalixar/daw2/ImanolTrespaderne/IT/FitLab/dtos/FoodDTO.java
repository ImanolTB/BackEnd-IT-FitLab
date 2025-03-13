package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
public class FoodDTO {
    private Long id;
    private String name;
    private Integer calories;
    private BigDecimal proteins;
    private BigDecimal carbohydrates;
    private BigDecimal fats;
}
