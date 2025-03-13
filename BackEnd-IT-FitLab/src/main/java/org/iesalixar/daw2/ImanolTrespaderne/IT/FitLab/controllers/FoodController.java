package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.controllers;

import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.config.SecurityConfig;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.FoodDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.services.FoodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/foods")
public class FoodController {
    private static final Logger logger = LoggerFactory.getLogger(FoodController.class);

    @Autowired
    private FoodService foodService;



    @GetMapping
    public List<FoodDTO> getAllFoods() {
        return foodService.getAllFoods();
    }

    @PostMapping
    public FoodDTO createFood(@RequestBody FoodDTO dto) {
        return foodService.createFood(dto);
    }

    @DeleteMapping("/{id}")
    public void deleteFood(@PathVariable Long id) {
        foodService.deleteFood(id);
    }
}
