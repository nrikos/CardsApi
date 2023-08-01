package com.logicea.cards.api.controllers;

import com.logicea.cards.api.dtos.CardDTO;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CreateCardControllerTest extends CardsControllerBaseTest{



    private static List<String> validColors() {
        return IntStream.range(0, 10).mapToObj(i -> String.format("#%s", RandomStringUtils.randomAlphanumeric(6))).toList();
    }

    /*
        This test covers the happy path of the following requirements:
         1. A user creates a card by providing a name for it and, optionally, a description and a color
         2. Name is mandatory
         3. Upon creation, the status of a card is To Do
         4. Color, if provided, should conform to a “6 alphanumeric characters prefixed with a #“ format
     */
    @ParameterizedTest
    @MethodSource("validColors")
    public void createValidCardTest(String color) throws Exception {

        CardDTO card = CardDTO.builder().name("Test card").color(color).build();
        CardDTO savedCard = performValidCreateCardRequest(card, johnToken);
        Assertions.assertTrue(savedCard.getStatus().name().equalsIgnoreCase("TO_DO"));
    }


    /*
        This test covers the invalid path of the following requirements:
         1. Name is mandatory
    */
    @Test
    public void createCardBlankNameTest() throws Exception {

        CardDTO card = CardDTO.builder().color("#FF6067").build();
        String requestBody = objectMapper.writeValueAsString(card);
        mockMvc.perform(post("/api/cards")
                        .header("Authorization", String.format("Bearer %s", johnToken))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                        {"errorCode":"CARDS-E-002","error":"Card name is mandatory"}"""));
    }

    private static List<String> invalidColors() {
        return List.of("invalid", "#1", "#12", "#123", "#1234", "#12345", "#1234567", "#A", "#Ab", "#AbC", "#AbCd", "#AbCdE", "#AbCdEfG");
    }

    /*
    This test covers the invalid path of the following requirements:
     1. Color, if provided, should conform to a “6 alphanumeric characters prefixed with a #“ format
    */
    @ParameterizedTest
    @MethodSource("invalidColors")
    public void createCardInvalidColorTest(String color) throws Exception {

        CardDTO card = CardDTO.builder().name("Test").color(color).build();
        String requestBody = objectMapper.writeValueAsString(card);
        mockMvc.perform(post("/api/cards")
                        .header("Authorization", String.format("Bearer %s", johnToken))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                        {"errorCode":"CARDS-E-002","error":"Invalid color"}"""));

    }
}

