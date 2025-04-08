package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories;

import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.WorkoutExercise;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.WorkoutExercisePK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExercise, WorkoutExercisePK> {
}
