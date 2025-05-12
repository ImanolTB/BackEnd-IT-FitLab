package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.controllers;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.hibernate.sql.Update;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.CreateUserDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.UpdateUserDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.UserDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.UserTDEEDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.services.UserService;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Obtener todos los usuarios.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UpdateUserDTO>> getAllUsers() {
        logger.info("Solicitando la lista de todos los usuarios...");
        try {
            List<UpdateUserDTO> users = userService.getAllUsers();
            if (users.isEmpty()) {
                logger.warn("No hay usuarios registrados.");
                return ResponseEntity.noContent().build();
            }
            logger.info("Se han encontrado {} usuarios.", users.size());
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            logger.error("Error al listar los usuarios: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Obtener un usuario por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        logger.info("Buscando usuario con ID: {}", id);
        try {
            String username = jwtUtil.getAuthenticatedUsername();

            Optional<UpdateUserDTO> user = Optional.ofNullable(userService.getUserById(id));
            if (user.isEmpty()) {
                logger.warn("No se encontró el usuario con ID: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el usuario con ID: " + id);
            }

            // Verificar que el usuario autenticado es el mismo que el solicitado

            logger.info("Usuario con ID {} encontrado: {}", id, user.get());
            return ResponseEntity.ok(user.get());

        } catch (ValidationException e) {
            logger.warn("El usuario autenticado no tiene permiso para acceder a la información del usuario con ID: {}", id);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso para acceder a esta información.");
        } catch (Exception e) {
            logger.error("Error al buscar el usuario con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }


    /**
     * Crear un nuevo usuario.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody CreateUserDTO dto) {
        logger.info("Intentando registrar un nuevo usuario con username: {}", dto.getUsername());
        try {
            CreateUserDTO createdUser = userService.createUser(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado exitosamente");
        } catch (IllegalArgumentException e) {
            logger.warn("Error en los datos del usuario: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al crear el usuario: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el usuario.");
        }
    }

    /**
     * Crear un nuevo usuario administrador.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/register")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody CreateUserDTO dto) {
        logger.info("Intentando registrar un nuevo usuario con username: {}", dto.getUsername());
        try {
            CreateUserDTO createdUser = userService.createAdmin(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Administrador registrado exitosamente");
        } catch (IllegalArgumentException e) {
            logger.warn("Error en los datos del usuario: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al crear el usuario: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el usuario.");
        }
    }

    @GetMapping("/tdee")
    public ResponseEntity<UserTDEEDTO> getTdeeData() {
        logger.info("Solicitando datos de TDEE para el usuario ");
        String username = jwtUtil.getAuthenticatedUsername();
        try {
            UserTDEEDTO tdeeData = userService.getTDEEData(username);
            logger.info("Datos de TDEE encontrados para el usuario con nombre de usuario: {}", username);
            return ResponseEntity.ok(tdeeData);
        } catch (IllegalArgumentException e) {
            logger.warn("Usuario no encontrado con id: {}", username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            logger.error("Error al obtener los datos de TDEE para el usuario con nombre de usuario {}: {}", username, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        try {
            UserDTO userDTO = userService.getUserByUsername(username);
            return ResponseEntity.ok(userDTO);
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado: " + username);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }

    @GetMapping("/check-username/{username}")
    public ResponseEntity<?> checkUsernameAvailability(@PathVariable String username) {
        logger.info("Verificando disponibilidad del username: {}", username);

        try {
            boolean exists = userService.isUsernameTaken(username);

            logger.info("El username '{}' está {}", username, exists ? "en uso" : "disponible");
            return ResponseEntity.ok(exists);

        } catch (IllegalArgumentException e) {
            logger.warn("Solicitud inválida: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        } catch (Exception e) {
            logger.error("Error al verificar username '{}': {}", username, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al verificar el username.");
        }
    }

    @GetMapping("/check-email/{email}")
    public ResponseEntity<?> checkEmailAvailability(@PathVariable String email) {
        logger.info("Verificando disponibilidad del email: {}", email);

        try {
            boolean exists = userService.isEmailTaken(email);

            logger.info("El email '{}' está {}", email, exists ? "en uso" : "disponible");
            return ResponseEntity.ok(exists);

        } catch (IllegalArgumentException e) {
            logger.warn("Solicitud inválida: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());

        } catch (Exception e) {
            logger.error("Error al verificar email '{}': {}", email, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor al verificar el email.");
        }
    }

    /**
     * Actualizar un usuario existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserDTO dto) {
        logger.info("Intentando actualizar el usuario con ID {}", id);
        try {
            // Obtener el usuario actual
            Optional<UpdateUserDTO> existingUser = Optional.ofNullable(userService.getUserById(id));
            if (existingUser.isEmpty()) {
                logger.warn("No se encontró el usuario con ID {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el usuario con ID: " + id);
            }
            // Realizar la actualización
            UpdateUserDTO updatedUser = userService.updateUser(id, dto);
            logger.info("Usuario con ID {} actualizado exitosamente", id);
            return ResponseEntity.ok(updatedUser);
        }
        catch (ValidationException e) {
            logger.warn("El usuario autenticado no tiene permiso para actualizar el usuario con ID {}", id);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso para actualizar esta cuenta.");
        }catch (IllegalArgumentException e) {
            logger.warn("Error en los datos del usuario: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al actualizar el usuario: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al actualizar el usuario.");
        }
    }


    /**
     * Eliminar un usuario por ID.
     */
    @PreAuthorize("hasRole('ADMIN')or hasRole('USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        logger.info("Intentando eliminar el usuario con ID {}", id);
        try {
            userService.deleteUser(id);

            return ResponseEntity.ok("Usuario con id: " + id + " eliminado con éxito.");
        } catch (Exception e) {
            logger.error("Error al eliminar el usuario con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el usuario.");
        }
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivateUser(@PathVariable Long id) {
        try {
            String username = jwtUtil.getAuthenticatedUsername();
            userService.validateUserOwnership(id, username);
            userService.deactivateUser(id);
            return ResponseEntity.status(HttpStatus.OK).body("Cuenta desativada correctamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno.");
        }
    }

    @PutMapping("/reactivate/{email}")
    public ResponseEntity<?> reactivateUserByEmail(@PathVariable String email) {
        try {
            userService.reactivateUserByEmail(email);
            return ResponseEntity.ok("Cuenta reactivada con éxito.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno.");
        }
    }
}
