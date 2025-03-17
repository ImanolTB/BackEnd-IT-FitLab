package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities;

import jakarta.persistence.*;
import lombok.*;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums.DayOfTheWeek;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums.MealType;

import java.math.BigDecimal;

@Entity
@Table(name = "workout_exercises")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutExercise {

    @EmbeddedId
    private WorkoutExercisePK id;

    @ManyToOne
    @MapsId("workoutId")
    @JoinColumn(name = "workout_id", nullable = false)
    private Workout workout;

    @ManyToOne
    @MapsId("exerciseId")
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @Column(nullable = false)
    private Integer sets;

    @Column(nullable = false)
    private Integer repetitions;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal weight;

    public WorkoutExercise(Workout workout, Exercise exercise, Integer sets, Integer repetitions, BigDecimal weight) {
        this.id = new WorkoutExercisePK(workout.getId(), exercise.getId());
        this.workout = workout;
        this.exercise = exercise;
        this.sets = sets;
        this.repetitions = repetitions;
        this.weight = weight;
    }
}
