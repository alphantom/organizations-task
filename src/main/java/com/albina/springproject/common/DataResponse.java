package com.albina.springproject.common;

public class DataResponse<T> {

    private T data;

    public DataResponse(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
