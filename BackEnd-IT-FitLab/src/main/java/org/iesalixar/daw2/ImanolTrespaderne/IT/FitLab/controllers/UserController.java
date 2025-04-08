package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.UserDTO;
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
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        logger.info("Solicitando la lista de todos los usuarios...");
        try {
            List<UserDTO> users = userService.getAllUsers();
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

            Optional<UserDTO> user = Optional.ofNullable(userService.getUserById(id));
            if (user.isEmpty()) {
                logger.warn("No se encontró el usuario con ID: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el usuario con ID: " + id);
            }

            // Verificar que el usuario autenticado es el mismo que el solicitado
            if (!user.get().getUsername().equals(username)) {
                logger.warn("El usuario autenticado no tiene permiso para acceder a la información del usuario con ID: {}", id);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso para acceder a esta información.");
            }

            logger.info("Usuario con ID {} encontrado: {}", id, user.get());
            return ResponseEntity.ok(user.get());

        } catch (Exception e) {
            logger.error("Error al buscar el usuario con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }


    /**
     * Crear un nuevo usuario.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO dto) {
        logger.info("Intentando registrar un nuevo usuario con username: {}", dto.getUsername());
        try {
            UserDTO createdUser = userService.createUser(dto);
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
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody UserDTO dto) {
        logger.info("Intentando registrar un nuevo usuario con username: {}", dto.getUsername());
        try {
            UserDTO createdUser = userService.createAdmin(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Administrador registrado exitosamente");
        } catch (IllegalArgumentException e) {
            logger.warn("Error en los datos del usuario: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al crear el usuario: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el usuario.");
        }
    }

    /**
     * Actualizar un usuario existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO dto) {
        logger.info("Intentando actualizar el usuario con ID {}", id);
        try {
            String username = jwtUtil.getAuthenticatedUsername();
            // Obtener el usuario actual
            Optional<UserDTO> existingUser = Optional.ofNullable(userService.getUserById(id));
            if (existingUser.isEmpty()) {
                logger.warn("No se encontró el usuario con ID {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el usuario con ID: " + id);
            }
            // Validar que el usuario autenticado es el propietario de la cuenta
            if (!existingUser.get().getUsername().equals(username)) {
                logger.warn("El usuario autenticado no tiene permiso para actualizar el usuario con ID {}", id);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso para actualizar esta cuenta.");
            }
            // Realizar la actualización
            UserDTO updatedUser = userService.updateUser(id, dto);
            logger.info("Usuario con ID {} actualizado exitosamente", id);
            return ResponseEntity.ok(updatedUser);

        } catch (IllegalArgumentException e) {
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
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        logger.info("Intentando eliminar el usuario con ID {}", id);
        try {
            userService.deleteUser(id);

            return ResponseEntity.ok("Usuario con id: "+id+" eliminado con éxito.");
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
            return ResponseEntity.ok("Cuenta desactivada correctamente.");
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
