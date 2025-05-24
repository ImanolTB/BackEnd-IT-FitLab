package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

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

    @Column(columnDefinition = "TEXT", nullable = true,length = 100)

    private String comment;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Madrid")
    private ZonedDateTime date;
}
