package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.TrainingProgramme;

public enum TrainingLevel {
    PRINCIPIANTE, INTERMEDIO, AVANZADO;
    // Convierte cualquier string (mayúsculas o minúsculas) en el enum correcto
    @JsonCreator
    public static TrainingLevel fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El nivel de entrenamiento no puede estar vacío.");
        }

        for (TrainingLevel level : TrainingLevel.values()) {
            if (level.name().equalsIgnoreCase(value)) {
                return level;
            }
        }

        throw new IllegalArgumentException("Nivel de entrenamiento no válido: " + value);
    }

    // Devuelve siempre el nombre en mayúsculas cuando se serializa en JSON
    @JsonValue
    public String toJson() {
        return name().toUpperCase();
    }
}
