package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories;

import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.DietFood;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums.DayOfTheWeek;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums.MealType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DietFoodRepository extends JpaRepository<DietFood, Long> {
    List<DietFood> findByDietIdAndDayWeek(Long dietId, DayOfTheWeek dayWeek);

    void deleteByDietIdAndFoodIdAndDayWeekAndMealType(Long dietId, Long foodId, String upperCase, String upperCase1);
}
