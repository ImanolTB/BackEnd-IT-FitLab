package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.services;

import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.ExerciseDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.Exercise;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.mappers.ExerciseMapper;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories.ExerciseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExerciseService {

    private static final Logger logger = LoggerFactory.getLogger(ExerciseService.class);

    @Autowired
    private ExerciseRepository exerciseRepository;
    @Autowired
    private ExerciseMapper exerciseMapper;

    public List<ExerciseDTO> getAllExercises() {
        logger.info("Solicitando todos los ejercicios.");
        return exerciseRepository.findAll().stream()
                .map(exerciseMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ExerciseDTO getExerciseById(Long id) {
        logger.info("Buscando ejercicio con ID: {}", id);
        return exerciseRepository.findById(id)
                .map(exerciseMapper::toDTO)
                .orElseThrow(() -> {
                    logger.warn("Ejercicio con ID {} no encontrado.", id);
                    return new IllegalArgumentException("Ejercicio no encontrado.");
                });
    }

    @Transactional
    public ExerciseDTO createExercise(ExerciseDTO dto) {
        logger.info("Creando nuevo ejercicio: {}", dto.getName());
        try {
            Exercise exercise = exerciseMapper.toEntity(dto);
            Exercise savedExercise = exerciseRepository.save(exercise);
            logger.info("Ejercicio creado con éxito: {}", savedExercise.getId());
            return exerciseMapper.toDTO(savedExercise);
        } catch (Exception e) {
            logger.error("Error al crear ejercicio: {}", e.getMessage());
            throw new RuntimeException("Error al crear el ejercicio.");
        }
    }

    @Transactional
    public ExerciseDTO updateExercise(Long id, ExerciseDTO dto) {
        logger.info("Actualizando ejercicio con ID: {}", id);
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Intento de actualizar un ejercicio inexistente con ID: {}", id);
                    return new IllegalArgumentException("Ejercicio no encontrado.");
                });

        try {
            exercise.setName(dto.getName());
            exercise.setVideoUrl(dto.getVideoUrl());
            exercise.setMuscleGroup(dto.getMuscleGroup());

            exercise = exerciseRepository.save(exercise);
            logger.info("Ejercicio actualizado con éxito: {}", id);
            return exerciseMapper.toDTO(exercise);
        } catch (Exception e) {
            logger.error("Error al actualizar ejercicio con ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al actualizar el ejercicio.");
        }
    }

    @Transactional
    public void deleteExercise(Long id) {
        logger.info("Intentando eliminar ejercicio con ID: {}", id);
        if (!exerciseRepository.existsById(id)) {
            logger.warn("Intento de eliminar un ejercicio inexistente con ID: {}", id);
            throw new IllegalArgumentException("Ejercicio no encontrado.");
        }

        try {
            exerciseRepository.deleteById(id);
            logger.info("Ejercicio con ID {} eliminado con éxito.", id);
        } catch (Exception e) {
            logger.error("Error al eliminar ejercicio con ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al eliminar el ejercicio.");
        }
    }
}
