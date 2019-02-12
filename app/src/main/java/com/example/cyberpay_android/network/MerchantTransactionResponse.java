package com.example.cyberpay_android.network;

import java.io.Serializable;

public class MerchantTransactionResponse implements Serializable {


    private String status;

    private String processorCode;

    private Advice advice;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProcessorCode() {
        return processorCode;
    }

    public void setProcessorCode(String processorCode) {
        this.processorCode = processorCode;
    }

    public Advice getAdvice() {
        return advice;
    }

    public void setAdvice(Advice advice) {
        this.advice = advice;
    }

    public MerchantTransactionResponse() {
    }
}
