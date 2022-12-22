package com.abc.senki.common;

public enum OrderStatus {
    PENDING("pending"),
    PROCESSING("processing"),
    SHIPPING("shipping"),
    DELIVERED("delivered"),
    CANCELLED("cancelled");

    private final String message;

    OrderStatus(String message) {
        this.message=message;
    }

    public String getMessage(){
        return message;
    }

}
