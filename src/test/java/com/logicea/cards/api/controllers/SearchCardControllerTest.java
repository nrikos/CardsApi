package com.logicea.cards.api.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.logicea.cards.api.dtos.CardDTO;
import com.logicea.cards.api.payloads.CardStatus;
import com.logicea.cards.api.payloads.SearchResult;
import com.logicea.cards.api.payloads.SortOrder;
import com.logicea.cards.api.payloads.SearchRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SearchCardControllerTest extends CardsControllerBaseTest{

    private static Stream<Arguments> searchValues() {
        return Stream.of(
                Arguments.of(List.of(CardDTO.builder().name("Card 1").color("#FF0001").build()), CardDTO.builder().color("#FF0001").build(), 1),
                Arguments.of(List.of(CardDTO.builder().name("Card 2").color("#FF0002").build(),
                        CardDTO.builder().name("Card 3").color("#FF0002").build(),
                        CardDTO.builder().name("Card 4").color("#FF0002").build(),
                        CardDTO.builder().name("Card 5").color("#FF0003").build()), CardDTO.builder().color("#FF0002").build(), 3),
                Arguments.of(List.of(CardDTO.builder().name("Card 2").color("#FF0002").build(),
                        CardDTO.builder().name("Card 3").color("#FF0002").build(),
                        CardDTO.builder().name("Card 3").color("#FF0002").build(),
                        CardDTO.builder().name("Card 5").color("#FF0003").build()), CardDTO.builder().name("Card 3").color("#FF0002").build(), 2),
                Arguments.of(List.of(CardDTO.builder().name("Card 6").color("#FF0005").build(),
                        CardDTO.builder().name("Card 7").color("#FF0006").build(),
                        CardDTO.builder().name("Card 8").color("#FF0007").build(),
                        CardDTO.builder().name("Card 9").color("#FF0008").build()), CardDTO.builder().color("#FF1234").build(), 0)
        );
    }

    /*
        This test covers the search requirement:

            A user can search through cards they have access to.
        Filters include name, color, status and date of creation Optionally limit results using page
        & size or offset
        & limit options Results may be sorted by name, color, status, date of creation
     */

    @ParameterizedTest
    @MethodSource("searchValues")
    public void searchCardTest(List<CardDTO> cardList, CardDTO example, int expectedResults) throws Exception {

        for (CardDTO card : cardList) performValidCreateCardRequest(card, johnToken);

        SearchRequest<CardDTO> searchRequest = SearchRequest.<CardDTO>builder().pageNumber(0).resultsPerPage(10).example(example).build();
        String requestBody = objectMapper.writeValueAsString(searchRequest);
        MvcResult result = mockMvc.perform(post("/api/cards/search")
                        .header("Authorization", String.format("Bearer %s", johnToken))
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

        String resultString = result.getResponse().getContentAsString();
        SearchResult<CardDTO> searchResult = objectMapper.readValue(resultString, new TypeReference<>() {
        });
        Assertions.assertEquals(expectedResults, searchResult.getTotal());
        if (expectedResults > 0)
            Assertions.assertTrue(searchResult.getSearchResult().stream().allMatch(r -> r.getColor().contentEquals(example.getColor())));
    }

    @Test
    public void searchWithOrderTest() throws Exception {
        List<CardDTO> cardList = new java.util.ArrayList<>(List.of(
                CardDTO.builder().name("Card D").color("#FF0009").build(),
                CardDTO.builder().name("Card B").color("#FF0009").status(CardStatus.DONE).build(),
                CardDTO.builder().name("Card E").color("#FE0009").build(),
                CardDTO.builder().name("Card C").color("#FF0009").status(CardStatus.TO_DO).build(),
                CardDTO.builder().name("Card A").color("#FA0001").build(),
                CardDTO.builder().name("Card X").color("#FZ0005").build()));
        for (CardDTO card : cardList) performValidCreateCardRequest(card, johnToken);

        // Test asc order
        SearchRequest<CardDTO> searchRequest = SearchRequest.<CardDTO>builder().pageNumber(0).resultsPerPage(10).example(CardDTO.builder().color("#FF0009").build()).build();
        String requestBody = objectMapper.writeValueAsString(searchRequest);
        MvcResult result = mockMvc.perform(post("/api/cards/search")
                        .header("Authorization", String.format("Bearer %s", johnToken))
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        List<CardDTO> sortedAscList = cardList.stream().filter(c -> c.getColor().contentEquals("#FF0009")).sorted(Comparator.comparing(CardDTO::getName)).toList();
        Assertions.assertNotNull(result.getResponse());
        String ascResultString = result.getResponse().getContentAsString();
        SearchResult<CardDTO> ascResult = objectMapper.readValue(ascResultString, new TypeReference<>() {
        });
        for (int i = 0; i < ascResult.getSearchResult().size(); i++) {
            Assertions.assertTrue(ascResult.getSearchResult().get(i).getName().contentEquals(sortedAscList.get(i).getName()));
        }

        // test desc order
        searchRequest.setSortOrder(SortOrder.DESC.getOrder());
        requestBody = objectMapper.writeValueAsString(searchRequest);
        result = mockMvc.perform(post("/api/cards/search")
                        .header("Authorization", String.format("Bearer %s", johnToken))
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        List<CardDTO> sortedDescList = cardList.stream().filter(c -> c.getColor().contentEquals("#FF0009")).sorted(Comparator.comparing(CardDTO::getName).reversed()).toList();

        Assertions.assertNotNull(result.getResponse());
        String descResultString = result.getResponse().getContentAsString();
        SearchResult<CardDTO> descResult = objectMapper.readValue(descResultString, new TypeReference<>() {
        });
        for (int i = 0; i < descResult.getSearchResult().size(); i++) {
            Assertions.assertTrue(descResult.getSearchResult().get(i).getName().contentEquals(sortedDescList.get(i).getName()));
        }


    }

    /*
      This test the sort filed in the SearchRequest pojo.
     */
    @Test
    public void searchInvalidSortFieldTest() throws Exception {
        SearchRequest<CardDTO> searchRequest = SearchRequest.<CardDTO>builder().pageNumber(0).sortField("invalid").resultsPerPage(10).example(CardDTO.builder().color("#FF0009").status(CardStatus.IN_PROGRESS).build()).build();
        String requestBody = objectMapper.writeValueAsString(searchRequest);
        mockMvc.perform(post("/api/cards/search")
                        .header("Authorization", String.format("Bearer %s", johnToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {"errorCode":"CARDS-E-002","error":"Invalid sort field"}"""));
    }
}
