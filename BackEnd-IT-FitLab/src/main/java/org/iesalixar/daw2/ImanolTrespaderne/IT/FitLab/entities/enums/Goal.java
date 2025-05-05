package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Goal {
    PERDER_PESO, GANAR_MASA_MUSCULAR, MANTENIMIENTO;

    // Convierte cualquier string (mayúsculas o minúsculas) en el enum correcto
    @JsonCreator
    public static Goal fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El objetivo no puede estar vacío.");
        }

        for (Goal goal : Goal.values()) {
            if (goal.name().equalsIgnoreCase(value)) {
                return goal;
            }
        }

        throw new IllegalArgumentException("Objetivo no válido: " + value);
    }

    // Devuelve siempre el nombre en mayúsculas cuando se serializa en JSON
    @JsonValue
    public String toJson() {
        return name().toUpperCase();
    }
}
