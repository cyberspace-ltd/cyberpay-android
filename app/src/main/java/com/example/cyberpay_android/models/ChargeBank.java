package com.example.cyberpay_android.models;

public class ChargeBank {

    private String bankCode;
    private String accountNumber;
    private String reference;
    private String accountName;

    private String registeredPhoneNumber;
    private String Otp;//only if otp is required


    public ChargeBank() {
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
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

    public String getRegisteredPhoneNumber() {
        return registeredPhoneNumber;
    }

    public void setRegisteredPhoneNumber(String registeredPhoneNumber) {
        this.registeredPhoneNumber = registeredPhoneNumber;
    }

    public String getOtp() {
        return Otp;
    }

    public void setOtp(String otp) {
        this.Otp = otp;
    }

}
