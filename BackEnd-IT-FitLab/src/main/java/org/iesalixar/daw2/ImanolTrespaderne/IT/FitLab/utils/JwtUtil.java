package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKeyFromProperties;

    // Par de claves públicas y privadas para firmar/verificar tokens JWT con algoritmo RSA
    @Autowired
    private KeyPair jwtkeyPair;

    // Tiempo de expiración del token (1 hora = 3600000 milisegundos)
    private static final long JWT_EXPIRATION = 3600000;

    /**
     * Método auxiliar para obtener una clave HMAC firmada a partir del valor definido en las propiedades.
     * No se utiliza en esta implementación, que está basada en RSA, pero se mantiene por compatibilidad.
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKeyFromProperties.getBytes());
    }

    /**
     * Extrae el nombre de usuario (subject) de un token JWT.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Permite extraer cualquier valor del token usando una función sobre los claims.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extrae todos los claims de un token JWT verificando su firma con la clave pública RSA.
     */
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(jwtkeyPair.getPublic())  // Verifica la firma con la clave pública
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Genera un token JWT firmado con clave privada RSA, incluyendo nombre de usuario y lista de roles.
     */
    public String generateToken(String username, List<String> roles) {
        return Jwts.builder()
                .subject(username)                          // Asigna el subject como el nombre de usuario
                .claim("roles", roles)                      // Añade los roles como claim personalizado
                .issuedAt(new Date())                       // Fecha de emisión
                .expiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))  // Fecha de expiración
                .signWith(jwtkeyPair.getPrivate(), Jwts.SIG.RS256) // Firma con RSA y clave privada
                .compact();
    }

    /**
     * Verifica si un token es válido comprobando que:
     * - El nombre de usuario coincide
     * - El token no ha expirado
     */
    public boolean validateToken(String token, String username) {
        Claims claims = Jwts.parser()
                .verifyWith(jwtkeyPair.getPublic())  // Verifica con clave pública
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return username.equals(claims.getSubject()) && !isTokenExpired(claims);
    }

    /**
     * Comprueba si el token ya ha expirado.
     */
    public boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    /**
     * Devuelve el nombre de usuario autenticado actualmente en el contexto de seguridad de Spring.
     */
    public String getAuthenticatedUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return principal.toString();  // En otros casos, devuelve la representación del objeto principal
    }
}