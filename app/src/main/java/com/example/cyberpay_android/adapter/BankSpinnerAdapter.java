package com.example.cyberpay_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cyberpay_android.R;
import com.example.cyberpay_android.network.BankResponse;

import java.util.ArrayList;
import java.util.List;

public class BankSpinnerAdapter extends BaseAdapter {
    private Context context;
    private List<BankResponse> bankList;

    public BankSpinnerAdapter(Context context) {
        this.context = context;
        this.bankList = new ArrayList<>();
    }

    public void updateData(List<BankResponse> bankList){
        if(bankList != null){
            this.bankList.clear();
            this.bankList.addAll(bankList);
        }
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return bankList.size();
    }

    @Override
    public BankResponse getItem(int i) {
        return bankList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.list_item_spinner, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.banks = view.findViewById(R.id.bank_name);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        BankResponse bank = bankList.get(i);
        if(bank != null){
            String bankName = bank.getBankName();
            viewHolder.banks.setText(bankName);
            viewHolder.banks.setTag(bank.getId());
        }
        return view;
    }

    static class ViewHolder{
        TextView banks;
    }
}
