package com.example.cyberpay_android.network;

public class VerifyTransactionResponse {

    private String status;

    private String message;

    private String reference;


    public VerifyTransactionResponse() {
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
