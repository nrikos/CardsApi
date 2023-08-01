package com.logicea.cards.api.payloads;


import java.text.MessageFormat;
import java.util.Locale;

public enum CardsApiError {


    INVALID_SEARCH_TYPE("CARDS-E-001","{0} not a valid search filter"),
    VALIDATION_ERROR("CARDS-E-002","{0}"),
    INVALID_CREDENTIALS("CARDS-E-003","Invalid Credentials"),
    USER_DOES_NOT_HAVE_OWNERSHIP("CARDS-E-004","Card does not belong to user"),
    CARD_NOT_FOUND("CARDS-E-005","Card not found"),
    USER_NOT_FOUND("CARDS-E-006","User not found"),
    EXPIRED_USER("CARDS-E-007","Token has expired"),
    UNAUTHORIZED("CARDS-E-008","Invalid credentials"),
    MISSING_TOKEN("CARDS-E-009",""),
    EXPIRED_TOKEN("CARDS-E-010",""),
    INVALID_TOKEN("CARDS-E-0","");



    private final static String JSON_FORMAT = "{\"errorCode\":\"%s\",\"error\":\"%s\"}";
    private final String code;
    private final String error;


    public String getCode(){return this.code;}

    public String getError(){return this.error;}
    CardsApiError(String code, String error){
        this.code =code;
        this.error = error;
    }

    public String asJsonString(String... args) {
        MessageFormat messageFormat = new MessageFormat(this.getError(), Locale.getDefault());
        messageFormat.format(args);
        return String.format(String.format(JSON_FORMAT, this.code, messageFormat.format(args)));
    }

}
