package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.controllers;

import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.config.SecurityConfig;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.DietDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.services.DietService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diets")
public class DietController {
    private static final Logger logger = LoggerFactory.getLogger(DietController.class);

    private final DietService dietService;

    public DietController(DietService dietService) {
        this.dietService = dietService;
    }

    @GetMapping
    public List<DietDTO> getAllDiets() {
        return dietService.getAllDiets();
    }

    @PostMapping
    public DietDTO createDiet(@RequestBody DietDTO dto) {
        return dietService.createDiet(dto);
    }

    @DeleteMapping("/{id}")
    public void deleteDiet(@PathVariable Long id) {
        dietService.deleteDiet(id);
    }
}
