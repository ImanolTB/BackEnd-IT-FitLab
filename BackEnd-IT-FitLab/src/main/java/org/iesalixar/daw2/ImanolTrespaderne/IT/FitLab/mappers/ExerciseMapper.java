package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.mappers;

import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.ExerciseCreateDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.ExerciseDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.Exercise;
import org.springframework.stereotype.Component;

@Component
public class ExerciseMapper {

    public ExerciseDTO toDTO(Exercise exercise) {
        ExerciseDTO dto = new ExerciseDTO();
        dto.setId(exercise.getId());
        dto.setName(exercise.getName());
        dto.setVideoUrl(exercise.getVideoUrl());
        dto.setMuscleGroup(exercise.getMuscleGroup());
        return dto;
    }

    public Exercise toEntity(ExerciseDTO dto) {
        Exercise exercise = new Exercise();
        exercise.setId(dto.getId());
        exercise.setName(dto.getName());
        exercise.setVideoUrl(dto.getVideoUrl());
        exercise.setMuscleGroup(dto.getMuscleGroup());
        return exercise;
    }

    public Exercise toEntity(ExerciseCreateDTO dto) {
        Exercise exercise = new Exercise();
        exercise.setId(dto.getId());
        exercise.setName(dto.getName());
        exercise.setMuscleGroup(dto.getMuscleGroup());
        return exercise;
    }
}
