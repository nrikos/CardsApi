package com.logicea.cards.api.repositories;

import com.logicea.cards.api.models.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long>  {

}
