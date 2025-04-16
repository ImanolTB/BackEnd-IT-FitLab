package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.services;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.TrainingProgrammeDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.TrainingProgramme;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.mappers.TrainingProgrammeMapper;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories.TrainingProgrammeRepository;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories.UserRepository;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Comparator;
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

    public TrainingProgrammeDTO getTrainingProgrammeById(@PathVariable Long id) {
        logger.info("Buscando programa de entrenamiento con ID: {}", id);
        return programmeRepository.findById(id)
                .map(programmeMapper::toDTO)
                .orElseThrow(() -> {
                    logger.warn("Programa de entrenamiento con ID {} no encontrado.", id);
                    return new IllegalArgumentException("Programa de entrenamiento no encontrado.");
                });
    }
    public List<TrainingProgrammeDTO> getGenericTrainingProgrammesSortedByTrainingLevel() {
        logger.info("Listando programas de entrenamiento genéricos organizados por trainingLevel...");
        try {
            return programmeRepository.findAll()
                    .stream()
                    // Filtra para obtener solo aquellos que son genéricos
                    .filter(programme -> Boolean.TRUE.equals(programme.getIsGeneric()))
                    // Ordena según el valor del enum TrainingLevel (puede usar su orden natural)
                    .sorted(Comparator.comparing(TrainingProgramme::getTrainingLevel))
                    // Convierte la entidad a DTO
                    .map(programmeMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error al listar programas de entrenamiento genéricos: {}", e.getMessage());
            throw new RuntimeException("Error al listar programas de entrenamiento genéricos.");
        }
    }
    public List<TrainingProgrammeDTO> getTrainingProgrammesByUserId(@PathVariable Long userId) {
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
    public TrainingProgrammeDTO createTrainingProgramme(@Valid @RequestBody TrainingProgrammeDTO dto) {
        logger.info("Creando nuevo programa de entrenamiento: {}", dto.getName());
        if (dto.getUser().getId() == null || !userRepository.existsById(dto.getUser().getId())) {
            logger.warn("Intento de creación con usuario inválido: {}", dto.getUser().getId());
            throw new IllegalArgumentException("Usuario no encontrado para asociar al programa.");
        }
        try {
            TrainingProgramme programme = programmeMapper.toEntity(dto);
            if (dto.getIsGeneric() == null) {
                dto.setIsGeneric(false);
            }
            programme = programmeRepository.save(programme);
            logger.info("Programa de entrenamiento creado con éxito: {}", programme.getId());
            return programmeMapper.toDTO(programme);
        } catch (Exception e) {
            logger.error("Error al crear programa de entrenamiento: {}", e.getMessage());
            throw new RuntimeException("Error interno al crear programa de entrenamiento.");
        }
    }

    @Transactional
    public TrainingProgrammeDTO updateTrainingProgramme(@PathVariable Long id,@Valid @RequestBody TrainingProgrammeDTO dto) {
        logger.info("Actualizando programa de entrenamiento con ID: {}", id);
        TrainingProgramme programme = programmeRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Intento de actualizar un programa inexistente con ID: {}", id);
                    return new IllegalArgumentException("Programa de entrenamiento no encontrado.");
                });

        try {
            programme.setName(dto.getName());
            programme.setDurationWeeks(dto.getDurationWeeks());
            programme.setIsGeneric(dto.getIsGeneric());
            if (programme.getIsGeneric() == null) {
                programme.setIsGeneric(false);
            }
            programme.setTrainingLevel(dto.getTrainingLevel());


            programme = programmeRepository.save(programme);
            logger.info("Programa de entrenamiento actualizado con éxito: {}", id);
            return programmeMapper.toDTO(programme);
        } catch (Exception e) {
            logger.error("Error al actualizar programa de entrenamiento con ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error interno al actualizar programa de entrenamiento.");
        }
    }

    @Transactional
    public void deleteTrainingProgramme(@PathVariable Long id) {
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

    public void validateOwnership(  Long programmeId, String username) {
        if (isAdmin(username)) return;

        TrainingProgramme programme = programmeRepository.findById(programmeId)
                .orElseThrow(() -> new IllegalArgumentException("Programa de entrenamiento no encontrado"));

        if (!programme.getUser().getUsername().equals(username)) {
            throw new SecurityException("No tienes permiso para modificar este programa de entrenamiento.");
        }
    }
    public boolean isAdmin(String username) {
        return userRepository.findByUsername(username)
                .map(user -> user.getRoles().stream()
                        .anyMatch(role -> role.getName().equals("ROLE_ADMIN")))
                .orElse(false);
    }
}
