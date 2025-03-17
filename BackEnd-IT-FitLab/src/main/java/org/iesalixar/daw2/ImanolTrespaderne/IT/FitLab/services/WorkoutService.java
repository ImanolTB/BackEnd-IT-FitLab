package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.services;

import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.WorkoutDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.TrainingProgramme;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.Workout;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.mappers.WorkoutMapper;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories.TrainingProgrammeRepository;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories.WorkoutRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkoutService {

    private static final Logger logger = LoggerFactory.getLogger(WorkoutService.class);

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private WorkoutMapper workoutMapper;

    @Autowired
    private TrainingProgrammeRepository trainingProgrammeRepository;

    public List<WorkoutDTO> getAllWorkouts() {
        logger.info("Obteniendo todos los workouts.");
        return workoutRepository.findAll().stream()
                .map(workoutMapper::toDTO)
                .collect(Collectors.toList());
    }

    public WorkoutDTO getWorkoutById(Long id) {
        logger.info("Buscando workout con ID: {}", id);
        return workoutRepository.findById(id)
                .map(workoutMapper::toDTO)
                .orElseThrow(() -> {
                    logger.warn("Workout con ID {} no encontrado.", id);
                    return new IllegalArgumentException("Workout no encontrado.");
                });
    }

    @Transactional
    public WorkoutDTO createWorkout(WorkoutDTO dto) {
        logger.info("Creando nuevo workout con nombre: {}", dto.getName());
        TrainingProgramme programme = trainingProgrammeRepository.findById(dto.getTrainingProgramme())
                .orElseThrow(() -> {
                    logger.warn("Programa de entrenamiento con ID {} no encontrado.", dto.getTrainingProgramme());
                    return new IllegalArgumentException("Programa de entrenamiento no encontrado.");
                });

        try {
            Workout workout = workoutMapper.toEntity(dto);
            Workout savedWorkout = workoutRepository.save(workout);
            logger.info("Workout creado con éxito.");
            return workoutMapper.toDTO(savedWorkout);
        } catch (Exception e) {
            logger.error("Error al crear workout: {}", e.getMessage());
            throw new RuntimeException("Error interno al crear workout.");
        }
    }

    @Transactional
    public WorkoutDTO updateWorkout(Long id, WorkoutDTO dto) {
        logger.info("Actualizando workout con ID: {}", id);
        Workout workout = workoutRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Intento de actualizar workout inexistente con ID: {}", id);
                    return new IllegalArgumentException("Workout no encontrado.");
                });

        try {
            workout.setName(dto.getName());
            workout.setDescription(dto.getDescription());
            workout.setSessionNumber(dto.getSessionNumber());
            workout = workoutRepository.save(workout);
            logger.info("Workout actualizado con éxito.");
            return workoutMapper.toDTO(workout);
        } catch (Exception e) {
            logger.error("Error al actualizar workout con ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error interno al actualizar workout.");
        }
    }

    @Transactional
    public void deleteWorkout(Long id) {
        logger.info("Intentando eliminar workout con ID: {}", id);
        if (!workoutRepository.existsById(id)) {
            logger.warn("Intento de eliminar workout inexistente con ID: {}", id);
            throw new IllegalArgumentException("Workout no encontrado.");
        }

        try {
            workoutRepository.deleteById(id);
            logger.info("Workout eliminado con éxito.");
        } catch (Exception e) {
            logger.error("Error al eliminar workout con ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error interno al eliminar workout.");
        }
    }
}
