package com.example.cyberpay_android.ui;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cyberpay_android.R;
import com.example.cyberpay_android.models.Card;
import com.example.cyberpay_android.models.Charge;
import com.example.cyberpay_android.models.ChargeBank;
import com.example.cyberpay_android.models.Transaction;
import com.example.cyberpay_android.network.BankResponse;
import com.example.cyberpay_android.repository.CyberPaySDK;
import com.ndroid.CoolEditText;

import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import dmax.dialog.SpotsDialog;

public class CyberPayActivty extends AppCompatActivity {

    private SpotsDialog progressDialog;

    CoolEditText amount;
    CoolEditText editText_pin;
    TextInputLayout cardPin;
    TextView cardTextView;
    CardNumberEditText editText_Card_Number;

    ExpiryDateEditText editText_Card_Date;

    TextInputEditText editText_Card_cvv;


    Charge charge;

    Transaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cyberpay);


        CyberPaySDK.initializeTestEnvironment("d5355204f9cf495f853c8f8d26ada19b");

        amount = findViewById(R.id.amount);
        editText_pin = findViewById(R.id.editText_pin);
        cardPin = findViewById(R.id.pin_text_input);
        cardTextView = findViewById(R.id.label_pin_number);

        editText_Card_Number = findViewById(R.id.editText_Card_Number);

        editText_Card_Date = findViewById(R.id.editText_Card_Date);

        editText_Card_cvv = findViewById(R.id.editText_Card_cvv);

        editText_Card_Number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.length() == 19){
                    cardPin.setVisibility(View.VISIBLE);
                    cardTextView.setVisibility(View.VISIBLE);
                } else {
                    cardPin.setVisibility(View.GONE);
                    cardTextView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        findViewById(R.id.payButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInput();
            }
        });
    }
    private void validateInput() {
        boolean valid = true;

        if(amount.getText().toString().trim().length() == 0){
            valid = false;
            amount.setError("Amount is needed");
        }
        if(editText_Card_Number.getText().toString().trim().length() == 0){
            valid = false;
            editText_Card_Number.setError("Card Number is needed");
        }

        if(editText_Card_Date.getText().toString().trim().length() == 0){
            valid = false;
            editText_Card_Date.setError("Expiry Date is needed");
        }
        if(editText_Card_cvv.getText().toString().trim().length() == 0){
            valid = false;
            editText_Card_cvv.setError("CVV is needed");
        }

        if(valid)
            BeginTransaction();

    }


    public static long generateRandom(int length) {
        Random random = new Random();
        char[] digits = new char[length];
        digits[0] = (char) (random.nextInt(9) + '1');
        for (int i = 1; i < length; i++) {
            digits[i] = (char) (random.nextInt(10) + '0');
        }
        return Long.parseLong(new String(digits));
    }

    private void BeginTransaction() {

        progressDialog = new SpotsDialog(CyberPayActivty.this);
        progressDialog.show();

        String amountText = amount.getText().toString().trim();
        String pinText = editText_pin.getText().toString().trim();
        Double amountValue = Double.parseDouble(amountText)*100;
        transaction = new Transaction();
        transaction.setAmountInKobo(amountValue);
        transaction.setDescription("Test transaction from Android SDK");
        transaction.setMerchantReference(String.valueOf(generateRandom(10)));
        transaction.setReturnUrl("https://www.google.com/");
        transaction.setCustomerName("Sample Test");
        transaction.setCustomerEmail("sampl@gmail.com");
        transaction.setCustomerMobile("2347039555295");

        String expiryDate = editText_Card_Date.getText().toString();

        String[] splitDate = expiryDate.split(Pattern.quote("/"));


        charge = new Charge();
//        charge.setCardNameHolder("Sample Test");
        charge.setExpiryMonth(splitDate[0]);
        charge.setExpiryYear(splitDate[1]);
        charge.setCardNumber(editText_Card_Number.getText().toString());
        charge.setCvv(editText_Card_cvv.getText().toString());
        if(pinText.length()!= 0 )
            charge.setCardPin(pinText);


        CyberPaySDK.getInstance().SetTransaction(transaction, new CyberPaySDK.TransactionCallback() {
            @Override
            public void onProvidePin(Charge charge) {

            }

            @Override
            public void onSuccess(String transactionReference) {
                Toast.makeText(CyberPayActivty.this, "Reference: " + transactionReference, Toast.LENGTH_LONG).show();

                charge.setReference(transaction.getTransactionReference());
                progressDialog.dismiss();
                ChargeCard();
            }

            @Override
            public void onOtpRequired(Charge transaction, Card card) {

                //not needed for set transaction
            }

            @Override
            public void onBankOtpRequired(ChargeBank transaction) {

            }

            @Override
            public void onSecure3dRequired(Charge transaction) {

            }

            @Override
            public void onSecure3DMpgsRequired(Charge transaction) {

            }

            @Override
            public void onEnrolOtp(Charge transaction) {

            }

            @Override
            public void onError(Throwable error, Transaction transaction) {

                Toast.makeText(CyberPayActivty.this, "Error: " + error, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onBank(List<BankResponse> bankResponses) {

            }
        });
    }

    public void ChargeCard() {

        CyberPaySDK.getInstance().ChargeCard(charge, new CyberPaySDK.TransactionCallback() {
            @Override
            public void onProvidePin(Charge charge) {
                Toast.makeText(CyberPayActivty.this, " Card Pin Required Transaction Ref: " + charge.getReference(), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(CyberPayActivty.this, CardPinActivity.class);

                intent.putExtra(CardPinActivity.PARAM_TRANSACTION, charge);

                startActivity(intent);
                finish();
            }

            @Override
            public void onSuccess(String transactionReference) {

                //This is called only when a transaction is successful
                Toast.makeText(CyberPayActivty.this, "Transaction successful: Transaction Ref: " + charge.getReference(), Toast.LENGTH_LONG).show();

            }


            @Override
            public void onOtpRequired(Charge transaction, Card card) {
                // This is called only when otp is required

                Toast.makeText(CyberPayActivty.this, "Otp Required Transaction Ref: " + transaction.getReference(), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(CyberPayActivty.this, OtpActivity.class);
                intent.putExtra(OtpActivity.PARAM_TRANSACTION, transaction);
                intent.putExtra(OtpActivity.PARAM_CARD, card.getCard().toString());

                startActivity(intent);
                finish();
            }

            @Override
            public void onBankOtpRequired(ChargeBank transaction) {

            }

            @Override
            public void onSecure3dRequired(Charge transaction) {
                Toast.makeText(CyberPayActivty.this, "Transaction successful: Transaction Ref: " + transaction.getReference(), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(CyberPayActivty.this, MakePaymentActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(MakePaymentActivity.PARAM_TRANSACTION, transaction.getReturnUrl());
                startActivity(intent);
                finish();
            }

            @Override
            public void onSecure3DMpgsRequired(Charge transaction) {
                Toast.makeText(CyberPayActivty.this, "Transaction successful: Transaction Ref: " + transaction.getReference(), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(CyberPayActivty.this, MakePaymentActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(MakePaymentActivity.PARAM_TRANSACTION, transaction.getReturnUrl());
                startActivity(intent);
                finish();
            }

            @Override
            public void onEnrolOtp(Charge transaction) {

            }

            @Override
            public void onError(Throwable error, Transaction transaction) {

                Toast.makeText(CyberPayActivty.this, "Error: " + error, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onBank(List<BankResponse> bankResponses) {
                Toast.makeText(CyberPayActivty.this, "Transaction successful: Transaction Ref: " + transaction.getTransactionReference(), Toast.LENGTH_LONG).show();

            }
        });

    }


}
