package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories;


import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
}
