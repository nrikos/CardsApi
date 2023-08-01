package com.logicea.cards.api.services;

import com.github.fge.jsonpatch.JsonPatchOperation;
import com.logicea.cards.api.dtos.CardDTO;
import com.logicea.cards.api.exceptions.CardsApiException;
import com.logicea.cards.api.payloads.SearchRequest;
import com.logicea.cards.api.payloads.SearchResult;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CardService {

    public CardDTO createCard(CardDTO card) throws Exception;

    public void deleteCard(Long id) throws CardsApiException;

    public CardDTO patchCard(Long cardId, List<JsonPatchOperation> patchOperations) throws Exception;

    public Optional<CardDTO> getCard(Long cardId) throws Exception;

    public Set<CardDTO> getAllCards();

    public SearchResult<CardDTO> searchCards(SearchRequest<CardDTO> cardSearchRequest) throws CardsApiException;

}
