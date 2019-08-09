package com.example.cyberpay_android.ui;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.cyberpay_android.R;
import com.example.cyberpay_android.models.Charge;
import com.example.cyberpay_android.models.Transaction;
import com.example.cyberpay_android.network.BankResponse;
import com.example.cyberpay_android.repository.CyberPaySDK;
import com.ndroid.CoolEditText;

import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

public class CyberPayActivty extends AppCompatActivity {


    CoolEditText amount;
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

        editText_Card_Number = findViewById(R.id.editText_Card_Number);

        editText_Card_Date = findViewById(R.id.editText_Card_Date);

        editText_Card_cvv = findViewById(R.id.editText_Card_cvv);


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

        String amountText = amount.getText().toString().trim();
        Double amountValue = Double.parseDouble(amountText)*100;
        transaction = new Transaction();
        transaction.setAmountInKobo(amountValue);
        transaction.setDescription("Test transaction from Android SDK");
        transaction.setMerchantReference(String.valueOf(generateRandom(10)));

        String expiryDate = editText_Card_Date.getText().toString();

        String[] splitDate = expiryDate.split(Pattern.quote("/"));


        charge = new Charge();
        charge.setCardNameHolder("Sample Test");
        charge.setCardExpiryMonth(splitDate[0]);
        charge.setCardExpiryYear(splitDate[1]);
        charge.setCardNumber(editText_Card_Number.getText().toString());
        charge.setCardCvv(editText_Card_cvv.getText().toString());


        CyberPaySDK.getInstance().SetTransaction(transaction, new CyberPaySDK.TransactionCallback() {
            @Override
            public void onSuccess(String transactionReference) {

                ChargeCard();
            }

            @Override
            public void onOtpRequired(Transaction transaction) {

                //not needed for set transaction
            }

            @Override
            public void onSecure3dRequired(Transaction transaction) {

            }

            @Override
            public void onSecure3DMpgsRequired(Transaction transaction) {

            }

            @Override
            public void onError(Throwable error, Transaction transaction) {

                Toast.makeText(CyberPayActivty.this, "Error: " + transaction.getTransactionReference(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onBank(List<BankResponse> bankResponses) {

            }
        });
    }

    public void ChargeCard() {

        CyberPaySDK.getInstance().ChargeCard(charge, new CyberPaySDK.TransactionCallback() {
            @Override
            public void onSuccess(String transactionReference) {

                //This is called only when a transaction is successful
                Toast.makeText(CyberPayActivty.this, "Transaction successful: Transaction Ref: " + transaction.getTransactionReference(), Toast.LENGTH_LONG).show();

            }


            @Override
            public void onOtpRequired(Transaction transaction) {
                // This is called only when otp is required

                Toast.makeText(CyberPayActivty.this, "Otp Required Transaction Ref: " + transaction.getTransactionReference(), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(CyberPayActivty.this, OtpActivity.class);
                intent.putExtra(OtpActivity.PARAM_TRANSACTION, transaction);

                startActivity(intent);
            }

            @Override
            public void onSecure3dRequired(Transaction transaction) {
                Toast.makeText(CyberPayActivty.this, "Transaction successful: Transaction Ref: " + transaction.getTransactionReference(), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(CyberPayActivty.this, MakePaymentActivity.class);
                intent.putExtra(MakePaymentActivity.PARAM_TRANSACTION, transaction.getReturnUrl());
                startActivity(intent);
            }

            @Override
            public void onSecure3DMpgsRequired(Transaction transaction) {
                Toast.makeText(CyberPayActivty.this, "Transaction successful: Transaction Ref: " + transaction.getTransactionReference(), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(CyberPayActivty.this, MakePaymentActivity.class);
                intent.putExtra(MakePaymentActivity.PARAM_TRANSACTION, transaction.getReturnUrl());
                startActivity(intent);
            }

            @Override
            public void onError(Throwable error, Transaction transaction) {

                Toast.makeText(CyberPayActivty.this, "Error: " + transaction.getTransactionReference(), Toast.LENGTH_LONG).show();

            }

            @Override
            public void onBank(List<BankResponse> bankResponses) {
                Toast.makeText(CyberPayActivty.this, "Transaction successful: Transaction Ref: " + transaction.getTransactionReference(), Toast.LENGTH_LONG).show();

            }
        });

    }


}
