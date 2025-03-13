package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.UserDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.services.RegisterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ITFitLab/register")
public class RegisterController {
    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    @Autowired
    private RegisterService userService;

    /**
     * Obtener todos los usuarios.
     */
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
            Optional<UserDTO> user = Optional.ofNullable(userService.getUserById(id));
            if (user.isPresent()) {
                logger.info("Usuario con ID {} encontrado: {}", id, user.get());
                return ResponseEntity.ok(user.get());
            } else {
                logger.warn("No se encontró el usuario con ID: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el usuario con ID: " + id);
            }
        } catch (Exception e) {
            logger.error("Error al buscar el usuario con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }

    /**
     * Crear un nuevo usuario.
     */
    @PostMapping
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDTO dto) {
        logger.info("Intentando registrar un nuevo usuario con username: {}", dto.getUsername());
        try {
            UserDTO createdUser = userService.createUser(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
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
            UserDTO updatedUser = userService.updateUser(id, dto);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            logger.warn("Error en los datos del usuario: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al actualizar el usuario: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el usuario.");
        }
    }

    /**
     * Eliminar un usuario por ID.
     */
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
}
