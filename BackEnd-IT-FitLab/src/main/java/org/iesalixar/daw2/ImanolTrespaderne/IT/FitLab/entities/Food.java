package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "food")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "El nombre del alimento no puede estar vacío.")
    @Size(max = 150, message = "El nombre del alimento no puede superar los 150 caracteres.")
    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @NotNull(message = "El número de calorías es obligatorio.")
    @Column(name = "calories")
    private Integer calories;

    @NotNull(message = "Es obligatorio proporcionar las proteínas que aportan el alimento a los 100g.")
    @Column(name = "proteins", precision = 5, scale = 2)
    private BigDecimal proteins;

    @NotNull(message = "Es obligatorio proporcionar los hidratos de carbono que aportan el alimento a los 100g.")
    @Column(name = "carbohydrates", precision = 5, scale = 2)
    private BigDecimal carbohydrates;

    @NotNull(message = " Es obligatorio proporcionar las grasas que aportan el alimento a los 100g.")
    @Column(name = "fats", precision = 5, scale = 2)
    private BigDecimal fats;
}
