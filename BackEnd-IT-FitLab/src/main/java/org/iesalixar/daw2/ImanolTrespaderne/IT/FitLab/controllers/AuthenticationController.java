package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.controllers;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.AuthRequestDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.AuthResponseDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.User;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories.UserRepository;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Autenticación", description = "Operaciones relacionadas con el login de usuarios")

public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login exitoso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Username o password vacíos",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Usuario deshabilitado",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json"))
    })
    /*
    * Método que se encargará de realizar el login de la aplicación
    */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> authenticate(@Valid @RequestBody AuthRequestDTO authRequest) {
        try {
            // Se busca el usuario en la base de datos por su nombre de usuario
            Optional<User> user = userRepository.findByUsername(authRequest.getUsername());

            // Verificación básica de campos obligatorios
            if (authRequest.getUsername() == null || authRequest.getPassword() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new AuthResponseDTO(null, "El nombre de usuario y la contraseña son obligatorios"));
            }

            // Si el usuario existe pero está deshabilitado, se lanza una excepción
            if (user.isPresent() && !user.get().isEnabled()) {
                throw new DisabledException("La cuenta está desactivada");
            }

            // Se intenta autenticar con el AuthenticationManager de Spring
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );

            // Si la autenticación es correcta, se recupera el nombre de usuario y roles
            String username = authentication.getName();
            List<String> roles = authentication.getAuthorities().stream()
                    .map(authority -> authority.getAuthority())
                    .toList();

            // Se genera un token JWT con el nombre de usuario y los roles
            String token = jwtUtil.generateToken(username, roles);

            // Se devuelve una respuesta con el token y un mensaje de éxito
            return ResponseEntity.ok(new AuthResponseDTO(token, "Login exitoso"));

        } catch (BadCredentialsException e) {
            // En caso de credenciales inválidas
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponseDTO(null, "Credenciales inválidas. Por favor, verifica tus datos"));
        } catch (DisabledException e) {
            // En caso de cuenta deshabilitada
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new AuthResponseDTO(null, "El usuario está actualmente deshabilitado"));
        } catch (Exception e) {
            // Para cualquier otro error inesperado
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponseDTO(null, "Ocurrió un error inesperado. Inténtelo de nuevo más tarde."));
        }
    }

}
