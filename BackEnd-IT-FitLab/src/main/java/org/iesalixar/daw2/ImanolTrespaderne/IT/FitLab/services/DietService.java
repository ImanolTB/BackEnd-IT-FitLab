package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.services;

import jakarta.validation.Valid;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.DietDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.Diet;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.mappers.DietMapper;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories.DietRepository;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DietService {

    // Logger para registrar información de depuración o errores
    private static final Logger logger = LoggerFactory.getLogger(DietService.class);

    @Autowired
    private DietRepository dietRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DietMapper dietMapper;

    @Autowired
    private CustomUserDetailService userDetailService;

    /**
     * Devuelve todas las dietas existentes en la base de datos como una lista de DTOs.
     */
    public List<DietDTO> getAllDiets() {
        try {
            return dietRepository.findAll().stream()
                    .map(dietMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener las dietas.");
        }
    }

    /**
     * Devuelve una dieta específica a partir de su ID.
     * Lanza una excepción si no se encuentra.
     */
    public DietDTO getDietById(@PathVariable Long id) {
        return dietRepository.findById(id)
                .map(dietMapper::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Dieta no encontrada"));
    }

    /**
     * Devuelve todas las dietas asociadas a un usuario dado por su nombre de usuario.
     * Si no se encuentra ninguna dieta, lanza una excepción.
     */
    public List<DietDTO> getDietByUsername(@PathVariable String username) {
        try {
            List<Diet> diets = dietRepository.findByUserUsername(username);

            if (diets.isEmpty()) {
                throw new IllegalArgumentException("No se encontraron dietas para el usuario: " + username);
            }

            return diets.stream()
                    .map(dietMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener las dietas del usuario.");
        }
    }

    /**
     * Crea una nueva dieta a partir de un DTO.
     * Convierte el DTO a entidad, lo guarda y devuelve el DTO resultante.
     */
    public DietDTO createDiet(@Valid @RequestBody DietDTO dto) {
        try {
            Diet diet = dietMapper.toEntity(dto);
            return dietMapper.toDTO(dietRepository.save(diet));
        } catch (Exception e) {
            throw new RuntimeException("Error al crear la dieta.");
        }
    }

    /**
     * Actualiza una dieta existente con los datos del DTO recibido.
     * Solo se actualizan los campos permitidos (nombre, descripción, duración y objetivo).
     */
    public DietDTO updateDiet(@PathVariable Long id, @Valid @RequestBody DietDTO dto) {
        try {
            Diet diet = dietRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Dieta no encontrada"));

            diet.setDescription(dto.getDescription());
            diet.setName(dto.getName());
            diet.setDurationWeeks(dto.getDurationWeeks());
            diet.setGoal(dto.getGoal());

            return dietMapper.toDTO(dietRepository.save(diet));
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar la dieta.");
        }
    }

    /**
     * Elimina una dieta por su ID.
     */
    public void deleteDiet(@PathVariable Long id) {
        try {
            dietRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar la dieta.");
        }
    }

    /**
     * Valida si el usuario tiene permiso para modificar la dieta indicada.
     * Si es administrador, se permite automáticamente. En caso contrario, se comprueba la propiedad.
     */
    public void validateOwnership(Long dietId, String username) {
        if (isAdmin(username)) return;

        Diet diet = dietRepository.findById(dietId)
                .orElseThrow(() -> new IllegalArgumentException("Dieta no encontrada"));

        if (!diet.getUser().getUsername().equals(username)) {
            throw new SecurityException("No tienes permiso para modificar esta dieta.");
        }
    }

    /**
     * Verifica si el usuario tiene rol de administrador.
     */
    public boolean isAdmin(String username) {
        return userRepository.findByUsername(username)
                .map(user -> user.getRoles().stream()
                        .anyMatch(role -> role.getName().equals("ROLE_ADMIN")))
                .orElse(false);
    }
}