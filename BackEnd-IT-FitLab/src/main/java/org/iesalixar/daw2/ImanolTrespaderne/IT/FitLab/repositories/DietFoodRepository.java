package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories;

import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.DietFood;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.DietFoodPK;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums.DayOfTheWeek;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums.MealType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DietFoodRepository extends JpaRepository<DietFood, DietFoodPK> {

    // Buscar alimentos de una dieta específica para un día de la semana
    List<DietFood> findById_DietIdAndId_DayWeek(Long dietId, DayOfTheWeek dayWeek);

    // 📌 Buscar todos los alimentos de una dieta
    List<DietFood> findById_DietId(Long dietId);

    // 📌 Buscar un alimento específico en una dieta para un día y tipo de comida
    Optional<DietFood> findById(DietFoodPK id);

    // 📌 Eliminar un alimento de una dieta para un día y tipo de comida
    void deleteById(DietFoodPK id);
}
