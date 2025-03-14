package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ActivityLevel {
    SEDENTARIO, LIGERO, MODERADO, ACTIVO, MUY_ACTIVO;

    // Convierte cualquier string (mayúsculas o minúsculas) en el enum correcto
    @JsonCreator
    public static ActivityLevel fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El nivel de actividad no puede estar vacío.");
        }

        for (ActivityLevel level : ActivityLevel.values()) {
            if (level.name().equalsIgnoreCase(value)) {
                return level;
            }
        }

        throw new IllegalArgumentException("Nivel de actividad no válido: " + value);
    }

    // Devuelve siempre el nombre en mayúsculas cuando se serializa en JSON
    @JsonValue
    public String toJson() {
        return name().toUpperCase();
    }
}
