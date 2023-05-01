package com.example.sigma;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class UserGuideActivity extends AppCompatActivity {
    private WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_guide);

        webView = findViewById(R.id.guideWebView);
        //webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("file:///android_asset/guide.html");

    }
}