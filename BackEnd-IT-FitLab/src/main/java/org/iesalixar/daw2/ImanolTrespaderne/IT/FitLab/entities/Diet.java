package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "diets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Diet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "{msg.diet.name.notEmpty}")
    @Size(max = 100, message = "{msg.diet.name.size}")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Size(max = 100, message = "{msg.diet.description.size}")
    @Column(name = "description", length = 100)
    private String description;

    @NotNull(message = "{msg.diet.durationWeeks.notNull}")
    @Column(name = "duration_weeks")
    private Integer durationWeeks;

    @NotNull(message = "{msg.diet.user.notNull}")
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

   ;
}
