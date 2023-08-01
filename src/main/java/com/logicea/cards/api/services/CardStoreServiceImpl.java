package com.logicea.cards.api.services;

import com.logicea.cards.api.dtos.CardDTO;
import com.logicea.cards.api.exceptions.CardsApiException;
import com.logicea.cards.api.models.Card;
import com.logicea.cards.api.models.User;
import com.logicea.cards.api.payloads.CardsApiError;
import com.logicea.cards.api.payloads.SearchResult;
import com.logicea.cards.api.repositories.CardRepository;
import com.logicea.cards.api.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.logicea.cards.api.payloads.Role.ADMIN;

@Slf4j
@Service
public class CardStoreServiceImpl implements CardStoreService {

    @Autowired
    CardRepository cardRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;


    public CardDTO saveCard(CardDTO cardDTO, String email) {

        Optional<User> user = userRepository.findDistinctByEmail(email);
        return user.map(u -> {
            Card card = modelMapper.map(cardDTO, Card.class);
            card.setUser(u);
            Card saved = cardRepository.save(card);
            return modelMapper.map(saved, CardDTO.class);
        }).orElse(null);
    }

    @Override
    public Optional<String> getCardOwnerEmail(Long id) throws CardsApiException {
        return cardRepository.findById(id)
                .map(card-> Optional.of(card.getUser().getEmail()))
                .orElseThrow(()-> new CardsApiException(CardsApiError.CARD_NOT_FOUND, HttpStatus.NOT_FOUND));

    }

    @Override
    public CardDTO patchCard(CardDTO card) {
        return modelMapper.map(cardRepository.save(modelMapper.map(card,Card.class)),CardDTO.class);
    }

    public void deleteCard(Long cardId, String email) throws CardsApiException {
        Optional<Card> card = cardRepository.findById(cardId);
        if (card.isEmpty()) throw new CardsApiException(CardsApiError.CARD_NOT_FOUND, HttpStatus.NOT_FOUND);
        validateUserHasOwnership(card.get(), email);
        cardRepository.deleteById(cardId);
    }

    public Optional<CardDTO> findCardById(Long id, String email) throws Exception {
        Optional<Card> card = cardRepository.findById(id);
        if (card.isEmpty()) throw new CardsApiException(CardsApiError.CARD_NOT_FOUND, HttpStatus.NOT_FOUND);
        validateUserHasOwnership(card.get(), email);
        return Optional.of(modelMapper.map(card, CardDTO.class));
    }

    @Override
    public Set<CardDTO> findAll() {
        return cardRepository.findAll().stream().map(c -> modelMapper.map(c, CardDTO.class)).collect(Collectors.toSet());
    }

    public SearchResult<CardDTO> searchByExample(CardDTO card, String email, Pageable pageable) throws CardsApiException {
        Optional<User> user = userRepository.findDistinctByEmail(email);
        if (user.isPresent()) {
            Example<Card> example = Example.of(modelMapper.map(card, Card.class));
            example.getProbe().setUser(user.get());
            Page<Card> result = cardRepository.findAll(example, pageable);
            List<CardDTO> results = result.getContent().stream().map(experiment -> modelMapper.map(experiment, CardDTO.class)).toList();
            return new SearchResult<>(results, result.getTotalElements());
        } else throw new CardsApiException(CardsApiError.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    public void validateUserHasOwnership(Card card, String email) throws CardsApiException {
        Optional<User> user = userRepository.findDistinctByEmail(email);
        if (user.isPresent()) {
            if(ADMIN.equals(user.get().getRole())) return;
            if (card.getUser().getUserId() != user.get().getUserId())
                throw new CardsApiException(CardsApiError.USER_DOES_NOT_HAVE_OWNERSHIP, HttpStatus.BAD_REQUEST);
        } else throw new CardsApiException(CardsApiError.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

}
