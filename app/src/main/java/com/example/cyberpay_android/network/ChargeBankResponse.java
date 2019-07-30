package com.example.cyberpay_android.network;

import java.io.Serializable;

public class ChargeBankResponse implements Serializable {

    private String status;

    private String message;


    private String responseAction;


    public ChargeBankResponse() {
    }


    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

}
