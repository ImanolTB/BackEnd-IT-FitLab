package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.WorkoutExerciseDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.Exercise;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.Workout;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.WorkoutExercise;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.WorkoutExercisePK;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.mappers.WorkoutExerciseMapper;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories.ExerciseRepository;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories.WorkoutExerciseRepository;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories.WorkoutRepository;
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

    // Logger para registrar eventos y errores relevantes
    private static final Logger logger = LoggerFactory.getLogger(WorkoutExerciseService.class);

    @Autowired
    private WorkoutExerciseRepository workoutExerciseRepository;

    @Autowired
    private WorkoutExerciseMapper workoutExerciseMapper;

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    /**
     * Devuelve todas las relaciones entre entrenamientos y ejercicios.
     */
    public List<WorkoutExerciseDTO> getAllWorkoutExercises() {
        logger.info("Solicitando todas las relaciones Workout-Exercise.");
        return workoutExerciseRepository.findAll().stream()
                .map(workoutExerciseMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Devuelve una relación específica entre un entrenamiento y un ejercicio,
     * utilizando como identificador compuesto el ID del entrenamiento y del ejercicio.
     */
    public WorkoutExerciseDTO getWorkoutExerciseById(@PathVariable Long workoutId, @PathVariable Long exerciseId) {
        logger.info("Buscando relación Workout-Exercise con WorkoutID: {} y ExerciseID: {}", workoutId, exerciseId);
        WorkoutExercisePK id = new WorkoutExercisePK(workoutId, exerciseId);

        return workoutExerciseRepository.findById(id)
                .map(workoutExerciseMapper::toDTO)
                .orElseThrow(() -> {
                    logger.warn("Relación Workout-Exercise no encontrada.");
                    return new IllegalArgumentException("Workout-Exercise no encontrado.");
                });
    }

    /**
     * Devuelve todas las relaciones de ejercicios pertenecientes a un entrenamiento concreto.
     * Si no existe el entrenamiento, lanza una excepción.
     */
    public List<WorkoutExerciseDTO> getExercisesByWorkoutId(Long workoutId) {
        if (!workoutRepository.existsById(workoutId)) {
            throw new EntityNotFoundException("Workout con ID " + workoutId + " no existe.");
        }

        List<WorkoutExercise> workoutExercises = workoutExerciseRepository.findByWorkoutId(workoutId);

        return workoutExercises.stream()
                .map(workoutExerciseMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Crea una nueva relación entre un entrenamiento y un ejercicio.
     * Los datos deben estar correctamente validados y deben existir las entidades referenciadas.
     */
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

    /**
     * Actualiza los detalles de una relación existente (series, repeticiones, peso).
     * Si no se encuentra la relación, lanza una excepción.
     */
    @Transactional
    public WorkoutExerciseDTO updateWorkoutExercise(@PathVariable Long workoutId, @PathVariable Long exerciseId,
                                                    @Valid @RequestBody WorkoutExerciseDTO dto) {
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

    /**
     * Elimina una relación entre un entrenamiento y un ejercicio, identificada por sus IDs.
     * Si no existe, lanza una excepción.
     */
    @Transactional
    public void deleteWorkoutExercise(@PathVariable Long workoutId, @PathVariable Long exerciseId) {
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
