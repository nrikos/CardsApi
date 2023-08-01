package com.logicea.cards.api.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.logicea.cards.api.CardsDemoApplicationTests;
import com.logicea.cards.api.dtos.CardDTO;
import com.logicea.cards.api.payloads.JwtTokenResponse;
import com.logicea.cards.api.payloads.LoginRequest;
import com.logicea.cards.api.services.CardStoreService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@EnableWebMvc
@AutoConfigureMockMvc
@SpringBootTest(classes = CardsDemoApplicationTests.class)
// to drop DB after each test method execution
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql({"/scripts/clean_db.sql", "/scripts/data.sql"})
public class CardsControllerBaseTest {


    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    CardStoreService cardStoreService;

    protected static String johnToken;


    @BeforeEach
    public void authenticate() throws Exception {
        if (johnToken == null) {
            johnToken = authenticateUser("john.doe@yahoo.com", "password");
        }
    }

    protected String authenticateUser(String email, String password) throws Exception {
        LoginRequest loginRequest = LoginRequest.builder().email(email)
                .password(password)
                .build();
        MvcResult result = mockMvc.perform(post("/api/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();
        String resultString = result.getResponse().getContentAsString();
        JwtTokenResponse jwtToken = objectMapper.readValue(resultString, JwtTokenResponse.class);
        Assertions.assertNotNull(jwtToken);
        Assertions.assertNotNull(jwtToken.getToken());
        return jwtToken.getToken();
    }

    protected CardDTO performValidCreateCardRequest(CardDTO card, String token) throws Exception {
        String requestBody = objectMapper.writeValueAsString(card);
        MvcResult response =mockMvc.perform(post("/api/cards")
                        .header("Authorization", String.format("Bearer %s", token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();
        String responseString = response.getResponse().getContentAsString();
        return objectMapper.readValue(responseString, CardDTO.class);
    }



    protected CardDTO performFindCardByIdRequest(Long id, String toke) throws Exception {
        MvcResult response =mockMvc.perform(get("/api/cards/{cardId}", String.valueOf(id))
                        .header("Authorization", String.format("Bearer %s", toke)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        String responseString = response.getResponse().getContentAsString();
        return objectMapper.readValue(responseString, CardDTO.class);
    }

}
