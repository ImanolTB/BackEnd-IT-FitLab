package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "food")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "{msg.food.name.notEmpty}")
    @Size(max = 100, message = "{msg.food.name.size}")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotNull(message = "{msg.food.calories.notNull}")
    @Column(name = "calories")
    private Integer calories;

    @Column(name = "proteins", precision = 5, scale = 2)
    private BigDecimal proteins;

    @Column(name = "carbohydrates", precision = 5, scale = 2)
    private BigDecimal carbohydrates;

    @Column(name = "fats", precision = 5, scale = 2)
    private BigDecimal fats;

}
