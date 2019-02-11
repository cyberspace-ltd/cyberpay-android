package com.example.cyberpay_android.network;

import java.io.Serializable;

public class TransactionResponse implements Serializable {

    private String transactionReference;

    private String charge;

    private String redirectUrl;


    public TransactionResponse() {
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
