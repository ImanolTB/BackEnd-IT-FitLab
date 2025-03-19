package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.services;

import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.UserDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.Role;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.User;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.mappers.UserMapper;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories.RoleRepository;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  UserMapper userMapper;
    @Autowired
    private RoleRepository roleRepository;
   @Autowired
   private PasswordEncoder passwordEncoder;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Ussuario no encontrado"));
    }

    public UserDTO createUser(UserDTO dto) {
        User user = userMapper.toEntity(dto);
        //Encripar aqui la contraseña
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEnabled(true);
        // Buscar el rol "USER" y asignarlo a la persona
        Role roles = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("El rol USER no fue encontrado"));

        // Asignar el rol al usuario
        user.setRoles(Collections.singleton(roles));
        return userMapper.toDTO(userRepository.save(user));
    }

    public UserDTO createAdmin(UserDTO dto) {
        User user = userMapper.toEntity(dto);
        //Encripar aqui la contraseña
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEnabled(true);
        // Buscar el rol "USER" y asignarlo a la persona
        Role roles = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("El rol ADMIN no fue encontrado"));

        // Asignar el rol al usuario
        user.setRoles(Collections.singleton(roles));
        return userMapper.toDTO(userRepository.save(user));
    }

    public UserDTO updateUser(Long id, UserDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        user.setName(dto.getName());
        user.setLastName(dto.getLastName());
        user.setAge(dto.getAge());
        user.setHeight(dto.getHeight());
        user.setWeight(dto.getWeight());
        user.setGender(dto.getGender());
        user.setActivityLevel(dto.getActivityLevel());

        return userMapper.toDTO(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Persona no encontrada");
        }
        userRepository.deleteById(id);
    }
}
