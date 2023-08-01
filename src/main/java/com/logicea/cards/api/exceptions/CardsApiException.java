package com.logicea.cards.api.exceptions;

import com.logicea.cards.api.payloads.CardsApiError;
import org.springframework.http.HttpStatus;


public class CardsApiException extends Exception{

    HttpStatus status;
    public CardsApiException(CardsApiError error, HttpStatus status, String... args){
        super(error.asJsonString(args));
        this.status = status;
    }

    public HttpStatus getStatus(){return this.status;}
}
