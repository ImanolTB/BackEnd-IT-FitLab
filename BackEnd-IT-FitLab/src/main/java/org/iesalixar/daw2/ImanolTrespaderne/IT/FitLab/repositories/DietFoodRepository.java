package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories;

import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.DietFood;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.DietFoodPK;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums.DayOfTheWeek;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums.MealType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DietFoodRepository extends JpaRepository<DietFood, DietFoodPK> {

    // Buscar alimentos de una dieta específica para un día de la semana
    List<DietFood> findById_DietIdAndId_DayWeek(Long dietId, DayOfTheWeek dayWeek);

    List<DietFood> findById_DietId(Long dietId);
    @Modifying
    @Query("DELETE FROM DietFood df WHERE df.id.dietId = :dietId AND df.id.dayWeek = :dayWeek AND df.id.mealType = :mealType")
    void deleteByDietIdAndDayWeekAndMealType(@Param("dietId") Long dietId,
                                             @Param("dayWeek") DayOfTheWeek dayWeek,
                                             @Param("mealType") MealType mealType);


    Optional<DietFood> findById(DietFoodPK id);

    void deleteById(DietFoodPK id);
}
