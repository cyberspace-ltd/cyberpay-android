package com.example.cyberpay_android.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.cyberpay_android.R;
import com.example.cyberpay_android.models.Transaction;
import com.example.cyberpay_android.network.BankResponse;
import com.example.cyberpay_android.repository.CyberPaySDK;

import java.util.List;

import dmax.dialog.SpotsDialog;

public class OtpActivity extends AppCompatActivity implements CyberPaySDK.TransactionCallback {


    public static String PARAM_TRANSACTION = "PARAM_TRANSACTION";



    Transaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);


        transaction = (Transaction) getIntent().getSerializableExtra(PARAM_TRANSACTION);


        final PinEntryEditText txtPinEntry = findViewById(R.id.pinEditText);
        txtPinEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

//                TransactionRepository.getInstance().VerifyOtp(transaction, OtpActivity.this);

            }
        });

        findViewById(R.id.payButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                transaction.setOtp(txtPinEntry.getText().toString());
                CyberPaySDK.getInstance().VerifyOtp(transaction, OtpActivity.this);
            }
        });
    }


    @Override
    public void onSuccess(String transactionReference) {
        Toast.makeText(this, "Transaction successful: Transaction Ref: " + transactionReference, Toast.LENGTH_LONG).show();
        startActivity(new Intent(OtpActivity.this, CyberpayPaymentSelectionActivity.class));
    }

    @Override
    public void onOtpRequired(Transaction transaction) {
        //not needed
    }

    @Override
    public void onSecure3dRequired(Transaction transaction) {

    }

    @Override
    public void onSecure3DMpgsRequired(Transaction transaction) {

    }

    @Override
    public void onError(Throwable error, Transaction transaction) {
        Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();

    }

    @Override
    public void onBank(List<BankResponse> bankResponses) {

    }
}
