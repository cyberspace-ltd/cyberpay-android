package com.example.cyberpay_android.network;

import android.util.Log;

import com.example.cyberpay_android.repository.CyberPaySDK;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {


    private static String API_URL = "https://payment-api.staging.cyberpay.ng/api/v1/";

    private static IApiService iApiService;
    private static Retrofit retrofit = null;
    private static final String TAG = ApiClient.class.getSimpleName();


    private static Retrofit getClient() {

        if (!CyberPaySDK.getIsStagingMode()) {
            API_URL = "https://payment-api.cyberpay.ng/api/v1/";
        }

        String baseUri = API_URL;

        if (retrofit == null) {

            try {

//                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//                interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
//                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


                OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
//                        .addInterceptor(interceptor)
//                        .cache(provideCache())
                        .connectTimeout(100, TimeUnit.SECONDS)
                        .readTimeout(100, TimeUnit.SECONDS)
                        .retryOnConnectionFailure(true)
                        .build();

                retrofit = new Retrofit.Builder()
                        .client(okHttpClient)
                        .baseUrl(baseUri)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

            } catch (Exception ex) {
                Log.e(TAG, "getClient: ", ex);
            }

        }
        return retrofit;
    }

//    private static HttpLoggingInterceptor provideHttpLoggingInterceptor() {
//        HttpLoggingInterceptor httpLoggingInterceptor =
//                new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
//                    @Override
//                    public void log(String message) {Log.d(TAG, message);}
//                });
//        httpLoggingInterceptor.setLevel(BuildConfig.DEBUG ? BODY : NONE);
//        return httpLoggingInterceptor;
//    }


    public synchronized static IApiService getIApiService() {
        if (iApiService == null)
            iApiService = getClient().create(IApiService.class);

        return iApiService;
    }

}
