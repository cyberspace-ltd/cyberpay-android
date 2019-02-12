package com.example.cyberpay_android.repository;


import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.example.cyberpay_android.models.Charge;
import com.example.cyberpay_android.models.Transaction;
import com.example.cyberpay_android.network.ApiClient;
import com.example.cyberpay_android.network.ApiResponse;
import com.example.cyberpay_android.network.ChargeResponse;
import com.example.cyberpay_android.network.IApiService;
import com.example.cyberpay_android.network.MerchantTransactionResponse;
import com.example.cyberpay_android.network.OtpResponse;
import com.example.cyberpay_android.network.TransactionResponse;
import com.example.cyberpay_android.network.VerifyTransactionResponse;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CyberPaySDK {


    private static CyberPaySDK INSTANCE = null;

    private static final String TAG = CyberPaySDK.class.getSimpleName();

    private IApiService webService;


    private TransactionCallback transactionCallback;


    private Transaction transaction;

    private static String API_KEY = "";

    private static Boolean isStagingMode = false;


    public static Boolean getIsStagingMode() {
        return isStagingMode;
    }

    private CyberPaySDK() {
        webService = ApiClient.getIApiService();
    }

    public synchronized static CyberPaySDK getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CyberPaySDK();
        }

        return INSTANCE;
    }


    public synchronized static void initializeLiveEnvironment(String key) {

        API_KEY = key;
        isStagingMode = false;
    }

    public synchronized static void initializeTestEnvironment(String key) {

        API_KEY = key;
        isStagingMode = true;
    }


    //set transaction
    public void SetTransaction(Transaction trans, final TransactionCallback transactionCallback) {
        this.transaction = trans;

        Map<String, Object> jsonParams = new HashMap<>();
        jsonParams.put("Currency", transaction.getCurrency());

        if(!TextUtils.isEmpty(transaction.getMerchantReference())){
            jsonParams.put("MerchantRef", transaction.getMerchantReference());

        }
        jsonParams.put("Amount", transaction.getAmountInKobo());
        jsonParams.put("Description", transaction.getDescription());
        jsonParams.put("IntegrationKey", API_KEY);
        jsonParams.put("ReturnUrl", transaction.getReturnUrl());

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams))
                .toString());

        Call<ApiResponse<TransactionResponse>> call = webService.beginTransaction(body);

        call.enqueue(new Callback<ApiResponse<TransactionResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<TransactionResponse>> call, @NonNull Response<ApiResponse<TransactionResponse>> response) {

                if (response.body() == null)
                    return;

                if (response.body().getData() != null && response.body().isSucceeded()) {

                    transaction.setTransactionReference(response.body().getData().getTransactionReference());
                    transactionCallback.onSuccess(transaction.getTransactionReference());


                } else {


                    transactionCallback.onError(new Throwable("Transaction initiation failed."), transaction);

                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<TransactionResponse>> call, @NonNull Throwable t) {
                transactionCallback.onError(t, transaction);
            }
        });
    }

    //verify transaction
    public void VerifyTransaction(final Charge charge, final TransactionCallback transactionCallback) {

        final Call<ApiResponse<VerifyTransactionResponse>> call = webService.verifyTransaction(transaction.getTransactionReference());

        call.enqueue(new Callback<ApiResponse<VerifyTransactionResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<VerifyTransactionResponse>> call, @NonNull Response<ApiResponse<VerifyTransactionResponse>> response) {

                if (response.body() == null)
                    return;

                if (response.body().getData() != null && response.body().getData().getStatus().equals("Successful")) {

                    transactionCallback.onSuccess(transaction.getTransactionReference());

                }

            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<VerifyTransactionResponse>> call, @NonNull Throwable t) {
                transactionCallback.onError(t, transaction);
            }
        });
    }



    public void VerifyMerchantTransaction(final String merchantReference, final TransactionCallback transactionCallback) {

        final Call<ApiResponse<MerchantTransactionResponse>> call = webService.verifyMerchantTransaction(merchantReference);

        call.enqueue(new Callback<ApiResponse<MerchantTransactionResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<MerchantTransactionResponse>> call, @NonNull Response<ApiResponse<MerchantTransactionResponse>> response) {

                if (response.body() == null)//
                    return;

                if (response.body().getData() != null && response.body().getData().getStatus().equals("Successful")) {

                    //Updated Reference from the Response
                    transactionCallback.onSuccess(response.body().getData().getAdvice().getReference());
//                    transactionCallback.onSuccess(transaction.getTransactionReference());

                }

            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<MerchantTransactionResponse>> call, @NonNull Throwable t) {
                transactionCallback.onError(t, transaction);
            }
        });
    }



    public void ChargeCard(final Charge charge, final TransactionCallback transactionCallback) {

        Map<String, Object> jsonParams = new HashMap<>();
        jsonParams.put("Name", charge.getCardNameHolder());
        jsonParams.put("ExpiryMonth", charge.getCardExpiryMonth());
        jsonParams.put("ExpiryYear", charge.getCardExpiryYear());
        jsonParams.put("CardNumber", charge.getCardNumber());
        jsonParams.put("CVV", charge.getCardCvv());
        jsonParams.put("Reference", transaction.getTransactionReference());

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams))
                .toString());

        Call<ApiResponse<ChargeResponse>> call = webService.chargeCard(body);
        call.enqueue(new Callback<ApiResponse<ChargeResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<ChargeResponse>> call, @NonNull Response<ApiResponse<ChargeResponse>> response) {

                if (response.body() == null)
                    return;

                if (response.body().getData() != null && response.body().getData().getStatus().equals("Success")) {

                    transactionCallback.onSuccess(transaction.getTransactionReference());

                }
                if (response.body().getData() != null && response.body().getData().getStatus().equals("Successful")) {

                    transactionCallback.onSuccess(transaction.getTransactionReference());


                } else if (response.body().getData() != null && response.body().getData().getStatus().equals("Otp")) {
                    transactionCallback.onOtpRequired(transaction);

                } else {

                    transactionCallback.onError(new Throwable(response.body().getData().getMessage()), transaction);

                }

            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<ChargeResponse>> call, @NonNull Throwable t) {

                transactionCallback.onError(t, transaction);

            }
        });

    }


    public void VerifyOtp(final Transaction transaction, final TransactionCallback transactionCallback) {

        Map<String, Object> jsonParams = new HashMap<>();
        jsonParams.put("otp", transaction.getOtp());
        jsonParams.put("reference", transaction.getTransactionReference());

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams))
                .toString());

        Call<ApiResponse<OtpResponse>> call = webService.verifyOtp(body);

        call.enqueue(new Callback<ApiResponse<OtpResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<OtpResponse>> call, @NonNull Response<ApiResponse<OtpResponse>> response) {


                assert response.body() != null;
                if (response.body().getData() != null && response.body().getData().getStatus().equals("Successful")) {

                    transactionCallback.onSuccess(transaction.getTransactionReference());

                } else if (response.body().getData() != null && response.body().getData().getStatus().equals("Failed")) {

                    transactionCallback.onError(new Throwable(response.body().getData().getMessage()), transaction);
                }

            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<OtpResponse>> call, @NonNull Throwable t) {
                transactionCallback.onError(t, transaction);
            }
        });
    }


    public interface TransactionCallback {

        void onSuccess(String transactionReference);

        void onOtpRequired(Transaction transaction);

        void onError(Throwable error, Transaction transaction);
    }
}
