package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.mappers;

import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.TrainingProgrammeDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.TrainingProgramme;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.User;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrainingProgrammeMapper {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;



        public TrainingProgrammeDTO toDTO(TrainingProgramme programme) {
            TrainingProgrammeDTO dto = new TrainingProgrammeDTO();
            dto.setId(programme.getId());
            dto.setName(programme.getName());
            dto.setDurationWeeks(programme.getDurationWeeks());
            dto.setIsGeneric(programme.getIsGeneric());
            dto.setTrainingLevel(programme.getTrainingLevel());
            dto.setUser(userMapper.toDTO(programme.getUser()));
            return dto;
        }

        public TrainingProgramme toEntity(TrainingProgrammeDTO dto) {
            User user = userRepository.findById(dto.getUser().getId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID " + dto.getUser().getId()));

            TrainingProgramme programme = new TrainingProgramme();
            programme.setId(dto.getId());
            programme.setName(dto.getName());
            programme.setDurationWeeks(dto.getDurationWeeks());
            programme.setIsGeneric(dto.getIsGeneric());
            programme.setTrainingLevel(dto.getTrainingLevel());
            programme.setUser(user);
            return programme;
        }
    }