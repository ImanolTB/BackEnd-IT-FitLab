package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private String message;
}
