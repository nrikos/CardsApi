package com.logicea.cards.api.openapi.constants;

public interface OpenApiExampleConstants {

    String LOG_IN = """
            {
                "email":"jane.doe@yahoo.com",
                "password":"password"
            }""";

    String LOG_IN_RESPONSE = """
            {
                "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqYW5lLmRvZUB5YWhvby5jb20iLCJyb2xlIjpbeyJhdXRob3JpdHkiOiJST0xFX0FETUlOIn1dLCJleHAiOjE2OTA4NDQ0OTAsImlhdCI6MTY5MDgyNjQ5MCwiZW1haWwiOiJqYW5lLmRvZUB5YWhvby5jb20ifQ.dVkCfWD_WXcAGQmM3Djb__ECnV7vidjjz7foEQs6z7s"
            }""";
    String NO_CONTENT = """
            """;

    String CARD_CREATE = """
            {
                "color": "#FF5609",
                "description": "Some",
                "name": "some card"
            }""";
    String CARD_SUCCESS = """
            {
                "cardId": 1,
                "status": "TO_DO",
                "color": "#FF5609",
                "description": "Some",
                "name": "some card",
                "createdAt": "2023-07-31 15:38:51"
            }""";

    String PATCH_OPERATIONS = """
            [
               { "op": "replace", "path": "color", "value": "#FF4567" },
               { "op": "remove", "path": "description"}
            ]""";
    String EMPTY_LIST = """
            []""";
    String ALL_CARDS = """
            [
                {
                    "cardId": 1,
                    "status": "TO_DO",
                    "color": "#FF5609",
                    "description": "Some",
                    "name": "some card",
                    "createdAt": "2023-07-31 16:32:04"
                },
                {
                    "cardId": 2,
                    "status": "TO_DO",
                    "color": "#FF5609",
                    "description": "Some",
                    "name": "some card",
                    "createdAt": "2023-07-31 16:31:58"
                }
                {
                    "cardId": 3,
                    "status": "TO_DO",
                    "color": "#FF5609",
                    "description": "Some",
                    "name": "some card",
                    "createdAt": "2023-07-31 16:31:58"
                }
            ]""";
    String CREATE_CARD_FAILED = """
             {"errorCode":"CARDS-E-002","error":"Card name is mandatory"}""";

    String CARD_NOT_FOUND = """
             {"errorCode":"CARDS-E-002","error":"Card not found"}""";

    String INVALID_CARD_OWNERSHIP= """
            {"errorCode":"CARDS-E-004","error":"Card does not belong to user"}""";
    String INVALID_CREDENTIALS = """
            {"errorCode":"CARDS-E-008","error":"Invalid credentials"}""";

    String SEARCH_REQUEST = """
            {
              "resultsPerPage": 10,
              "pageNumber": 0,
              "sortField": "name",
              "sortOrder": "ascending",
              "example": {
                "name": "new"
              }
            }""";

    String SEARCH_RESPONSE = """
            {
                "searchResult": [
                    {
                        "cardId": 6,
                        "status": "TO_DO",
                        "color": "#FF0609",
                        "description": "Some",
                        "name": "new card",
                        "createdAt": "2023-07-31 17:17:07"
                    },
                    {
                        "cardId": 5,
                        "status": "TO_DO",
                        "color": "#FF0609",
                        "description": "Some",
                        "name": "some card",
                        "createdAt": "2023-07-31 17:16:59"
                    }
                ],
                "total": 2
            }""";
}
