package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums.TrainingLevel;

@Entity
@Table(name = "training_programmes")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class TrainingProgramme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "El nombre del programa de entrenamiento no puede estar vacío.")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres.")
    @Column(nullable = false, length = 100)
    private String name;

    @NotNull(message = "La duración del programa de entrenamiento es obligatoria.")
    @Column(name = "duration_weeks", nullable = false)
    private Integer durationWeeks;


    @Column(name = "is_generic")
    private Boolean isGeneric;

    @Enumerated(EnumType.STRING)
    @Column(name = "training_level")
    private TrainingLevel trainingLevel;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
