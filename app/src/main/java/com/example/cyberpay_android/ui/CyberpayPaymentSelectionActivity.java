package com.example.cyberpay_android.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.cyberpay_android.R;
import com.example.cyberpay_android.models.PaymentSelection;

import java.util.ArrayList;

public class CyberpayPaymentSelectionActivity extends AppCompatActivity implements PaymentSelectionAdapter.OnPaymentSelectedListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cyberpay_payment_selection);
        mRecyclerView = findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        ArrayList<PaymentSelection> paymentSelections = new ArrayList<>();
        paymentSelections.add(new PaymentSelection(1, "Card Payment", "Pay with MasterCard, Visa, or Verve", R.drawable.ic_credit_card_colored));
        paymentSelections.add(new PaymentSelection(2, "Bank", "Pay directly from your bank account", R.drawable.ic_bank_building));
//        paymentSelections.add(new PaymentSelection(3, "QR Code", "Pay quickly by scanning a QR Code", R.drawable.ic_qr_code));


        mAdapter = new PaymentSelectionAdapter(paymentSelections, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onPaymentMethodSelected(PaymentSelection selection) {

        switch (selection.getId()) {
            case 1:
                startActivity(new Intent(this, CyberPayActivty.class));
                break;
            case 2:
                startActivity(new Intent(this, BankAccountActivity.class));
                break;
            default:
                Toast.makeText(this, "Feature not yet available", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
