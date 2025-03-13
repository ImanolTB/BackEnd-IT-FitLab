package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.mappers;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.User;

import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.UserDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums.ActivityLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setPassword(null);
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setLastName(user.getLastName());
        dto.setAge(user.getAge());
        dto.setHeight(user.getHeight());
        dto.setWeight(user.getWeight());
        dto.setGender(user.getGender());
        dto.setActivityLevel(user.getActivityLevel());
        return dto;
    }

    public User toEntity(UserDTO dto) {
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
        user.setGender(dto.getGender());
        user.setActivityLevel(dto.getActivityLevel());
        return user;
    }
}
