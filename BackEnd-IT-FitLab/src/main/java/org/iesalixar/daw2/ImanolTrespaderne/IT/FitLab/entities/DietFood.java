package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities;

import jakarta.persistence.*;
import lombok.*;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums.DayOfTheWeek;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums.MealType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.DayOfWeek;

@Entity
@Table(name = "diet_food")
@IdClass(DietFoodPK.class) // 📌 Definimos la clave primaria compuesta sin `@Embeddable`
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class DietFood {

    @Id
    @ManyToOne
    @JoinColumn(name = "diet_id", nullable = false)
    private Diet diet;

    @Id
    @ManyToOne
    @JoinColumn(name = "food_id", nullable = false)
    private Food food;

    @Id
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private DayOfTheWeek dayWeek;

    @Id
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private MealType mealType;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal quantity;
}
