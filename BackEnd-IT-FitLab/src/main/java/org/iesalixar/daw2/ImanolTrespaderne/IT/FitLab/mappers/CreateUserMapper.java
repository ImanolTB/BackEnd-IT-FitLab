package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.mappers;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.User;

import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.CreateUserDTO;
import org.springframework.stereotype.Component;

@Component
public class CreateUserMapper {

    public CreateUserDTO toDTO(User user) {
        CreateUserDTO dto = new CreateUserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setPassword(null);
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setLastName(user.getLastName());
        dto.setAge(user.getAge());
        dto.setEnabled(user.isEnabled());
        dto.setHeight(user.getHeight());
        dto.setWeight(user.getWeight());
        dto.setGender(user.getGender());
        dto.setActivityLevel(user.getActivityLevel());

        return dto;
    }

    public User toEntity(CreateUserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setLastName(dto.getLastName());
        user.setAge(dto.getAge());
        user.setHeight(dto.getHeight());
        user.setWeight(dto.getWeight());
        user.setEnabled(dto.isEnabled());
        user.setGender(dto.getGender());
        user.setActivityLevel(dto.getActivityLevel());
        return user;
    }
}
