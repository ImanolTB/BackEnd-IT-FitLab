package org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.repositories;

import org.iesalixar.daw2.ImanolTrespaderne.IT.FitLab.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
