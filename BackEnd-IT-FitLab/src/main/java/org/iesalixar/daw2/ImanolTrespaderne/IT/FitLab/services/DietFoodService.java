package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.services;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.dtos.DietFoodDTO;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.Diet;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.DietFood;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.DietFoodPK;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.Food;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums.DayOfTheWeek;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums.MealType;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.mappers.DietFoodMapper;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories.DietFoodRepository;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories.DietRepository;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DietFoodService {

    @Autowired
    private DietFoodRepository dietFoodRepository;

    @Autowired
    private DietFoodMapper dietFoodMapper;

    @Autowired
    private DietRepository dietRepository;

    @Autowired
    private FoodRepository foodRepository;
    /**
     * Añade un alimento a una dieta con una cantidad, día de la semana y tipo de comida específicos.
     * Convierte el DTO recibido a entidad, lo guarda en la base de datos y devuelve el resultado como DTO.
     */
    @Transactional
    public DietFoodDTO addFoodToDiet(@Valid @RequestBody DietFoodDTO dto) {
        DietFood dietFood = dietFoodMapper.toEntity(dto);
        return dietFoodMapper.toDTO(dietFoodRepository.save(dietFood));
    }

    /**
     * Elimina un alimento de una dieta según el identificador compuesto (dieta, alimento, día, tipo de comida).
     * Si el alimento no existe en esa combinación, lanza una excepción.
     */
    @Transactional
    public void removeFoodFromDiet(@Valid @RequestBody DietFoodDTO dto) {
        DietFoodPK id = new DietFoodPK(dto.getDietId(), dto.getFoodId(), dto.getDayWeek(), dto.getMealType());

        Optional<DietFood> dietFood = dietFoodRepository.findById(id);

        if (dietFood.isPresent()) {
            System.out.println(dietFood.get().getId()); // Impresión opcional de depuración
            dietFoodRepository.deleteById(dietFood.get().getId());
        } else {
            throw new IllegalArgumentException("No se encontró la comida en la dieta para eliminarla.");
        }
    }

    /**
     * Reemplaza todos los alimentos asignados a una dieta para un día de la semana y tipo de comida determinados.
     * Primero valida los datos, luego elimina los alimentos existentes para ese día/tipo, y finalmente guarda los nuevos.
     */
    @Transactional
    public List<DietFoodDTO> replaceFoodsForDayAndType(Long dietId, DayOfTheWeek day, MealType type, List<DietFoodDTO> nuevosAlimentos) {
        if (dietId == null || day == null || type == null) {
            throw new IllegalArgumentException("Día, tipo de comida y dieta no pueden ser nulos.");
        }

        // Validación de cada nuevo alimento (ID y cantidad positiva)
        for (DietFoodDTO dto : nuevosAlimentos) {
            if (dto.getFoodId() == null || dto.getQuantity() == null || dto.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Los alimentos deben tener foodId y una cantidad válida mayor que 0.");
            }
        }

        // Verifica que la dieta existe
        Diet diet = dietRepository.findById(dietId)
                .orElseThrow(() -> new IllegalArgumentException("Dieta no encontrada con ID: " + dietId));

        // Obtiene todos los alimentos actuales asignados a ese día y tipo, y los elimina
        List<DietFood> foodsToDelete = dietFoodRepository.findById_DietIdAndId_DayWeekAndId_MealType(dietId, day, type);
        dietFoodRepository.deleteAll(foodsToDelete);

        // Crea nuevas entidades DietFood a partir de los DTO
        List<DietFood> nuevasEntidades = nuevosAlimentos.stream().map(dto -> {
            Food food = foodRepository.findById(dto.getFoodId())
                    .orElseThrow(() -> new IllegalArgumentException("Alimento no encontrado con ID: " + dto.getFoodId()));

            DietFoodPK pk = new DietFoodPK(dietId, dto.getFoodId(), day, type);

            DietFood entity = new DietFood();
            entity.setId(pk);
            entity.setDiet(diet);
            entity.setFood(food);
            entity.setQuantity(dto.getQuantity());

            return entity;
        }).toList();

        // Guarda las nuevas entidades y devuelve sus DTOs
        return dietFoodRepository.saveAll(nuevasEntidades).stream()
                .map(dietFoodMapper::toDTO)
                .toList();
    }

    /**
     * Devuelve una lista de alimentos asociados a una dieta para un día de la semana concreto.
     * Filtra por el ID de la dieta y el día especificado, mapea los resultados a DTOs.
     */
    public List<DietFoodDTO> getFoodsByDayOfTheWeek(@PathVariable Long dietId, @PathVariable DayOfTheWeek dayWeek) {
        return dietFoodRepository.findById_DietIdAndId_DayWeek(dietId, dayWeek)
                .stream()
                .map(dietFoodMapper::toDTO)
                .collect(Collectors.toList());
    }
}