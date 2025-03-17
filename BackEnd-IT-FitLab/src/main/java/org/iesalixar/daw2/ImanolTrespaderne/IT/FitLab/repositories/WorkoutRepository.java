package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories;

import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.Workout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {
    List<Workout> findByTrainingProgrammeId(Long trainingProgrammeId);
    List<Workout> findByTrainingProgrammeIdAndSessionNumber(Long trainingProgrammeId, Integer sessionNumber);
}
