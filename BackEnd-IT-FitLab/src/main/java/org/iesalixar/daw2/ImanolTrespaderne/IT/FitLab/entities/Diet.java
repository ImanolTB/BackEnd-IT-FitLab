package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "diets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Diet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "El nombre de la dieta no puede estar vacío.")
    @Size(max = 100, message = "El nombre de la dieta no puede superar los 100 caracteres.")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Size(max = 100, message = "La descripción de la dieta no puede superar los 100 caracteres.")
    @Column(name = "description", length = 100)
    private String description;

    @NotNull(message = "La duración de la dieta en semanas es obligatoria.")
    @Column(name = "duration_weeks",nullable = false)
    private Integer durationWeeks;

    @NotNull(message = "El usuario asociado a la dieta es obligatorio.")
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
