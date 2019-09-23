package com.example.cyberpay_android.repository;


import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.example.cyberpay_android.models.Card;
import com.example.cyberpay_android.models.Charge;
import com.example.cyberpay_android.models.ChargeBank;
import com.example.cyberpay_android.models.Transaction;
import com.example.cyberpay_android.network.ApiClient;
import com.example.cyberpay_android.network.ApiResponse;
import com.example.cyberpay_android.network.BankResponse;
import com.example.cyberpay_android.network.ChargeBankResponse;
import com.example.cyberpay_android.network.ChargeResponse;
import com.example.cyberpay_android.network.IApiService;
import com.example.cyberpay_android.network.MerchantTransactionResponse;
import com.example.cyberpay_android.network.OtpResponse;
import com.example.cyberpay_android.network.TransactionResponse;
import com.example.cyberpay_android.network.VerifyTransactionResponse;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CyberPaySDK {


    private static CyberPaySDK INSTANCE = null;

    private static final String TAG = CyberPaySDK.class.getSimpleName();

    private IApiService webService;


    private TransactionCallback transactionCallback;


    private Transaction transaction;
    private Card cardModel;

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
        jsonParams.put("currency", transaction.getCurrency());

        if (!TextUtils.isEmpty(transaction.getMerchantReference())) {
            jsonParams.put("merchantRef", transaction.getMerchantReference());

        }
        jsonParams.put("amount", transaction.getAmountInKobo());
        jsonParams.put("description", transaction.getDescription());
        jsonParams.put("integrationKey", API_KEY);
        jsonParams.put("returnUrl", transaction.getReturnUrl());
        jsonParams.put("customerEmail", transaction.getCustomerEmail());
        jsonParams.put("customerMobile", transaction.getCustomerMobile());

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


    public void getBank(final TransactionCallback transactionCallback) {
        final Call<ApiResponse<List<BankResponse>>> call = webService.getBank();
        call.enqueue(new Callback<ApiResponse<List<BankResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<BankResponse>>> call, Response<ApiResponse<List<BankResponse>>> response) {
                if (response.body() == null) {
                    return;
                }
                if (response.body().getData() != null && response.body().isSucceeded()) {
                    transactionCallback.onBank(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<BankResponse>>> call, Throwable t) {
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
        jsonParams.put("Name", "Sample Name");
        jsonParams.put("ExpiryMonth", charge.getExpiryMonth());
        jsonParams.put("ExpiryYear", charge.getExpiryYear());
        jsonParams.put("CardNumber", charge.getCardNumber());
        jsonParams.put("CVV", charge.getCvv());
        jsonParams.put("Reference", transaction.getTransactionReference());
        jsonParams.put("CardPin", charge.getCardPin());

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams))
                .toString());

        JSONObject jsonObject = new JSONObject(jsonParams);

        cardModel = new Card();
        cardModel.setCard(jsonObject);


        Call<ApiResponse<ChargeResponse>> call = webService.chargeCard(body);
        call.enqueue(new Callback<ApiResponse<ChargeResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<ChargeResponse>> call, @NonNull Response<ApiResponse<ChargeResponse>> response) {

                if (response.body() == null)
                    return;

                if (response.body().getData() != null && response.body().getData().getStatus().equals("Success")) {

                    transactionCallback.onSuccess(response.body().getData().getReference());

                }

//                if (response.body().getData() != null && response.body().getData().getStatus().equals("ProvidePin")) {
//
//                    transactionCallback.onProvidePin(cardModel);
//
//                }
                if (response.body().getData() != null && response.body().getData().getStatus().equals("Successful")) {

                    transactionCallback.onSuccess(response.body().getData().getReference());


                } else if (response.body().getData() != null && response.body().getData().getStatus().equals("Otp")) {
                    if(response.body().getData().getRedirectUrl() != null)
                        transaction.setReturnUrl(response.body().getData().getRedirectUrl());
                    transactionCallback.onOtpRequired(transaction);

                } else if (response.body().getData() != null && response.body().getData().getStatus().equals("EnrollOtp")) {
                    if(response.body().getData().getRedirectUrl() != null)
                        transaction.setReturnUrl(response.body().getData().getRedirectUrl());
                    transactionCallback.onEnrolOtp(transaction);

                } else if (response.body().getData() != null && response.body().getData().getStatus().equals("Secure3D")) {
                    if(response.body().getData().getRedirectUrl() != null)
                        transaction.setReturnUrl(response.body().getData().getRedirectUrl());
                    transactionCallback.onSecure3dRequired(transaction);

                } else if (response.body().getData() != null && response.body().getData().getStatus().equals("Secure3DMpgs")) {
                    transaction.setReturnUrl(response.body().getData().getRedirectUrl());
                    transactionCallback.onSecure3DMpgsRequired(transaction);

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


    public void enrolBankOtp(String yourOtp, final TransactionCallback transactionCallback) {
        Map<String, Object> jsonParams = new HashMap<>();
        jsonParams.put("reference", transaction.getTransactionReference());
        jsonParams.put("otp", yourOtp);

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());

        Call<ApiResponse<ChargeBankResponse>> call = webService.enrolBankOtp(body);
        call.enqueue(new Callback<ApiResponse<ChargeBankResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<ChargeBankResponse>> call, Response<ApiResponse<ChargeBankResponse>> response) {
                if (response.body().getData() != null && response.body().getData().getStatus().equals("EnrollOtp")) {
                    transactionCallback.onOtpRequired(transaction);

                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ChargeBankResponse>> call, Throwable t) {

            }
        });
    }

public void enrolCardOtp(final Transaction transaction, final TransactionCallback transactionCallback) {
        Map<String, Object> jsonParams = new HashMap<>();
        jsonParams.put("reference", transaction.getTransactionReference());
        jsonParams.put("registeredPhoneNumber", transaction.getRegisteredPhoneNumber());
        jsonParams.put("cardModel", cardModel.getCard());


    RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());

        Call<ApiResponse<ChargeBankResponse>> call = webService.enrolBankOtp(body);
        call.enqueue(new Callback<ApiResponse<ChargeBankResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<ChargeBankResponse>> call, Response<ApiResponse<ChargeBankResponse>> response) {
                if (response.body().getData() != null && response.body().getData().getStatus().equals("EnrollOtp")) {
                    transactionCallback.onOtpRequired(transaction);

                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ChargeBankResponse>> call, Throwable t) {

            }
        });
    }

    public void ChargeBank(final ChargeBank charge, final TransactionCallback transactionCallback) {


        Map<String, Object> jsonParams = new HashMap<>();
        jsonParams.put("BankCode", charge.getBankCode());
        jsonParams.put("AccountNumber", charge.getAccountNumber());
        jsonParams.put("Reference", transaction.getTransactionReference());
        jsonParams.put("AccountName", charge.getAccountName());

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams))
                .toString());

        Call<ApiResponse<ChargeBankResponse>> call = webService.chargeBank(body);
        call.enqueue(new Callback<ApiResponse<ChargeBankResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<ChargeBankResponse>> call, @NonNull Response<ApiResponse<ChargeBankResponse>> response) {

                if (response.body() == null)
                    return;

                if (response.body().getData() != null && response.body().getData().getStatus().equals("Otp")) {
                    transactionCallback.onOtpRequired(transaction);

                } else {

                    transactionCallback.onError(new Throwable(response.body().getData().getMessage()), transaction);

                }

            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<ChargeBankResponse>> call, @NonNull Throwable t) {

                transactionCallback.onError(t, transaction);

            }
        });

    }

    public void VerifyOtp(final Transaction transaction, final TransactionCallback transactionCallback) {

        Map<String, Object> jsonParams = new HashMap<>();
        jsonParams.put("otp", transaction.getOtp());
        jsonParams.put("reference", transaction.getTransactionReference());
        jsonParams.put("card", cardModel.getCard());

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams))
                .toString());

        Call<ApiResponse<OtpResponse>> call = webService.verifyOtp(body);

        call.enqueue(new Callback<ApiResponse<OtpResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<OtpResponse>> call, @NonNull Response<ApiResponse<OtpResponse>> response) {


                if(response.code() == 200){
                    assert response.body() != null;

                    try{
                         if (response.body().getData() != null && response.body().getData().getStatus().equals("Successful")) {

                            transactionCallback.onSuccess(response.body().getData().getReference());

                        } else if (response.body().getData() != null && response.body().getData().getStatus().equals("Failed")) {

                            transactionCallback.onError(new Throwable(response.body().getData().getMessage()), transaction);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                } else if(response.code() == 500){
                    ResponseBody responseBody = response.errorBody();

                    Gson gson = new Gson();
                    ApiResponse apiResponse = null;
                    try{
                        apiResponse = gson.fromJson(responseBody.string(),ApiResponse.class);
                        if(apiResponse.getMessage() != null){
                            transactionCallback.onError(new Throwable(apiResponse.getMessage()), transaction);

                        }
                    } catch (IOException e){
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<OtpResponse>> call, @NonNull Throwable t) {
                transactionCallback.onError(t, transaction);
            }
        });
    }


    public void VerifyBankOtp(final Transaction transaction, final TransactionCallback transactionCallback) {

        Map<String, Object> jsonParams = new HashMap<>();

        jsonParams.put("BankCode", transaction.getBankCode());
        jsonParams.put("AccountName", transaction.getAccountName());
        jsonParams.put("AccountNumber", transaction.getAccountNumber());
        jsonParams.put("Reference", transaction.getTransactionReference());

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams))
                .toString());

        Call<ApiResponse<OtpResponse>> call = webService.verifyBankOtp(body, transaction.getOtp());

        call.enqueue(new Callback<ApiResponse<OtpResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<OtpResponse>> call, @NonNull Response<ApiResponse<OtpResponse>> response) {


                if(response.code() == 200){
                    assert response.body() != null;

                    try{
                        if (response.body().getData() != null && response.body().getData().getStatus().equals("Successful")) {

                            transactionCallback.onSuccess(response.body().getData().getReference());

                        } else if (response.body().getData() != null && response.body().getData().getStatus().equals("Failed")) {

                            transactionCallback.onError(new Throwable(response.body().getData().getMessage()), transaction);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                } else if(response.code() == 500){
                    ResponseBody responseBody = response.errorBody();

                    Gson gson = new Gson();
                    ApiResponse apiResponse = null;
                    try{
                        apiResponse = gson.fromJson(responseBody.string(),ApiResponse.class);
                        if(apiResponse.getMessage() != null){
                            transactionCallback.onError(new Throwable(apiResponse.getMessage()), transaction);


                        }
                    } catch (IOException e){
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<OtpResponse>> call, @NonNull Throwable t) {
                transactionCallback.onError(t, transaction);
            }
        });
    }


    public interface TransactionCallback {

//        void onProvidePin(Charge charge);

        void onSuccess(String transactionReference);

        void onOtpRequired(Transaction transaction);

        void onSecure3dRequired(Transaction transaction);

        void onSecure3DMpgsRequired(Transaction transaction);

        void onEnrolOtp(Transaction transaction);

        void onError(Throwable error, Transaction transaction);

        void onBank(List<BankResponse> bankResponses);
    }
}
