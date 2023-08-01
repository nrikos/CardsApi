package com.logicea.cards.api.services;

import com.logicea.cards.api.dtos.CardDTO;
import com.logicea.cards.api.exceptions.CardsApiException;
import com.logicea.cards.api.payloads.SearchResult;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.Set;

public interface CardStoreService {

    public CardDTO saveCard(CardDTO card, String email);

    public Optional<String> getCardOwnerEmail(Long id) throws CardsApiException;
    public CardDTO patchCard(CardDTO card);
    public void deleteCard(Long cardId,String email) throws CardsApiException;

    public Optional<CardDTO> findCardById(Long id,String email) throws Exception;

    public Set<CardDTO> findAll();

    public SearchResult<CardDTO> searchByExample(CardDTO card,String email, Pageable pageable) throws CardsApiException;
}
