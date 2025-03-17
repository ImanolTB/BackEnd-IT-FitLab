package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.mappers;

import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.WorkoutExerciseDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.*;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories.ExerciseRepository;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories.WorkoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WorkoutExerciseMapper {

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    public WorkoutExerciseDTO toDTO(WorkoutExercise workoutExercise) {
        WorkoutExerciseDTO dto = new WorkoutExerciseDTO();
        dto.setWorkoutId(workoutExercise.getWorkout().getId());
        dto.setExerciseId(workoutExercise.getExercise().getId());
        dto.setSets(workoutExercise.getSets());
        dto.setRepetitions(workoutExercise.getRepetitions());
        dto.setWeight(workoutExercise.getWeight());
        return dto;
    }

    public WorkoutExercise toEntity(WorkoutExerciseDTO dto) {
        Workout workout = workoutRepository.findById(dto.getWorkoutId())
                .orElseThrow(() -> new RuntimeException("Error: Entrenamiento no encontrado con ID " + dto.getWorkoutId()));

        Exercise exercise = exerciseRepository.findById(dto.getExerciseId())
                .orElseThrow(() -> new RuntimeException("Error: Ejercicio no encontrado con ID " + dto.getExerciseId()));

        WorkoutExercisePK id = new WorkoutExercisePK(dto.getWorkoutId(), dto.getExerciseId());

        return new WorkoutExercise(id, workout, exercise, dto.getSets(), dto.getRepetitions(), dto.getWeight());
    }
}
