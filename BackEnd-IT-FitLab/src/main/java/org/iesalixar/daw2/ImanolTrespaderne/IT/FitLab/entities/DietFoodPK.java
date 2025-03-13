package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities;

import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
public class DietFoodPK implements Serializable {
    private Long diet;
    private Long food;
    private String dayWeek;
    private String mealType;
}
