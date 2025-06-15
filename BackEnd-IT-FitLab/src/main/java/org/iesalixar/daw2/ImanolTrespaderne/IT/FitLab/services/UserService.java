package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.services;

import jakarta.validation.Valid;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.CreateUserDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.UpdateUserDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.UserDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.UserTDEEDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.Role;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.User;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.mappers.CreateUserMapper;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.mappers.UserMapper;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories.RoleRepository;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CreateUserMapper createUserMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Devuelve todos los usuarios en forma de lista de DTOs para edición.
     */
    public List<UpdateUserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUpdateDTO)
                .collect(Collectors.toList());
    }

    /**
     * Devuelve un usuario por su ID en formato de DTO para edición.
     */
    public UpdateUserDTO getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(userMapper::toUpdateDTO)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }

    /**
     * Devuelve un usuario completo a partir de su nombre de usuario.
     */
    public UserDTO getUserByUsername(@PathVariable String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con username: " + username));
        return userMapper.toDTO(user);
    }

    /**
     * Comprueba si un nombre de usuario ya está registrado.
     */
    public boolean isUsernameTaken(@PathVariable String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío.");
        }
        return userRepository.findByUsername(username).isPresent();
    }

    /**
     * Comprueba si un email ya está registrado.
     */
    public boolean isEmailTaken(@PathVariable String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede estar vacío.");
        }
        return userRepository.findByEmail(email).isPresent();
    }

    /**
     * Crea un nuevo usuario con rol estándar.
     * Encripta la contraseña y activa la cuenta por defecto.
     */
    public CreateUserDTO createUser(@Valid @RequestBody CreateUserDTO dto) {
        User user = createUserMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEnabled(true);

        Role role = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("El rol USER no fue encontrado"));

        user.setRoles(Collections.singleton(role));
        return createUserMapper.toDTO(userRepository.save(user));
    }

    /**
     * Crea un nuevo usuario con rol de administrador.
     */
    public CreateUserDTO createAdmin(@Valid @RequestBody CreateUserDTO dto) {
        User user = createUserMapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEnabled(true);

        Role role = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("El rol ADMIN no fue encontrado"));

        user.setRoles(Collections.singleton(role));
        return createUserMapper.toDTO(userRepository.save(user));
    }

    /**
     * Devuelve un DTO con los datos necesarios para calcular el TDEE (gasto energético diario).
     */
    public UserTDEEDTO getTDEEData(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return userMapper.toTDEEDTO(user);
    }

    /**
     * Actualiza los datos de un usuario a partir del ID y nuevo DTO recibido.
     * Valida edad positiva.
     */
    public CreateUserDTO updateUser(@PathVariable Long id, CreateUserDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        user.setName(dto.getName());
        user.setLastName(dto.getLastName());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setHeight(dto.getHeight());
        user.setWeight(dto.getWeight());

        if (dto.getAge() < 1) {
            throw new IllegalArgumentException("La edad debe ser mayor de 0");
        } else {
            user.setAge(dto.getAge());
        }

        user.setActivityLevel(dto.getActivityLevel());
        return createUserMapper.toDTO(userRepository.save(user));
    }

    /**
     * Elimina un usuario por su ID.
     */
    public void deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        userRepository.deleteById(id);
    }

    /**
     * Desactiva la cuenta de un usuario (no se elimina).
     */
    public void deactivateUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        user.setEnabled(false);
        userRepository.save(user);
    }

    /**
     * Reactiva una cuenta desactivada, a partir del email del usuario.
     */
    public void reactivateUserByEmail(@PathVariable String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ese email"));

        if (user.isEnabled()) {
            throw new IllegalStateException("La cuenta ya está activa.");
        }

        user.setEnabled(true);
        userRepository.save(user);
    }

    /**
     * Valida si un usuario tiene permiso para modificar su propia cuenta o si es admin.
     */
    public void validateUserOwnership(Long userId, String username) {
        if (isAdmin(username)) return;

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (!user.getUsername().equals(username) && !isAdmin(username)) {
            throw new SecurityException("No tienes permiso para realizar esta acción.");
        }
    }

    /**
     * Verifica si un usuario es administrador.
     */
    public boolean isAdmin(String username) {
        return userRepository.findByUsername(username)
                .map(user -> user.getRoles().stream()
                        .anyMatch(role -> role.getName().equals("ROLE_ADMIN")))
                .orElse(false);
    }

    /**
     * Devuelve los datos de un usuario a partir de su email.
     */
    public UpdateUserDTO getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toUpdateDTO)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }
}
