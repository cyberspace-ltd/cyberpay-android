package com.example.cyberpay_android.models;

import java.io.Serializable;

public class Transaction implements Serializable {

    private String currency = "NGN";

    private String merchantReference;

    private Double amountInKobo;

    private String customerName;

    private String customerEmail;

    private String customerMobile;


    private String returnUrl;

    private String description;

    private String transactionReference;//gotten after set charge

    private String Otp;//only if otp is required

    private String bankCode;
    private String accountNumber;
    private String accountName;


    public Transaction() {
    }


    public String getDescription() {
        return description;
    }


    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getMerchantReference() {
        return merchantReference;
    }

    public void setMerchantReference(String merchantReference) {
        this.merchantReference = merchantReference;
    }

    public Double getAmountInKobo() {
        return amountInKobo;
    }

    public void setAmountInKobo(Double amountInKobo) {
        this.amountInKobo = amountInKobo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }


    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getOtp() {
        return Otp;
    }

    public void setOtp(String otp) {
        this.Otp = otp;
    }


    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
