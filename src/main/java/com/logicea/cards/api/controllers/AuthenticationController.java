package com.logicea.cards.api.controllers;

import com.logicea.cards.api.openapi.controllers.AuthenticationControllerOpenApi;
import com.logicea.cards.api.payloads.LoginRequest;
import com.logicea.cards.api.security.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("api/authenticate")
public class AuthenticationController implements AuthenticationControllerOpenApi {

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> authenticate(@RequestBody @Valid LoginRequest logInRequest) throws Exception {
        return authenticationService.createAuthToken(logInRequest);
    }
}
