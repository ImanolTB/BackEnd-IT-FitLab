package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.controllers;

import jakarta.validation.Valid;

import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.AuthRequestDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.AuthResponseDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> authenticate(@Valid @RequestBody AuthRequestDTO authRequest){
      try {
          if (authRequest.getUsername() == null || authRequest.getPassword() == null){
              return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponseDTO(null, "El nombre de usuario y la contraseña son obligatorios"));
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
      } catch (Exception e){
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                  .body(new AuthResponseDTO(null, "Ocurrió un error inesperado. Intentelo de nuevo más tarde.")) ;
      }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<AuthResponseDTO> handleException(Exception e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new AuthResponseDTO(null, "Ocurrió un error inesperado "+ e.getMessage()));
    }
}
