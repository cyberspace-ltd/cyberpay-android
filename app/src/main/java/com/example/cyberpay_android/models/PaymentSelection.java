package com.example.cyberpay_android.models;

public class PaymentSelection {

    private String paymentType;

    private String paymentInfo;

    private int paymentIcon;

    private int id;

    public PaymentSelection(int id,String paymentType, String paymentInfo, int paymentIcon) {
        this.paymentType = paymentType;
        this.paymentInfo = paymentInfo;
        this.paymentIcon = paymentIcon;
        this.id = id;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(String paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    public int getPaymentIcon() {
        return paymentIcon;
    }

    public void setPaymentIcon(int paymentIcon) {
        this.paymentIcon = paymentIcon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
