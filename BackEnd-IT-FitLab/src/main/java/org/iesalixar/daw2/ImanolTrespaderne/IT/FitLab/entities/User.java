package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums.ActivityLevel;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums.Gender;

import java.math.BigDecimal;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "roles")
@EqualsAndHashCode
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "El nombre de usuario no puede estar vacío.")
    @Size(max = 50, message = "El nombre de usuario no puede superar los 50 caracteres.")
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @NotEmpty(message = "La contraseña no puede estar vacía.")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres.")
    @Column(nullable = false, length = 100)
    private String password;

    @NotEmpty(message = "El correo electrónico no puede estar vacío.")
    @Email(message = "Debe proporcionar un correo electrónico válido.")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @NotEmpty(message = "El nombre no puede estar vacío.")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres.")
    @Column(length = 100, nullable = false)
    private String name;

    @NotEmpty(message = "El apellido no puede estar vacío.")
    @Size(max = 100, message = "El apellido no puede superar los 100 caracteres.")
    @Column(name = "last_name", length = 100, nullable = false)
    private String lastName;

    @NotNull(message = "La edad es obligatoria.")
    @Column(name = "age", nullable = false)
    private Integer age;

    @NotNull(message = "La altura es obligatoria.")
    @Column(name = "height", precision = 5, scale = 2, nullable = false)
    private BigDecimal height;

    @NotNull(message = "El peso es obligatorio.")
    @Column(name = "weight", precision = 5, scale = 2, nullable = false)
    private BigDecimal weight;

    @NotNull(message = "El género es obligatorio.")
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, length = 1)
    private Gender gender;

    @NotNull(message = "El nivel de actividad es obligatorio.")
    @Enumerated(EnumType.STRING)
    @Column(name = "activity_level", nullable = false)
    private ActivityLevel activityLevel;
}
