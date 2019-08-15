package com.example.cyberpay_android.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.inputmethod.InputMethodManager;

import com.example.cyberpay_android.ui.CyberpayPaymentSelectionActivity;
import com.example.cyberpay_android.ui.OtpBankActivity;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AppUtility {

    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNework = connectivityManager.getActiveNetworkInfo();
        return activeNework !=null;
    }

    public static void hideKeyboard(Activity context){
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(context.getCurrentFocus() != null){
            inputMethodManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static String formatDate(String value) {
        String formatted = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = formatter.parse(value.replace('T', ' '));
            SimpleDateFormat newFormat = new SimpleDateFormat("MMM dd, yyyy");
            formatted = newFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatted;
    }

    public static String converData(double bytes) {

        boolean si = false;
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }
    public static String formatNumber(double numberToFormat) {
        DecimalFormat df = new DecimalFormat("#,###.00");
        Date d1 = null;
        String dateString = "";
        try{
            dateString = df.format(numberToFormat);
            if(dateString.equalsIgnoreCase(".00")){
                dateString = "0.00";
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        if(numberToFormat==0)dateString="0.00";
        return dateString;
    }

    public static String getCurrentDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }

    public static void okDialog(Context context, String title, String message){
        new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", (dialogInterface, i) -> {
                    context.startActivity(new Intent(context, CyberpayPaymentSelectionActivity.class));

                })
                .show();
    }

    public static void PaymentDialog(Context context, String title, String message){
        new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", (dialogInterface, i) -> {
                    //context.startActivity(new Intent(context, MainActivity.class));
                })
                .show();
    }

}
