package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MuscleGroup {
    PECTORAL, ESPALDA, HOMBRO, TRICEP, BICEP, PIERNA, ABDOMEN;
    // Convierte cualquier string (mayúsculas o minúsculas) en el enum correcto
    @JsonCreator
    public static MuscleGroup fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El grupo muscular no puede estar vacío.");
        }

        for (MuscleGroup muscle : MuscleGroup.values()) {
            if (muscle.name().equalsIgnoreCase(value)) {
                return muscle;
            }
        }

        throw new IllegalArgumentException("Grupo muscular no válido: " + value);
    }

    // Devuelve siempre el nombre en mayúsculas cuando se serializa en JSON
    @JsonValue
    public String toJson() {
        return name().toUpperCase();
    }
}
