package com.albina.springproject.common;

public class ErrorResponse {

    private String error;

    public ErrorResponse(String message) {
        super();
        this.error = message;
    }

    public String getMessage() {
        return error;
    }

    public void setMessage(String message) {
        this.error = message;
    }
}
