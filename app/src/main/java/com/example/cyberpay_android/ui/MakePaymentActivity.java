package com.example.cyberpay_android.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.cyberpay_android.R;

public class MakePaymentActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    public static String PARAM_TRANSACTION = "PARAM_TRANSACTION";

    WebView webView;
    SwipeRefreshLayout swipeRefreshLayout;
    String paymentData, reference = " ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment);

        Bundle bundle = getIntent().getExtras();

        if(bundle.getString(PARAM_TRANSACTION)!= null)
        {
            PARAM_TRANSACTION =  bundle.getString(PARAM_TRANSACTION);
        }
        webView = findViewById(R.id.web_view);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);

        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);
        swipeRefreshLayout.setOnRefreshListener(this);

        loadData();

    }

    private void loadData(){
        if (PARAM_TRANSACTION != null){
            if (TextUtils.isEmpty(PARAM_TRANSACTION)){
                Toast.makeText(getApplicationContext(),  "CyberPay is currently unavailable, try again", Toast.LENGTH_SHORT).show();
            } else {
                initWebView(PARAM_TRANSACTION);
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView(final String data){
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.clearCache(true);
        webView.clearHistory();
//        webView.setPadding(0, 0, 0, 0);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setJavaScriptEnabled(true); // enable
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setScrollbarFadingEnabled(false);
        webView.setWebChromeClient(new CyberPayChromeClient(getApplicationContext()));
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                swipeRefreshLayout.setRefreshing(true);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                swipeRefreshLayout.setRefreshing(false);


                if(url.startsWith("https://payment.staging.cyberpay.ng/notify?ref=")){
                    webView.destroy();
                    reference = url.substring(url.lastIndexOf("=")+ 1);


                    startActivity(new Intent(MakePaymentActivity.this, CyberPayActivty.class));
                    finish();
                }

                if(url.startsWith("https://payment.staging.cyberpay.ng/pay?reference")){
                    webView.destroy();
                    reference = url.substring(url.lastIndexOf("=")+ 1);

                    startActivity(new Intent(MakePaymentActivity.this, CyberPayActivty.class));
                    finish();
                }


            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                swipeRefreshLayout.setRefreshing(false);
            }
        });



        webView.loadUrl(data);
    }


    @Override
    public void onRefresh() {
        loadData();
    }


    private class CyberPayChromeClient extends WebChromeClient {
        Context context;

        CyberPayChromeClient(Context context) {
            super();
            this.context = context;
        }
    }
}
