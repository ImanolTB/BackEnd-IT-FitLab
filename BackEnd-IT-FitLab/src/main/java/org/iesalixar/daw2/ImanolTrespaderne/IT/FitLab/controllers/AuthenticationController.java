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
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> authenticate(@Valid @RequestBody AuthRequestDTO authRequest){
      try {
          Optional<User> user=userRepository.findByUsername(authRequest.getUsername());
          if (authRequest.getUsername() == null || authRequest.getPassword() == null){
              return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponseDTO(null, "El nombre de usuario y la contraseña son obligatorios"));
          }
          if (!user.get().isEnabled()) {
              throw new DisabledException("La cuenta está desactivada");
          }
          Authentication authentication =authenticationManager.authenticate(
                  new UsernamePasswordAuthenticationToken(authRequest.getUsername(),authRequest.getPassword())
          );

          String username= authentication.getName();
          List<String> roles= authentication.getAuthorities().stream()
                  .map(authority ->authority.getAuthority())
                  .toList();

          String token =jwtUtil.generateToken(username, roles);

          return ResponseEntity.ok(new AuthResponseDTO(token,"Login exitoso"));
      }catch (BadCredentialsException e){
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                  .body(new AuthResponseDTO(null, "Credenciales inválidas. Por favor, verifica tus datos"));
      }catch (DisabledException e){
          return ResponseEntity.status(HttpStatus.FORBIDDEN)
                  .body(new AuthResponseDTO(null, "El usuario está actualmente deshabilitado")) ;
      }
      catch (Exception e){
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                  .body(new AuthResponseDTO(null, "Ocurrió un error inesperado. Intentelo de nuevo más tarde.")) ;
      }
    }


}
