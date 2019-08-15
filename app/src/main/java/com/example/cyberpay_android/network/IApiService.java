package com.example.cyberpay_android.network;

import android.view.View;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IApiService {

    @POST("payments")
    Call<ApiResponse<TransactionResponse>> beginTransaction(@Body RequestBody params);

    @POST("payments/card")
    Call<ApiResponse<ChargeResponse>> chargeCard(@Body RequestBody params);


    @GET("payments/{transactionReference}")
    Call<ApiResponse<VerifyTransactionResponse>> verifyTransaction(@Path("transactionReference") String transactionReference);


    @GET("transactions/transactionsBymerchantRef")
    Call<ApiResponse<MerchantTransactionResponse>> verifyMerchantTransaction(@Query("merchantRef") String merchantRef);


    @POST("payments/otp")
    Call<ApiResponse<OtpResponse>> verifyOtp(@Body RequestBody params);

    @POST("payments/bank/otp/{value}")
    Call<ApiResponse<OtpResponse>> verifyBankOtp(@Body RequestBody params, @Path("value") String value);

    @POST("payments/bank")
    Call<ApiResponse<ChargeBankResponse>> chargeBank(@Body RequestBody params);

   @POST("payments/bank/enrol/otp")
    Call<ApiResponse<ChargeBankResponse>> enrolOtp(@Body RequestBody params);

    @GET("banks")
    Call<ApiResponse<List<BankResponse>>> getBank();

}
