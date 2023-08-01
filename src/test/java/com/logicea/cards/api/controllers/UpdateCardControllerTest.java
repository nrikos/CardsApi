package com.logicea.cards.api.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import com.github.fge.jsonpatch.JsonPatchOperation;
import com.github.fge.jsonpatch.ReplaceOperation;
import com.logicea.cards.api.dtos.CardDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UpdateCardControllerTest extends CardsControllerBaseTest {

    /*
            Valid update test
            Steps:
            1. upload card with owner -> john
            2. perform valid update request to change name and color
            3. validate card has been updated
            4. perform valid update request with ADMIN user -> jane
            5. validate card has been updated.

     */
    @Test
    public void updateCardTest() throws Exception {


        CardDTO card = CardDTO.builder().name("Test").color("#FF1234").build();
        CardDTO savedCard = performValidCreateCardRequest(card, johnToken);


        mockMvc.perform(patch("/api/cards/{cardId}",savedCard.getCardId())
                        .header("Authorization", String.format("Bearer %s", johnToken))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("""
                                [{"op":"replace","path":"/color","value":"#FF4567"},
                                {"op":"replace","path":"/name","value":"updated"}]"""))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        CardDTO updated = performFindCardByIdRequest(savedCard.getCardId(),johnToken);
        Assertions.assertEquals("#FF4567",updated.getColor());
        Assertions.assertEquals("updated",updated.getName());

        String janeToken = authenticateUser("jane.doe@yahoo.com", "password");
        mockMvc.perform(patch("/api/cards/{cardId}",savedCard.getCardId())
                        .header("Authorization", String.format("Bearer %s", janeToken))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("""
                                [{"op":"replace","path":"/color","value":"#FF4567"},
                                {"op":"replace","path":"/name","value":"jane updated"}]"""))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));

        updated = performFindCardByIdRequest(savedCard.getCardId(),johnToken);
        Assertions.assertEquals("jane updated",updated.getName());
    }

    /*
            Valid update test
            Steps:
            1. upload card with owner -> john
            2. perform invalid update request by removing color
            3. validate error returned

     */
    @Test
    public void updateCardBlankNameTest() throws Exception {

        String updateRequestString = """
                [{"op":"replace","path":"/color","value":"#FF4567"},{"op":"remove","path":"/name"}]""";
        List<JsonPatchOperation> operations = objectMapper.readValue(updateRequestString, new TypeReference<>(){});

        CardDTO card = CardDTO.builder().name("Test").color("#FF1234").build();
        CardDTO savedCard = performValidCreateCardRequest(card, johnToken);
        savedCard.setColor("#FF4567");
        savedCard.setName("updated");
        mockMvc.perform(patch("/api/cards/{cardId}",savedCard.getCardId())
                        .header("Authorization", String.format("Bearer %s", johnToken))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(operations)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                    {"errorCode":"CARDS-E-002","error":"name must not be empty"}"""));
    }

        /*
            Valid update test
            Steps:
            1. upload card with owner -> john
            2. perform invalid update request with wrong owner
            3. validate error response
     */

    @Test
    public void updateCardWithoutCardOwnershipTest() throws Exception {

        String updateRequestString = """
                [{"op":"replace","path":"/color","value":"#FF4567"},{"op":"remove","path":"/name"}]""";
        List<JsonPatchOperation> operations = objectMapper.readValue(updateRequestString, new TypeReference<>(){});

        CardDTO card = CardDTO.builder().name("Test").color("#FF1234").build();
        CardDTO savedCard = performValidCreateCardRequest(card,johnToken);

        String mikeToken = authenticateUser("mike.doe@yahoo.com", "password");
        savedCard.setColor("#FF4567");
        savedCard.setName("updated");
        mockMvc.perform(patch("/api/cards/{cardId}",savedCard.getCardId())
                        .header("Authorization", String.format("Bearer %s",mikeToken))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(operations)))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json("""
                    {"errorCode":"CARDS-E-004","error":"Card does not belong to user"}"""));
    }
}
