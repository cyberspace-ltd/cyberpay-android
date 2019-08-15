package com.example.cyberpay_android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cyberpay_android.R;
import com.example.cyberpay_android.adapter.BankSpinnerAdapter;
import com.example.cyberpay_android.models.Charge;
import com.example.cyberpay_android.models.Transaction;
import com.example.cyberpay_android.network.BankResponse;
import com.example.cyberpay_android.repository.CyberPaySDK;
import com.ndroid.CoolEditText;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

import static com.example.cyberpay_android.ui.CyberPayActivty.generateRandom;


public class BankAccountActivity extends AppCompatActivity {

    CoolEditText editText_Account_Number, editText_Account_Name;
    CoolEditText amount;
    Spinner bankSpinner;
    Charge chargeBank;
    BankSpinnerAdapter adapter;
    String bankCode;
    String bankName;
    String bankAccountName, bankAccountNumber;

    Transaction transaction;
    private SpotsDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_account);
        loadBanks();

        CyberPaySDK.initializeTestEnvironment("d5355204f9cf495f853c8f8d26ada19b");

        amount = findViewById(R.id.amount);

        editText_Account_Name = findViewById(R.id.card_name);

        bankSpinner = findViewById(R.id.banksSpinner);

        editText_Account_Number = findViewById(R.id.account_number);

        adapter = new BankSpinnerAdapter(this);
        bankSpinner.setAdapter(adapter);

        bankSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                bankName = bankSpinner.getItemAtPosition(bankSpinner.getSelectedItemPosition()).toString();
                bankSpinner.getSelectedItem().toString();

                int bankID = bankSpinner.getSelectedItemPosition();
                bankCode = adapter.getItem(bankID).getBankCode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                bankCode = "057";
            }
        });
        findViewById(R.id.payBankButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInput();
            }
        });

    }

    private void loadBanks() {
        CyberPaySDK.getInstance().getBank(new CyberPaySDK.TransactionCallback() {
            @Override
            public void onSuccess(String transactionReference) {

            }

            @Override
            public void onOtpRequired(Transaction transaction) {

            }

            @Override
            public void onSecure3dRequired(Transaction transaction) {

            }

            @Override
            public void onSecure3DMpgsRequired(Transaction transaction) {

            }

            @Override
            public void onError(Throwable error, Transaction transaction) {

            }

            @Override
            public void onBank(List<BankResponse> response) {
                List<BankResponse> temp = new ArrayList<>();
                if(response!= null) {
                    temp.addAll(response);
                    adapter.updateData(temp);
                }
            }
        });
    }

    private void validateInput() {
        boolean valid = true;

        if(amount.getText().toString().trim().length() == 0){
            valid = false;
            amount.setError("Amount is needed");
        }

        if(editText_Account_Number.getText().toString().trim().length() == 0){
            valid = false;
            editText_Account_Number.setError("Account Number is needed");
        }

        if(editText_Account_Name.getText().toString().trim().length() == 0){
            valid = false;
            editText_Account_Name.setError("Account Name is needed");
        }


        if(valid)
            beginTransaction();

    }

    private void beginTransaction() {

        progressDialog = new SpotsDialog(BankAccountActivity.this);
        progressDialog.show();
        String amountText = amount.getText().toString().trim();
        Double amountValue = Double.parseDouble(amountText)*100;
        transaction = new Transaction();
        transaction.setAmountInKobo(amountValue);
        transaction.setDescription("Test transaction from Android SDK");
        transaction.setMerchantReference(String.valueOf(generateRandom(10)));

        bankAccountName = editText_Account_Name.getText().toString().trim();
        bankAccountNumber = editText_Account_Number.getText().toString().trim();

        chargeBank = new Charge();
        chargeBank.setBankCode(bankCode);
        chargeBank.setAccountName(bankAccountName);
        chargeBank.setAccountNumber(bankAccountNumber);


        CyberPaySDK.getInstance().SetTransaction(transaction, new CyberPaySDK.TransactionCallback() {
            @Override
            public void onSuccess(String transactionReference) {
                Toast.makeText(BankAccountActivity.this, "Transaction successful: Transaction Ref: " + transactionReference, Toast.LENGTH_LONG).show();

                progressDialog.dismiss();
                ChargeBank();

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

                Toast.makeText(BankAccountActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onBank(List<BankResponse> bankResponses) {

            }
        });

    }

    private void ChargeBank() {

        CyberPaySDK.getInstance().ChargeBank(chargeBank, new CyberPaySDK.TransactionCallback() {
            @Override
            public void onSuccess(String transactionReference) {


                //This is called only when a transaction is successful
                Toast.makeText(BankAccountActivity.this, "Transaction successful: Transaction Ref: " + transactionReference, Toast.LENGTH_LONG).show();

            }


            @Override
            public void onOtpRequired(Transaction transaction) {
                // This is called only when otp is required

                Toast.makeText(BankAccountActivity.this, "Otp Required Transaction Ref: " + transaction.getTransactionReference(), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(BankAccountActivity.this, OtpBankActivity.class);
                intent.putExtra(OtpActivity.PARAM_TRANSACTION, transaction);

                startActivity(intent);
            }

            @Override
            public void onSecure3dRequired(Transaction transaction) {

            }

            @Override
            public void onSecure3DMpgsRequired(Transaction transaction) {

            }

            @Override
            public void onError(Throwable error, Transaction transaction) {

                Toast.makeText(BankAccountActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onBank(List<BankResponse> bankResponses) {

            }
        });
    }
}
