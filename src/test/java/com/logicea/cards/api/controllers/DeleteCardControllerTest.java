package com.logicea.cards.api.controllers;

import com.logicea.cards.api.dtos.CardDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DeleteCardControllerTest extends CardsControllerBaseTest{

    @Test
    public void deleteCardTest() throws Exception {
        CardDTO card = CardDTO.builder().name("Test card").color("#RR1234").build();
        CardDTO savedCard = performValidCreateCardRequest(card, johnToken);

        mockMvc.perform(delete("/api/cards/{cardId}", String.valueOf(savedCard.getCardId()))
                        .header("Authorization", String.format("Bearer %s", johnToken))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
        mockMvc.perform(get("/api/cards/{cardId}",  String.valueOf(savedCard.getCardId()))
                        .header("Authorization", String.format("Bearer %s", johnToken)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andReturn();
    }

    @Test
    public void deleteCardAdminRoleTest() throws Exception {
        CardDTO card = CardDTO.builder().name("Test card").color("#RR1234").build();
        CardDTO savedCard = performValidCreateCardRequest(card, johnToken);

        String janeToken = authenticateUser("jane.doe@yahoo.com", "password");
        mockMvc.perform(delete("/api/cards/{cardId}", String.valueOf(savedCard.getCardId()))
                        .header("Authorization", String.format("Bearer %s", janeToken))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());

    }

    @Test
    public void deleteCardInvalidOwnershipTest() throws Exception {
        CardDTO card = CardDTO.builder().name("Test card").color("#RR1234").build();
        CardDTO savedCard = performValidCreateCardRequest(card, johnToken);

        String mikeToken = authenticateUser("mike.doe@yahoo.com", "password");
        mockMvc.perform(delete("/api/cards/{cardId}", String.valueOf(savedCard.getCardId()))
                        .header("Authorization", String.format("Bearer %s", mikeToken))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                            {"errorCode":"CARDS-E-004","error":"Card does not belong to user"}"""));

    }
}
