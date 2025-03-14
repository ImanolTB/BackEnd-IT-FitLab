package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.services;

import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.config.SecurityConfig;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.DietDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.Diet;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.mappers.DietMapper;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories.DietRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DietService {
    private static final Logger logger = LoggerFactory.getLogger(DietService.class);


    @Autowired
    private  DietRepository dietRepository;
    @Autowired
    private  DietMapper dietMapper;



    public List<DietDTO> getAllDiets() {
        return dietRepository.findAll().stream()
                .map(dietMapper::toDTO)
                .collect(Collectors.toList());
    }

    public DietDTO getDietById(Long id) {
        return dietRepository.findById(id)
                .map(dietMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Diet not found"));
    }

    public DietDTO createDiet(DietDTO dto) {
        Diet diet = dietMapper.toEntity(dto);
        return dietMapper.toDTO(dietRepository.save(diet));
    }

    public DietDTO updateDiet(Long id,DietDTO dto){
        Diet diet=dietRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Dieta no encontrada"));
        diet.setDescription(dto.getDescription());
        diet.setName(dto.getName());
        diet.setDurationWeeks(dto.getDurationWeeks());
        return dietMapper.toDTO(dietRepository.save(diet));
    }

    public void deleteDiet(Long id) {
        dietRepository.deleteById(id);
    }
}
