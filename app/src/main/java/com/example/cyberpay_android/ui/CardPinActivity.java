package com.example.cyberpay_android.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.cyberpay_android.R;
import com.example.cyberpay_android.models.Card;
import com.example.cyberpay_android.models.Charge;
import com.example.cyberpay_android.models.ChargeBank;
import com.example.cyberpay_android.models.Transaction;
import com.example.cyberpay_android.network.BankResponse;
import com.example.cyberpay_android.repository.CyberPaySDK;

import java.util.List;

public class CardPinActivity extends AppCompatActivity implements CyberPaySDK.TransactionCallback {

    public static String PARAM_TRANSACTION = "PARAM_TRANSACTION";



    Charge charge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_pin);

        charge = (Charge) getIntent().getSerializableExtra(PARAM_TRANSACTION);

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

                charge.setCardPin(txtPinEntry.getText().toString());
                CyberPaySDK.getInstance().ChargeCard(charge, new CyberPaySDK.TransactionCallback() {
                    @Override
                    public void onProvidePin(Charge charge) {

                    }

                    @Override
                    public void onSuccess(String transactionReference) {
                        Toast.makeText(CardPinActivity.this, "Reference: " + transactionReference, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onOtpRequired(Charge transaction, Card card) {

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

                    }

                    @Override
                    public void onBank(List<BankResponse> bankResponses) {

                    }
                });
            }
        });

    }

    @Override
    public void onProvidePin(Charge charge) {

    }

    @Override
    public void onSuccess(String transactionReference) {

    }

    @Override
    public void onOtpRequired(Charge transaction, Card card) {

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

    }

    @Override
    public void onBank(List<BankResponse> bankResponses) {

    }
}
