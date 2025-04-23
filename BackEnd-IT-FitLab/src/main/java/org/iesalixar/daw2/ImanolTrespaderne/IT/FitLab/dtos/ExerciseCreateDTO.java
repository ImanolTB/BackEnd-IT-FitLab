package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos;

import lombok.Data;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums.MuscleGroup;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ExerciseCreateDTO {
    private Long id;
    private String name;
    private MultipartFile videoUrl;
    private MuscleGroup muscleGroup;
}
