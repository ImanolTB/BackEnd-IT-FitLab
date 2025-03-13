package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.mappers;

import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.DietDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.Diet;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class DietMapper {

    private final UserRepository userRepository;

    public DietMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public DietDTO toDTO(Diet diet) {
        DietDTO dto = new DietDTO();
        dto.setId(diet.getId());
        dto.setName(diet.getName());
        dto.setDescription(diet.getDescription());
        dto.setDurationWeeks(diet.getDurationWeeks());
        dto.setUserId(diet.getUser().getId());
        return dto;
    }

    public Diet toEntity(DietDTO dto) {
        Diet diet = new Diet();
        diet.setId(dto.getId());
        diet.setName(dto.getName());
        diet.setDescription(dto.getDescription());
        diet.setDurationWeeks(dto.getDurationWeeks());
        diet.setUser(userRepository.findById(dto.getUserId()).orElseThrow());
        return diet;
    }
}
