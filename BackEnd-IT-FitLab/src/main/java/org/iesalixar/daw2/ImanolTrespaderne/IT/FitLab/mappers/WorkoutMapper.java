package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.mappers;

import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.WorkoutDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.TrainingProgramme;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.Workout;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories.TrainingProgrammeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WorkoutMapper {

    @Autowired
    private TrainingProgrammeRepository trainingProgrammeRepository;
@Autowired
private TrainingProgrammeMapper trainingProgrammeMapper;
    public WorkoutDTO toDTO(Workout workout) {
        WorkoutDTO dto = new WorkoutDTO();
        dto.setId(workout.getId());
        dto.setName(workout.getName());
        dto.setDescription(workout.getDescription());
        dto.setTrainingProgramme(trainingProgrammeMapper.toDTO(workout.getTrainingProgramme()) );
        dto.setSessionNumber(workout.getSessionNumber());
        return dto;
    }

    public Workout toEntity(WorkoutDTO dto) {
        TrainingProgramme trainingProgramme = trainingProgrammeRepository.findById(dto.getTrainingProgramme().getId())
                .orElseThrow(() -> new RuntimeException("Programa de entrenamiento no encontrado"));

        Workout workout = new Workout();
        workout.setId(dto.getId());
        workout.setName(dto.getName());
        workout.setDescription(dto.getDescription());
        workout.setTrainingProgramme(trainingProgramme);
        workout.setSessionNumber(dto.getSessionNumber());
        return workout;
    }
}
