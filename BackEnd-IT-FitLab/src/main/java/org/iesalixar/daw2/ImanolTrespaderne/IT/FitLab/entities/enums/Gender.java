package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Gender {
    M, F;
    // Convierte cualquier string (mayúsculas o minúsculas) en el enum correcto
    @JsonCreator
    public static Gender fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El género no puede estar vacío.");
        }

        for (Gender gender : Gender.values()) {
            if (gender.name().equalsIgnoreCase(value)) {
                return gender;
            }
        }

        throw new IllegalArgumentException("Género no válido: " + value);
    }

    // Devuelve siempre el nombre en mayúsculas cuando se serializa en JSON
    @JsonValue
    public String toJson() {
        return name().toUpperCase();
    }
}
