package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "workouts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "exercises")
@EqualsAndHashCode(exclude = "exercises")
public class Workout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "El nombre del entrenamiento no puede estar vacío.")
    @Size(max = 100, message = "El nombre del entrenamiento no puede superar los 100 caracteres.")
    @Column(nullable = false, length = 100)
    private String name;

    @Size(max = 255, message = "La descripción no puede superar los 255 caracteres.")
    @Column(length = 255)
    private String description;

    @NotNull(message = "El número de sesión es obligatorio.")
    @Column(nullable = false)
    private Integer sessionNumber;

    @NotNull(message = "El entrenamiento debe pertenecer a un programa de entrenamiento.")
    @ManyToOne
    @JoinColumn(name = "training_program_id", nullable = false)
    private TrainingProgramme trainingProgramme;

}
