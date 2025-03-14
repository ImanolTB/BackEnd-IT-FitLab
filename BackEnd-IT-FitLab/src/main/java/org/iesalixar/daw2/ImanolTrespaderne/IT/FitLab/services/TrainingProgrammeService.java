package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.services;

import jakarta.transaction.Transactional;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.TrainingProgrammeDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.TrainingProgramme;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.mappers.TrainingProgrammeMapper;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories.TrainingProgrammeRepository;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainingProgrammeService {

    private static final Logger logger = LoggerFactory.getLogger(TrainingProgrammeService.class);

    @Autowired
    private TrainingProgrammeRepository programmeRepository;

    @Autowired
    private TrainingProgrammeMapper programmeMapper;

    @Autowired
    private UserRepository userRepository;

    public List<TrainingProgrammeDTO> getAllTrainingProgrammes() {
        logger.info("Solicitando lista de todos los programas de entrenamiento...");
        try {
            List<TrainingProgrammeDTO> programmes = programmeRepository.findAll()
                    .stream()
                    .map(programmeMapper::toDTO)
                    .collect(Collectors.toList());
            logger.info("Se encontraron {} programas de entrenamiento.", programmes.size());
            return programmes;
        } catch (Exception e) {
            logger.error("Error al obtener programas de entrenamiento: {}", e.getMessage());
            throw new RuntimeException("Error interno al obtener programas de entrenamiento.");
        }
    }

    public TrainingProgrammeDTO getTrainingProgrammeById(Long id) {
        logger.info("Buscando programa de entrenamiento con ID: {}", id);
        return programmeRepository.findById(id)
                .map(programmeMapper::toDTO)
                .orElseThrow(() -> {
                    logger.warn("Programa de entrenamiento con ID {} no encontrado.", id);
                    return new IllegalArgumentException("Programa de entrenamiento no encontrado.");
                });
    }

    public List<TrainingProgrammeDTO> getTrainingProgrammesByUserId(Long userId) {
        logger.info("Buscando programas de entrenamiento para el usuario con ID: {}", userId);
        if (!userRepository.existsById(userId)) {
            logger.warn("Usuario con ID {} no encontrado.", userId);
            throw new IllegalArgumentException("Usuario no encontrado.");
        }
        try {
            List<TrainingProgrammeDTO> programmes = programmeRepository.findByUserId(userId)
                    .stream()
                    .map(programmeMapper::toDTO)
                    .collect(Collectors.toList());
            return programmes;
        } catch (Exception e) {
            logger.error("Error al obtener programas de entrenamiento del usuario con ID {}: {}", userId, e.getMessage());
            throw new RuntimeException("Error interno al obtener programas de entrenamiento del usuario.");
        }
    }

    @Transactional
    public TrainingProgrammeDTO createTrainingProgramme(TrainingProgrammeDTO dto) {
        logger.info("Creando nuevo programa de entrenamiento: {}", dto.getName());
        if (dto.getUserId() == null || !userRepository.existsById(dto.getUserId())) {
            logger.warn("Intento de creación con usuario inválido: {}", dto.getUserId());
            throw new IllegalArgumentException("Usuario no encontrado para asociar al programa.");
        }
        try {
            TrainingProgramme programme = programmeMapper.toEntity(dto);
            programme = programmeRepository.save(programme);
            logger.info("Programa de entrenamiento creado con éxito: {}", programme.getId());
            return programmeMapper.toDTO(programme);
        } catch (Exception e) {
            logger.error("Error al crear programa de entrenamiento: {}", e.getMessage());
            throw new RuntimeException("Error interno al crear programa de entrenamiento.");
        }
    }

    @Transactional
    public TrainingProgrammeDTO updateTrainingProgramme(Long id, TrainingProgrammeDTO dto) {
        logger.info("Actualizando programa de entrenamiento con ID: {}", id);
        TrainingProgramme programme = programmeRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Intento de actualizar un programa inexistente con ID: {}", id);
                    return new IllegalArgumentException("Programa de entrenamiento no encontrado.");
                });

        try {
            programme.setName(dto.getName());
            programme.setDurationWeeks(dto.getDurationWeeks());

            programme = programmeRepository.save(programme);
            logger.info("Programa de entrenamiento actualizado con éxito: {}", id);
            return programmeMapper.toDTO(programme);
        } catch (Exception e) {
            logger.error("Error al actualizar programa de entrenamiento con ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error interno al actualizar programa de entrenamiento.");
        }
    }

    @Transactional
    public void deleteTrainingProgramme(Long id) {
        logger.info("Intentando eliminar programa de entrenamiento con ID: {}", id);
        if (!programmeRepository.existsById(id)) {
            logger.warn("No se encontró el programa de entrenamiento con ID: {}", id);
            throw new IllegalArgumentException("Programa de entrenamiento no encontrado.");
        }
        try {
            programmeRepository.deleteById(id);
            logger.info("Programa de entrenamiento eliminado con éxito: {}", id);
        } catch (Exception e) {
            logger.error("Error al eliminar programa de entrenamiento con ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error interno al eliminar programa de entrenamiento.");
        }
    }
}
