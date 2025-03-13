package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.controllers;

import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.DietFoodDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.services.DietFoodService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dietfood")
public class DietFoodController {

    private final DietFoodService dietFoodService;

    public DietFoodController(DietFoodService dietFoodService) {
        this.dietFoodService = dietFoodService;
    }

    /**
     * Obtener los alimentos de una dieta para un día específico.
     */
    @GetMapping("/{dietId}/day/{dayWeek}")
    public ResponseEntity<List<DietFoodDTO>> getFoodsByDayOfTheWeek(@PathVariable Long dietId,
                                                                    @PathVariable String dayWeek) {
        return ResponseEntity.ok(dietFoodService.getFoodsByDayOfTheWeek(dietId, dayWeek));
    }
}
