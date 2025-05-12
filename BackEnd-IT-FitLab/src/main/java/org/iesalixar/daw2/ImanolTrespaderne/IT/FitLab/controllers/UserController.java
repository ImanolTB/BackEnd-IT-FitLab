package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "Usuarios", description = "Operaciones relacionadas con los usuarios")
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
    @Operation(summary = "Método para btener todos los usuarios", description = "Requiere ser administrador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Lista de usuarios encontrada",content = @Content(mediaType = "application/json",schema = @Schema(implementation = UpdateUserDTO.class))),
            @ApiResponse(responseCode = "204", description = "No hay usuarios registrados"),
            @ApiResponse(responseCode = "403", description = "No tienes permisos para realizar esta accion"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
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
    @Operation(summary = "Método para btener un  usuario por ID", description = "Debe ser el propietario o administrador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente",content = @Content(mediaType = "application/json",schema = @Schema(implementation = UpdateUserDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "403", description = "No tienes permisos para realizar esta accion"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
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
    @Operation(summary = "Método para egistrar un nuevo usuario", description = "Se le asigna de manera automática el rol USER")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente"),
            @ApiResponse(responseCode = "400", description = "La edad deber ser mayor que 0"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
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
    @Operation(summary = "Método para egistrar un nuevo administrador", description = "Requiere ser administrador. Se le asigna de manera automática el rol ADMIN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "400", description = "La edad deber ser mayor que 0"),
            @ApiResponse(responseCode = "403", description = "No tienes permiso para crear una cuenta admin"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
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
    @Operation(summary = "Método para calcular el TDEE(gasto total de energía diaris) del usuario autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Datos de TDEE encontrado para el usuario", content = @Content(mediaType = "application/json",schema = @Schema(implementation = UserTDEEDTO.class)) ),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    @GetMapping("/tdee")
    public ResponseEntity<UserTDEEDTO> getTdeeData() {
        logger.info("Solicitando datos de TDEE para el usuario ");
        String username= jwtUtil.getAuthenticatedUsername();
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
    @Operation(summary = "Método para buscar un usuario por username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado", content = @Content(mediaType = "application/json",schema = @Schema(implementation = UserDTO.class)) ),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
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
    @Operation(summary = "Método para erificar disponibilidad de username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Username disponible"),
            @ApiResponse(responseCode = "400", description = "El username no puede estar vacío"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
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
    @Operation(summary = "Método para verificar disponibilidad de email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email disponible"),
            @ApiResponse(responseCode = "400", description = "El email no puede estar vacío"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    @GetMapping("/check-email/{email}")
    public ResponseEntity<?> checkEmailAvailability(@PathVariable String email) {
        logger.info("Verificando disponibilidad del email: {}", email);

        try {
            boolean exists = userService.isEmailTaken(email);

            logger.info("El email '{}' está {}", email, exists ? "en uso" : "disponible");
            return ResponseEntity.ok("Email disponible");

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
    @PreAuthorize("hasRole('ADMIN')or hasRole('USER')")
    @Operation(summary = "Método para actualizar datos de un usuario", description = "El administrador puede cambiar los datos de cualquier user. Un usuario solo puede cambiar sus propios datos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuenta actualizada con éxito", content = @Content(mediaType = "application/json",schema = @Schema(implementation = CreateUserDTO.class)) ),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
            @ApiResponse(responseCode = "400", description = "La edad deber ser mayor que 0"),
            @ApiResponse(responseCode = "403", description = "No tienes permiso para actualizar esta cuenta"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody CreateUserDTO dto) {
        logger.info("Intentando actualizar el usuario con ID {}", id);
        try {
            String username = jwtUtil.getAuthenticatedUsername();
            // Obtener el usuario actual
            Optional<UpdateUserDTO> existingUser = Optional.ofNullable(userService.getUserById(id));
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
            CreateUserDTO updatedUser = userService.updateUser(id, dto);
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
    @Operation(summary = "Método para eliminar usuario", description = "Un usuario administrador puede eliminar cualquier usuario, pero un usuario solo podrá eliminar su propia cuenta")
    @PreAuthorize("hasRole('ADMIN')or hasRole('USER')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuenta eliminada con éxito"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado con ese email"),
            @ApiResponse(responseCode = "403", description = "No tienes permiso para realizar esta acción."),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        logger.info("Intentando eliminar el usuario con ID {}", id);
        try {
            String username= jwtUtil.getAuthenticatedUsername();
            userService.validateUserOwnership(id, username);
            userService.deleteUser(id);
            return ResponseEntity.ok("Usuario con id: "+id+" eliminado con éxito.");
        }catch (IllegalArgumentException e) {
            logger.error("Error al eliminar el usuario con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (SecurityException e) {
            logger.error("No tienes permisos para eliminar al usuario con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
        catch (Exception e) {
            logger.error("Error al eliminar el usuario con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el usuario.");
        }
    }
    @Operation(summary = "Método para desactivar usuario", description = "Un usuario administrador puede desactivar cualquier usuario, pero un usuario solo podrá desactivar su propia cuenta")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PatchMapping("/{id}/deactivate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuenta desactivada con éxito"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado con ese email"),
            @ApiResponse(responseCode = "403", description = "No tienes permiso para realizar esta acción."),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
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
    @Operation(summary = "Método para reactivar usuario", description = "El usuario tendrá que introducir el correo para reactivar su cuenta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuenta reactivada coin éxito"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado con ese email"),
            @ApiResponse(responseCode = "409", description = "Cuenta ya existente"),
            @ApiResponse(responseCode = "500", description = "Error interno")
    })
    @PatchMapping("/reactivate/{email}")
    public ResponseEntity<?> reactivateUserByEmail(@PathVariable String email) {
        try {
            userService.reactivateUserByEmail(email);
            return ResponseEntity.ok("Cuenta reactivada con éxito.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno.");
        }
    }
}
