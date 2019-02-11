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
import com.example.cyberpay_android.repository.CyberPaySDK;

import java.util.regex.Pattern;

public class CyberPayActivty extends AppCompatActivity {


    CardNumberEditText editText_Card_Number;

    ExpiryDateEditText editText_Card_Date;

    TextInputEditText editText_Card_cvv;


    Charge charge;

    Transaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cyberpay);


        CyberPaySDK.initializeTestEnvironment("e2d0ff2733ba470ca1f7a4cb98a190dc");

        editText_Card_Number = findViewById(R.id.editText_Card_Number);

        editText_Card_Date = findViewById(R.id.editText_Card_Date);

        editText_Card_cvv = findViewById(R.id.editText_Card_cvv);


        findViewById(R.id.payButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BeginTransaction();
            }
        });
    }


    private void BeginTransaction() {

        transaction = new Transaction();
        transaction.setAmountInKobo(10000.00);
        transaction.setDescription("Test transaction from Android SDK");


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
            public void onError(Throwable error, Transaction transaction) {

                Toast.makeText(CyberPayActivty.this, "Error: " + transaction.getTransactionReference(), Toast.LENGTH_LONG).show();

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
            public void onError(Throwable error, Transaction transaction) {

                Toast.makeText(CyberPayActivty.this, "Error: " + transaction.getTransactionReference(), Toast.LENGTH_LONG).show();

            }
        });

    }


}
