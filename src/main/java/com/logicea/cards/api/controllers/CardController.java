package com.logicea.cards.api.controllers;

import com.github.fge.jsonpatch.JsonPatchOperation;
import com.logicea.cards.api.dtos.CardDTO;
import com.logicea.cards.api.openapi.controllers.CardControllerOpenApi;
import com.logicea.cards.api.payloads.CardsApiError;
import com.logicea.cards.api.services.CardService;
import com.logicea.cards.api.exceptions.CardsApiException;
import com.logicea.cards.api.payloads.SearchRequest;
import com.logicea.cards.api.payloads.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@RestController
@RequestMapping("/api/cards")
public class CardController implements CardControllerOpenApi {

    @Autowired
    CardService cardService;

    @Override
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createCard(@Valid @RequestBody CardDTO card) throws Exception {
        CardDTO stored = cardService.createCard(card);
        return  ResponseEntity.ok(stored);
    }

    @Override
    @PatchMapping(value = "/{cardId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateCard(@PathVariable Long cardId,@RequestBody List<JsonPatchOperation> patchOperations) throws Exception {
        return ResponseEntity.ok(cardService.patchCard(cardId,patchOperations));
    }

    @Override
    @GetMapping(value = "/{cardId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCardById(@PathVariable  Long cardId) throws Exception {
        Optional<CardDTO> found = cardService.getCard(cardId);
        return found.isPresent() ? ResponseEntity.ok(found.get()) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(CardsApiError.CARD_NOT_FOUND.asJsonString());
    }


    @DeleteMapping(value = "/{cardId}" ,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteCard(@PathVariable Long cardId) throws Exception {
        cardService.deleteCard(cardId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllCards(){
        Set<CardDTO> allCards = cardService.getAllCards();
        return ResponseEntity.status(HttpStatus.OK).body(allCards);
    }

    @Override
    @PostMapping(value = "/search",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findCard(@RequestBody @Valid SearchRequest<CardDTO> searchRequest) throws CardsApiException {
        SearchResult<CardDTO> result = cardService.searchCards(searchRequest);
        return ResponseEntity.ok(result);
    }
}
