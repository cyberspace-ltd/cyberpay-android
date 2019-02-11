package com.example.cyberpay_android.network;

import java.io.Serializable;

public class ApiResponse<T> implements Serializable {

    private boolean succeeded;
    private T data;

    public ApiResponse() {
    }

    public boolean isSucceeded() {
        return succeeded;
    }

    public void setSucceeded(boolean succeeded) {
        this.succeeded = succeeded;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
