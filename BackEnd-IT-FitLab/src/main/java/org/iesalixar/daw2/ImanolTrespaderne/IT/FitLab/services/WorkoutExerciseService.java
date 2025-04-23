package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.services;

import jakarta.validation.Valid;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.WorkoutExerciseDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.WorkoutExercise;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.WorkoutExercisePK;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.mappers.WorkoutExerciseMapper;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories.WorkoutExerciseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkoutExerciseService {

    private static final Logger logger = LoggerFactory.getLogger(WorkoutExerciseService.class);

    @Autowired
    private WorkoutExerciseRepository workoutExerciseRepository;
    @Autowired
    private WorkoutExerciseMapper workoutExerciseMapper;

    public List<WorkoutExerciseDTO> getAllWorkoutExercises() {
        logger.info("Solicitando todas las relaciones Workout-Exercise.");
        return workoutExerciseRepository.findAll().stream()
                .map(workoutExerciseMapper::toDTO)
                .collect(Collectors.toList());
    }

    public WorkoutExerciseDTO getWorkoutExerciseById(@PathVariable Long workoutId,@PathVariable Long exerciseId) {
        logger.info("Buscando relación Workout-Exercise con WorkoutID: {} y ExerciseID: {}", workoutId, exerciseId);
        WorkoutExercisePK id = new WorkoutExercisePK(workoutId, exerciseId);

        return workoutExerciseRepository.findById(id)
                .map(workoutExerciseMapper::toDTO)
                .orElseThrow(() -> {
                    logger.warn("Relación Workout-Exercise no encontrada.");
                    return new IllegalArgumentException("Workout-Exercise no encontrado.");
                });
    }
    public List<WorkoutExerciseDTO> getExercisesByWorkoutId(Long workoutId) {
        // Obtener la lista de relaciones para el workout dado
        List<WorkoutExercise> workoutExercises = workoutExerciseRepository.findByWorkoutId(workoutId);

        if (workoutExercises == null || workoutExercises.isEmpty()) {
            throw new IllegalArgumentException("No se encontraron ejercicios para el workout con ID: " + workoutId);
        }

        // Convertir cada entidad a DTO usando el mapper manual
        return workoutExercises.stream()
                .map(workoutExerciseMapper::toDTO)
                .collect(Collectors.toList());
    }
    @Transactional
    public WorkoutExerciseDTO createWorkoutExercise(@Valid @RequestBody WorkoutExerciseDTO dto) {
        logger.info("Creando nueva relación Workout-Exercise: WorkoutID: {}, ExerciseID: {}", dto.getWorkoutId(), dto.getExerciseId());
        try {
            WorkoutExercise workoutExercise = workoutExerciseMapper.toEntity(dto);
            WorkoutExercise savedWorkoutExercise = workoutExerciseRepository.save(workoutExercise);
            logger.info("Workout-Exercise creado con éxito.");
            return workoutExerciseMapper.toDTO(savedWorkoutExercise);
        } catch (Exception e) {
            logger.error("Error al crear Workout-Exercise: {}", e.getMessage());
            throw new RuntimeException("Error al crear Workout-Exercise.");
        }
    }

    @Transactional
    public WorkoutExerciseDTO updateWorkoutExercise(@PathVariable Long workoutId,@PathVariable Long exerciseId,@Valid @RequestBody WorkoutExerciseDTO dto) {
        logger.info("Actualizando relación Workout-Exercise: WorkoutID: {}, ExerciseID: {}", workoutId, exerciseId);
        WorkoutExercisePK id = new WorkoutExercisePK(workoutId, exerciseId);

        WorkoutExercise workoutExercise = workoutExerciseRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Intento de actualizar una relación Workout-Exercise inexistente.");
                    return new IllegalArgumentException("Workout-Exercise no encontrado.");
                });

        try {
            workoutExercise.setSets(dto.getSets());
            workoutExercise.setRepetitions(dto.getRepetitions());
            workoutExercise.setWeight(dto.getWeight());
            workoutExercise = workoutExerciseRepository.save(workoutExercise);
            logger.info("Workout-Exercise actualizado con éxito.");
            return workoutExerciseMapper.toDTO(workoutExercise);
        } catch (Exception e) {
            logger.error("Error al actualizar Workout-Exercise: {}", e.getMessage());
            throw new RuntimeException("Error al actualizar Workout-Exercise.");
        }
    }

    @Transactional
    public void deleteWorkoutExercise(@PathVariable Long workoutId,@PathVariable Long exerciseId) {
        logger.info("Intentando eliminar relación Workout-Exercise: WorkoutID: {}, ExerciseID: {}", workoutId, exerciseId);
        WorkoutExercisePK id = new WorkoutExercisePK(workoutId, exerciseId);

        if (!workoutExerciseRepository.existsById(id)) {
            logger.warn("Intento de eliminar una relación Workout-Exercise inexistente.");
            throw new IllegalArgumentException("Workout-Exercise no encontrado.");
        }

        try {
            workoutExerciseRepository.deleteById(id);
            logger.info("Workout-Exercise eliminado con éxito.");
        } catch (Exception e) {
            logger.error("Error al eliminar Workout-Exercise: {}", e.getMessage());
            throw new RuntimeException("Error al eliminar Workout-Exercise.");
        }
    }
}
