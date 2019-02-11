package com.example.cyberpay_android.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cyberpay_android.R;
import com.example.cyberpay_android.models.PaymentSelection;

import java.util.ArrayList;

public class PaymentSelectionAdapter extends RecyclerView.Adapter<PaymentSelectionAdapter.ViewHolder> {

    private ArrayList<PaymentSelection> paymentSelections;

    private OnPaymentSelectedListener onPaymentSelectedListener;

    public PaymentSelectionAdapter(ArrayList<PaymentSelection> paymentSelections, OnPaymentSelectedListener onPaymentSelectedListener) {
        this.paymentSelections = paymentSelections;
        this.onPaymentSelectedListener = onPaymentSelectedListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cyberpay_selection_row_item, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        final PaymentSelection selection = paymentSelections.get(i);

        viewHolder.paymentImageView.setImageResource(selection.getPaymentIcon());
        viewHolder.paymentTypeTextView.setText(selection.getPaymentType());
        viewHolder.paymentInfoTextView.setText(selection.getPaymentInfo());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPaymentSelectedListener != null) {
                    onPaymentSelectedListener.onPaymentMethodSelected(selection);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return paymentSelections.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        ImageView paymentImageView;
        TextView paymentTypeTextView;
        TextView paymentInfoTextView;

        ViewHolder(View v) {
            super(v);

            paymentImageView = v.findViewById(R.id.paymentImageView);

            paymentTypeTextView = v.findViewById(R.id.paymentTypeTextView);

            paymentInfoTextView = v.findViewById(R.id.paymentInfoTextView);

        }
    }

    public interface OnPaymentSelectedListener {

        void onPaymentMethodSelected(PaymentSelection selection);
    }
}
