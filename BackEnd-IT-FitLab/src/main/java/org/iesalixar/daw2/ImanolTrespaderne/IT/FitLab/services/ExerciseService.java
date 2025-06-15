package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.services;

import jakarta.validation.Valid;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.ExerciseCreateDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.ExerciseDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.Exercise;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.mappers.ExerciseMapper;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories.ExerciseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExerciseService {

    // Logger para registrar eventos y errores relacionados con esta clase
    private static final Logger logger = LoggerFactory.getLogger(ExerciseService.class);

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private ExerciseMapper exerciseMapper;

    @Autowired
    private FileStorageService fileStorageService;

    /**
     * Obtiene todos los ejercicios registrados en la base de datos.
     * Devuelve una lista de objetos DTO.
     */
    public List<ExerciseDTO> getAllExercises() {
        logger.info("Solicitando todos los ejercicios.");
        return exerciseRepository.findAll().stream()
                .map(exerciseMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Devuelve un ejercicio específico por su ID.
     * Lanza una excepción si no se encuentra el ejercicio.
     */
    public ExerciseDTO getExerciseById(@PathVariable Long id) {
        logger.info("Buscando ejercicio con ID: {}", id);
        return exerciseRepository.findById(id)
                .map(exerciseMapper::toDTO)
                .orElseThrow(() -> {
                    logger.warn("Ejercicio con ID {} no encontrado.", id);
                    return new IllegalArgumentException("Ejercicio no encontrado.");
                });
    }

    /**
     * Crea un nuevo ejercicio a partir del DTO recibido.
     * Si se proporciona un archivo de video, se guarda antes de persistir el ejercicio.
     */
    @Transactional
    public ExerciseDTO createExercise(@Valid @RequestBody ExerciseCreateDTO dto) {
        logger.info("Creando nuevo ejercicio: {}", dto.getName());
        try {
            String videoURL = null;

            // Guarda el archivo de video si se ha proporcionado
            if (dto.getVideoUrl() != null && !dto.getVideoUrl().isEmpty()) {
                videoURL = fileStorageService.saveFile(dto.getVideoUrl());
            }

            // Si no se pudo guardar el archivo, lanza una excepción
            if (videoURL == null) {
                throw new RuntimeException("Error al guardar la imagen");
            }

            // Mapea y guarda la entidad Exercise
            Exercise exercise = exerciseMapper.toEntity(dto);
            exercise.setVideoUrl(videoURL);
            Exercise savedExercise = exerciseRepository.save(exercise);

            logger.info("Ejercicio creado con éxito: {}", savedExercise.getId());
            return exerciseMapper.toDTO(savedExercise);

        } catch (Exception e) {
            logger.error("Error al crear ejercicio: {}", e.getMessage());
            throw new RuntimeException("Error al crear el ejercicio.");
        }
    }

    /**
     * Actualiza los datos de un ejercicio existente.
     * Permite reemplazar el archivo de video y elimina el anterior si es necesario.
     */
    @Transactional
    public ExerciseDTO updateExercise(@PathVariable Long id, @Valid @ModelAttribute ExerciseCreateDTO dto) {
        logger.info("Actualizando ejercicio con ID: {}", id);

        // Se busca el ejercicio original
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Intento de actualizar un ejercicio inexistente con ID: {}", id);
                    return new IllegalArgumentException("Ejercicio no encontrado.");
                });

        // Manejo del archivo de video
        String oldVideoFile = exercise.getVideoUrl();
        String newVideoFile = oldVideoFile;

        if (dto.getVideoUrl() != null && !dto.getVideoUrl().isEmpty()) {
            newVideoFile = fileStorageService.saveFile(dto.getVideoUrl());

            if (newVideoFile == null) {
                throw new RuntimeException("Error al guardar el nuevo video.");
            }

            if (oldVideoFile != null && !oldVideoFile.equals(newVideoFile)) {
                fileStorageService.deleteFile(oldVideoFile);
            }
        }

        try {
            // Actualiza los campos modificables
            exercise.setName(dto.getName());
            exercise.setMuscleGroup(dto.getMuscleGroup());
            exercise.setVideoUrl(newVideoFile);

            exercise = exerciseRepository.save(exercise);

            logger.info("Ejercicio actualizado con éxito: {}", id);
            return exerciseMapper.toDTO(exercise);

        } catch (Exception e) {
            logger.error("Error al actualizar ejercicio con ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al actualizar el ejercicio.");
        }
    }

    /**
     * Elimina un ejercicio por su ID. También elimina el archivo de video asociado si existe.
     */
    @Transactional
    public void deleteExercise(@PathVariable Long id) {
        logger.info("Intentando eliminar ejercicio con ID: {}", id);

        if (!exerciseRepository.existsById(id)) {
            logger.warn("Intento de eliminar un ejercicio inexistente con ID: {}", id);
            throw new IllegalArgumentException("Ejercicio no encontrado.");
        }

        try {
            String filename = exerciseRepository.findById(id).get().getVideoUrl();
            fileStorageService.deleteFile(filename);
            exerciseRepository.deleteById(id);

            logger.info("Ejercicio con ID {} eliminado con éxito.", id);

        } catch (Exception e) {
            logger.error("Error al eliminar ejercicio con ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al eliminar el ejercicio.");
        }
    }
}
