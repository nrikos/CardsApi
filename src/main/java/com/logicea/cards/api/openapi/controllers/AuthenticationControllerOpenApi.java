package com.logicea.cards.api.openapi.controllers;

import com.logicea.cards.api.openapi.constants.OpenApiExampleConstants;
import com.logicea.cards.api.payloads.JwtTokenResponse;
import com.logicea.cards.api.payloads.LoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Tag(name ="Authentication")
public interface AuthenticationControllerOpenApi {

    @Operation(operationId = "authenticate", summary = "API to authenticate user",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Authentication credentials",
                    content = @Content(
                            schema = @Schema(implementation = LoginRequest.class),
                            examples = {
                                    @ExampleObject(name = "Authentication credentials", value = OpenApiExampleConstants.LOG_IN),
                            }
                    )
            ))
    @ApiResponse(responseCode = "200", description = "Successful Operation",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = JwtTokenResponse.class),
                    examples = {@ExampleObject(value = OpenApiExampleConstants.LOG_IN_RESPONSE)}
            )})
    @ApiResponse(responseCode = "400", description = "Bad Request",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class),
                    examples = {@ExampleObject(value = OpenApiExampleConstants.INVALID_CREDENTIALS)}
            )})
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> authenticate(@RequestBody @Valid LoginRequest logInRequest)throws Exception;
}
