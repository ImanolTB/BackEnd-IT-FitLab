package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories;


import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.Food;
import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.enums.DayOfTheWeek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    // Buscar los alimentos de una dieta específica en un día de la semana
    @Query("SELECT f FROM Food f " +
            "JOIN DietFood df ON f.id = df.food.id " +
            "WHERE df.id.dietId = :dietId AND df.id.dayWeek = :dayOfWeek")
    List<Food> findFoodsByDietAndDay(@Param("dietId") Long dietId, @Param("dayOfWeek") DayOfTheWeek dayOfWeek);
}
