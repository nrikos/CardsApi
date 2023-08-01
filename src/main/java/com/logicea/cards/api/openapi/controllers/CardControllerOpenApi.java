package com.logicea.cards.api.openapi.controllers;

import com.github.fge.jsonpatch.JsonPatchOperation;
import com.logicea.cards.api.dtos.CardDTO;
import com.logicea.cards.api.openapi.constants.OpenApiExampleConstants;
import com.logicea.cards.api.exceptions.CardsApiException;
import com.logicea.cards.api.payloads.SearchRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;
import java.util.Set;

@Tag(name = "Cards")
public interface CardControllerOpenApi {

    /*
       createCard
     */
    @Operation(operationId = "create-card", summary = "API call to create a new card.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Card to be created",
                    content = @Content(
                            schema = @Schema(implementation = CardDTO.class),
                            examples = {
                                    @ExampleObject(name = "Card", value = OpenApiExampleConstants.CARD_CREATE),
                            }
                    )
            ))
    @ApiResponse(responseCode = "200", description = "Successful Operation",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = CardDTO.class),
                    examples = {@ExampleObject(value = OpenApiExampleConstants.CARD_SUCCESS)}
            )})
    @ApiResponse(responseCode = "400", description = "Bad Request",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class),
                    examples = {@ExampleObject(value = OpenApiExampleConstants.INVALID_CREDENTIALS)}
            )})
    @ApiResponse(responseCode = "401", description = "Bad Request",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class),
                    examples = {@ExampleObject(value = OpenApiExampleConstants.CREATE_CARD_FAILED)}
            )})
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createCard(@Valid CardDTO card) throws Exception;

    /*
       getCardById
     */
    @Operation(operationId = "get-card", summary = "API call to get a card by the id.")
    @ApiResponse(responseCode = "200", description = "Successful Operation",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(name="Card", implementation = CardDTO.class),
                    examples = {@ExampleObject(value = OpenApiExampleConstants.CARD_SUCCESS)}
            )})
    @ApiResponse(responseCode = "401", description = "Unauthorized",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class),
                    examples = {@ExampleObject(value = OpenApiExampleConstants.INVALID_CREDENTIALS)}
            )})
    @ApiResponse(responseCode = "404", description = "Not Found",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class),
                    examples = {@ExampleObject(value = OpenApiExampleConstants.CARD_NOT_FOUND)}
            )})
    @ApiResponse(responseCode = "400", description = "Bad Request",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class),
                    examples = {@ExampleObject(value = OpenApiExampleConstants.INVALID_CARD_OWNERSHIP)}
            )})
    @GetMapping(value = "/{cardId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCardById(@PathVariable Long cardId) throws Exception;

    /*
        updateCard
     */
    @Operation(operationId = "patch-card", summary = "API call to update existing card.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Card to be updated",
                    content = @Content(
                            schema = @Schema(name = "Patch Operations", implementation = List.class),
                            examples = {
                                    @ExampleObject(name = "Card", value = OpenApiExampleConstants.PATCH_OPERATIONS),
                            }
                    )
            ))
    @ApiResponse(responseCode = "200", description = "Successful Operation",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = CardDTO.class),
                    examples = {@ExampleObject(value = OpenApiExampleConstants.CARD_SUCCESS)}
            )})
    @ApiResponse(responseCode = "401", description = "Unauthorized",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class),
                    examples = {@ExampleObject(value = OpenApiExampleConstants.INVALID_CREDENTIALS)}
            )})
    @ApiResponse(responseCode = "404", description = "Not Found",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class),
                    examples = {@ExampleObject(value = OpenApiExampleConstants.CARD_NOT_FOUND)}
            )})
    @ApiResponse(responseCode = "400", description = "Bad Request",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class),
                    examples = {@ExampleObject(value = OpenApiExampleConstants.INVALID_CARD_OWNERSHIP)}
            )})
    @PatchMapping(value = "/{cardId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateCard(@PathVariable Long cardId,@RequestBody List<JsonPatchOperation> patchOperations) throws Exception;

    /*
        getAllCards
     */

    @Operation(operationId = "get-all-cards", summary = "API call to get all cards")
    @ApiResponse(responseCode = "200", description = "Successful Operation",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Set.class),
                    examples = {@ExampleObject(value = OpenApiExampleConstants.ALL_CARDS)}
            )})
    @ApiResponse(responseCode = "200", description = "Successful Operation",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Set.class),
                    examples = {@ExampleObject(value = OpenApiExampleConstants.EMPTY_LIST)}
            )})
    @ApiResponse(responseCode = "401", description = "Unauthorized",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class),
                    examples = {@ExampleObject(value = OpenApiExampleConstants.INVALID_CREDENTIALS)}
            )})
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllCards() throws Exception;

    /*
        deleteCard
     */
    @Operation(operationId = "delete-card", summary = "API call to delete card by id")
    @ApiResponse(responseCode = "200", description = "Successful Operation")
    @ApiResponse(responseCode = "404", description = "Not Found",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class),
                    examples = {@ExampleObject(value = OpenApiExampleConstants.CARD_NOT_FOUND)}
            )})
    @ApiResponse(responseCode = "401", description = "Unauthorized",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class),
                    examples = {@ExampleObject(value = OpenApiExampleConstants.INVALID_CREDENTIALS)}
            )})
    @ApiResponse(responseCode = "400", description = "Bad Request",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class),
                    examples = {@ExampleObject(value = OpenApiExampleConstants.INVALID_CARD_OWNERSHIP)}
            )})
    @DeleteMapping(value = "/{cardId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteCard(@PathVariable Long cardId) throws Exception;

    /*
        searchCards
     */

    @Operation(operationId = "search-cards", summary = "API call search cards",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Search criteria",
                    content = @Content(
                            schema = @Schema(name="SearchResult",implementation = SearchRequest.class),
                            examples = {
                                    @ExampleObject(name = "Search criteria", value = OpenApiExampleConstants.SEARCH_REQUEST),
                            }
                    )
            ))
    @ApiResponse(responseCode = "200", description = "Successful Operation",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Set.class),
                    examples = {@ExampleObject(value = OpenApiExampleConstants.SEARCH_RESPONSE)}
            )})
    @ApiResponse(responseCode = "401", description = "Unauthorized",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class),
                    examples = {@ExampleObject(value = OpenApiExampleConstants.INVALID_CREDENTIALS)}
            )})
    @PostMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findCard(@RequestBody @Valid SearchRequest<CardDTO> searchRequest) throws CardsApiException;
}
