package com.logicea.cards.api.repositories;

import com.logicea.cards.api.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findDistinctByEmail(String email);
}
