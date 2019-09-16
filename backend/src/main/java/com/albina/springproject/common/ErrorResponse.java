package com.albina.springproject.common;

public class ErrorResponse {

    private String error;

    public ErrorResponse(String error) {
        super();
        this.error = error;
    }

    public String getMessage() {
        return error;
    }

    public void setMessage(String message) {
        this.error = message;
    }
}
