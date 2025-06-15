package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.services;


import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.User;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.Role;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {
@Autowired
    private UserRepository userRepository;

    // Este método se invoca automáticamente por Spring Security para autenticar a un usuario por su nombre de usuario
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Se busca el usuario por su nombre de usuario; si no existe, se lanza una excepción
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // Se construye un objeto UserDetails de Spring Security con los datos del usuario
        return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                .password(user.getPassword()) // Se establece la contraseña
                .authorities(
                        user.getRoles().stream() // Se extraen los roles del usuario
                                .map(Role::getName) // Se obtiene el nombre de cada rol
                                .toList() // Se convierte a lista
                                .toArray(new String[0]) // Se convierte la lista en array de strings
                )
                .accountExpired(false) // Indica que la cuenta no está expirada
                .accountLocked(false) // Indica que la cuenta no está bloqueada
                .credentialsExpired(false) // Indica que las credenciales no han expirado
                .disabled(!user.isEnabled()) // Si el usuario no está habilitado, se marca como deshabilitado
                .build(); // Se construye y retorna el objeto UserDetails
    }


}
