package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DayOfTheWeek {
    LUNES, MARTES, MIERCOLES, JUEVES, VIERNES, SABADO, DOMINGO;
    // Convierte cualquier string (mayúsculas o minúsculas) en el enum correcto
    @JsonCreator
    public static DayOfTheWeek fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El día de la semana no puede estar vacío.");
        }

        for (DayOfTheWeek day : DayOfTheWeek.values()) {
            if (day.name().equalsIgnoreCase(value)) {
                return day;
            }
        }

        throw new IllegalArgumentException("Día de la semana no válido: " + value);
    }

    // Devuelve siempre el nombre en mayúsculas cuando se serializa en JSON
    @JsonValue
    public String toJson() {
        return name().toUpperCase();
    }
}
