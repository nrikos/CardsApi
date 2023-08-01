package com.logicea.cards.api.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchOperation;
import com.logicea.cards.api.dtos.CardDTO;
import com.logicea.cards.api.exceptions.CardsApiException;
import com.logicea.cards.api.payloads.*;
import com.logicea.cards.api.utilities.PatchUtilities;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.logicea.cards.api.utilities.PatchUtilities.validatePatch;

@Slf4j
@Service
public class CardServiceImpl implements CardService {

    @Autowired
    CardStoreService cardStoreService;

    @Autowired
    UserStoreService userStoreService;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    @Transactional
    public CardDTO createCard(CardDTO card) throws CardsApiException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userStoreService.findByEmail(user.getUsername()).map(u -> {
            card.setStatus(CardStatus.TO_DO);
            return cardStoreService.saveCard(card, user.getUsername());
        }).orElseThrow(() -> new CardsApiException(CardsApiError.CARD_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    @Override
    @Transactional
    public void deleteCard(Long id) throws CardsApiException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        cardStoreService.deleteCard(id, user.getUsername());
    }

    @Override
    @Transactional
    public CardDTO patchCard(Long cardId, List<JsonPatchOperation> patchOperations) throws Exception {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<CardDTO> oldCard = cardStoreService.findCardById(cardId, user.getUsername());
        if (oldCard.isEmpty()) throw new CardsApiException(CardsApiError.CARD_NOT_FOUND, HttpStatus.BAD_REQUEST);
        validatePatch(patchOperations, objectMapper, CardDTO.class);
        CardDTO patched = PatchUtilities.patchCard(oldCard.get(), objectMapper, patchOperations, CardDTO.class);
        if (StringUtils.isBlank(patched.getName()))
            throw new CardsApiException(CardsApiError.VALIDATION_ERROR, HttpStatus.BAD_REQUEST, "name must not be empty");
        Optional<String> ownerEmail = cardStoreService.getCardOwnerEmail(oldCard.get().getCardId());
        if (ownerEmail.isEmpty()) throw new CardsApiException(CardsApiError.USER_NOT_FOUND, HttpStatus.BAD_REQUEST);
        return cardStoreService.saveCard(patched, ownerEmail.get());

    }

    @Override
    public Optional<CardDTO> getCard(Long cardId) throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return cardStoreService.findCardById(cardId, user.getUsername());
    }

    @Override
    public Set<CardDTO> getAllCards() {
        return cardStoreService.findAll();
    }

    @Override
    public SearchResult<CardDTO> searchCards(SearchRequest<CardDTO> cardSearchRequest) throws CardsApiException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (StringUtils.isBlank(cardSearchRequest.getSortField())) {
            //set default sort field
            cardSearchRequest.setSortField("name");
        }
        Sort sort;
        if (cardSearchRequest.getSortOrder() != null && SortOrder.isDescending(cardSearchRequest.getSortOrder())) {
            sort = Sort.by(cardSearchRequest.getSortField()).descending();
        } else {
            sort = Sort.by(cardSearchRequest.getSortField()).ascending();
        }

        return cardStoreService.searchByExample(cardSearchRequest.getExample(), user.getUsername(), PageRequest.of(cardSearchRequest.getPageNumber(), cardSearchRequest.getResultsPerPage(), sort));
    }
}
