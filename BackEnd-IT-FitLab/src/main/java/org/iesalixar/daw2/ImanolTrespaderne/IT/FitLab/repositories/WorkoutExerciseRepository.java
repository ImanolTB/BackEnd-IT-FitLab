package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories;

import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.WorkoutExercise;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.WorkoutExercisePK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExercise, WorkoutExercisePK> {
    List<WorkoutExercise> findByWorkoutId(Long workoutId);

}
