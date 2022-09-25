package com.abc.senki.common;

import lombok.Getter;

@Getter
public enum ErrorDefinition {
    ACCESS_TOKEN_EXPIRED("access token is expired"),
    USER_NOT_FOUND("User not found"),
    ERROR_TRY_AGAIN("Error, please try again"),
    LIST_PRODUCT_EMPTY("List product is empty"),
    PRODUCT_NOT_FOUND("Product not found"),
    SORTING_TYPE_NOT_FOUND("Sorting type not found"),;
    private final String message;

    ErrorDefinition(String message) {
        this.message=message;
    }
}
