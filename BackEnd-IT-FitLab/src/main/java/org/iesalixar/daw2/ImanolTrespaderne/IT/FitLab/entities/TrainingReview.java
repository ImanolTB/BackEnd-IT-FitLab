package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "training_reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Clave primaria autogenerada

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "training_programme_id")
    private TrainingProgramme trainingProgramme;

    @Column(nullable = false)
    private Integer score;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(name = "date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime date = LocalDateTime.now();
}
