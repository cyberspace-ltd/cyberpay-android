package com.example.cyberpay_android.models;

import org.json.JSONObject;


import java.io.Serializable;


public class Charge implements Serializable {

//    private String accountName;

    private String reference;
//    private JSONObject card;
    private String Otp;//only if otp is required
    private String returnUrl;



//    private String accountNumber;

    private String cardPin;

//    @SerializedName("name")
//    private String cardNameHolder;


    private String expiryMonth;

    private String expiryYear;

    private String cardNumber;

    private String cvv;

    private String registeredPhoneNumber;

    public Charge() {
    }

//    public String getCardNameHolder() {
//        return cardNameHolder;
//    }
//
//    public void setCardNameHolder(String cardNameHolder) {
//        this.cardNameHolder = cardNameHolder;
//    }

    public String getExpiryMonth() {
        return expiryMonth;
    }

    public void setExpiryMonth(String expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    public String getExpiryYear() {
        return expiryYear;
    }

    public void setExpiryYear(String expiryYear) {
        this.expiryYear = expiryYear;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

//    public String getAccountName() {
//        return accountName;
//    }
//
//    public void setAccountName(String accountName) {
//        this.accountName = accountName;
//    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }


//    public String getAccountNumber() {
//        return accountNumber;
//    }
//
//    public void setAccountNumber(String accountNumber) {
//        this.accountNumber = accountNumber;
//    }

    public String getOtp() {
        return Otp;
    }

    public void setOtp(String otp) {
        this.Otp = otp;
    }

//    public JSONObject getCard() {
//        return card;
//    }
//
//    public void setCard(JSONObject card) {
//        this.card = card;
//    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getCardPin() {
        return cardPin;
    }

    public void setCardPin(String cardPin) {
        this.cardPin = cardPin;
    }

    public String getExpiry() {
        return expiryMonth + "20"+ expiryYear;
    }

    public String getRegisteredPhoneNumber() {
        return registeredPhoneNumber;
    }

    public void setRegisteredPhoneNumber(String registeredPhoneNumber) {
        this.registeredPhoneNumber = registeredPhoneNumber;
    }

}
