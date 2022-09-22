package com.abc.senki.model.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class SuccessResponse {
    private int status;
    private String message;
    private Map<String,Object> data;
    public SuccessResponse(){
        this.data = new HashMap<>();
    }

    public SuccessResponse( int status, String message, Map<String, Object> data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
    public SuccessResponse(int status, String message) {
        this.status = status;
        this.message = message;

    }



}
