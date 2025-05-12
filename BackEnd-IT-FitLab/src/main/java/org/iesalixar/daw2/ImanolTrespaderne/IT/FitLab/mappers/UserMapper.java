package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.mappers;

import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.UpdateUserDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.UserDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.UserTDEEDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        return dto;
    }
    public User toEntity(UserDTO dto) {
        if (dto == null) return null;

        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        return user;
    }
    public UserTDEEDTO toTDEEDTO(User user) {
        return new UserTDEEDTO(
                user.getWeight(),
                user.getHeight(),
                user.getAge(),
                user.getGender(),
                user.getActivityLevel()
        );
    }
    public UpdateUserDTO toUpdateDTO(User user){
        UpdateUserDTO dto= new UpdateUserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setHeight(user.getHeight());
        dto.setWeight(user.getWeight());
        dto.setEnabled(user.isEnabled());
        dto.setAge(user.getAge());
        dto.setActivityLevel(user.getActivityLevel());

        return dto;
    }
}
