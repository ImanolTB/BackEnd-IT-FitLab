package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities;

import jakarta.persistence.*;
import lombok.*;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums.DayOfTheWeek;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums.MealType;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "diet_food")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class DietFood {

    @EmbeddedId
    private DietFoodPK id;

    @ManyToOne
    @MapsId("dietId")
    @JoinColumn(name = "diet_id", nullable = false)
    private Diet diet;

    @ManyToOne
    @MapsId("foodId")
    @JoinColumn(name = "food_id", nullable = false)
    private Food food;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal quantity ;

    public DietFood(Diet diet, Food food, DayOfTheWeek dayWeek, MealType mealType, BigDecimal quantity) {
        this.id = new DietFoodPK(diet.getId(), food.getId(), dayWeek, mealType);
        this.diet = diet;
        this.food = food;
        this.quantity = quantity;
    }
}
