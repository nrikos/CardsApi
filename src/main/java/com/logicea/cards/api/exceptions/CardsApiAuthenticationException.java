package com.logicea.cards.api.exceptions;

import com.logicea.cards.api.payloads.CardsApiError;

import javax.naming.AuthenticationException;

public class CardsApiAuthenticationException extends AuthenticationException {
    public CardsApiAuthenticationException(CardsApiError error, String... args){
        super(error.asJsonString(args));
    }
}
