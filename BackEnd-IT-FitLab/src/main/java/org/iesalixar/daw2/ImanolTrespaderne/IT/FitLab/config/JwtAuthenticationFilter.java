package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.services.CustomUserDetailService;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtutil;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    /**
     * Este método se ejecuta una vez por cada petición HTTP entrante.
     * Se encarga de extraer y validar el token JWT del encabezado Authorization,
     * y establecer la autenticación en el contexto de seguridad de Spring si el token es válido.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Obtener el header "Authorization"
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // 2. Verificar que el header no sea nulo y comience con "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // Continúa sin hacer nada si no hay token válido
            return;
        }

        // 3. Extraer el token quitando el prefijo "Bearer "
        jwt = authHeader.substring(7);

        // 4. Extraer el nombre de usuario del token
        username = jwtutil.extractUsername(jwt);

        // 5. Si el usuario está presente y aún no ha sido autenticado en esta petición...
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 6. Cargar los detalles del usuario desde la base de datos
            var userDetails = customUserDetailService.loadUserByUsername(username);

            // 7. Validar el token
            if (jwtutil.validateToken(jwt, userDetails.getUsername())) {

                // 8. Extraer los claims del token
                Claims claims = jwtutil.extractAllClaims(jwt);

                // 9. Extraer la lista de roles del claim y convertirlos a autoridades de Spring
                List<String> roles = claims.get("roles", List.class);
                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                // 10. Crear un objeto de autenticación con los detalles del usuario y sus roles
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

                // 11. Establecer información adicional de la petición HTTP
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 12. Registrar la autenticación en el contexto de seguridad de Spring
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 13. Continuar con el resto del filtro (otros filtros y el endpoint)
        filterChain.doFilter(request, response);
    }
}

