package com.logicea.cards.api.controllers;

import com.logicea.cards.api.dtos.CardDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FindCardControllerTest extends CardsControllerBaseTest{


    @Test
    public void getCardByIdTest() throws Exception {

        CardDTO johnCard = CardDTO.builder().name("John").color("#FF5678").build();
        CardDTO johnSavedCard = performValidCreateCardRequest(johnCard, johnToken);

        /*
            test that user who created card gets the card with the getById request
         */
        CardDTO searchedCard =performFindCardByIdRequest(johnSavedCard.getCardId(), johnToken);
        Assertions.assertTrue(johnSavedCard.getName().contentEquals(searchedCard.getName()));

          /*
            test that user who did not create card, cannot get the card with the getById request
         */
        String mikeToken = authenticateUser("mike.doe@yahoo.com", "password");
        MvcResult invalidSearch = mockMvc.perform(get("/api/cards/{cardId}", johnSavedCard.getCardId())
                        .header("Authorization", String.format("Bearer %s", mikeToken)))
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();

        CardDTO mikeCard = CardDTO.builder().name("Mike").color("#FF1234").build();
        CardDTO mikeSavedCard = performValidCreateCardRequest(mikeCard, mikeToken);

        String janeToken = authenticateUser("jane.doe@yahoo.com", "password");
        CardDTO janeCard = CardDTO.builder().name("Jane").color("#FF1234").build();
        CardDTO janeSavedCard = performValidCreateCardRequest(janeCard, janeToken);

        /*
            test that only ADMIN users can access the getAll cards endpoint
         */
        mockMvc.perform(get("/api/cards/all")
                        .header("Authorization", String.format("Bearer %s", janeToken)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());


        mockMvc.perform(get("/api/cards/all")
                        .header("Authorization", String.format("Bearer %s", johnToken)))
                .andExpect(status().is4xxClientError())
                // .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

    }
}
