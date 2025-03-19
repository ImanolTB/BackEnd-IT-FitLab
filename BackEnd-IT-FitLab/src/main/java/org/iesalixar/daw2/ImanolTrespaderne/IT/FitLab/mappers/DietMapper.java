package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.mappers;

import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.DietDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.Diet;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DietMapper {

    @Autowired
    private UserMapper userMapper;



    public DietDTO toDTO(Diet diet) {
        if (diet == null) {
            return null;
        }

        DietDTO dto = new DietDTO();
        dto.setId(diet.getId());
        dto.setName(diet.getName());
        dto.setDescription(diet.getDescription());
        dto.setDurationWeeks(diet.getDurationWeeks());
        if (diet.getUser() != null) {
            dto.setUser(userMapper.toDTO(diet.getUser())); // Convertimos User a UserDTO
        }
        return dto;
    }

    public Diet toEntity(DietDTO dto) {
        if (dto == null) {
            return null;
        }

        Diet diet = new Diet();
        diet.setId(dto.getId());
        diet.setName(dto.getName());
        diet.setDescription(dto.getDescription());
        diet.setDurationWeeks(dto.getDurationWeeks());

        if (dto.getUser() != null) {
            diet.setUser(userMapper.toEntity(dto.getUser())); // Convertimos UserDTO a User
        }


        return diet;
    }

}
