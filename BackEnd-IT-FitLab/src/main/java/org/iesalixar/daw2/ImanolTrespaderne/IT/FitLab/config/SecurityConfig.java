package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);



<<<<<<< Updated upstream
=======
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .cors(withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/diets/**",
                                "/api/v1/food",
                                "/api/v1/dietfood/**",
                                "/api/v1/trainingprogrammes",
                                "/api/v1/workouts",
                                "/api/v1/exercises",
                                "/api/v1/workoutexercises," +
                                        "/api/v1/reviews").hasAnyRole("USER", "ADMIN")
>>>>>>> Stashed changes

   // @Bean
    //public PasswordEncoder passwordEncoder(){
       // logger.info("Entrando en el metodo passwordEncoder");
        //PasswordEncoder encoder= new BCryptPasswordEncoder();
       // logger.info("Saliendo del metodo passwordEncoder");
        //return encoder;
   // }
}
