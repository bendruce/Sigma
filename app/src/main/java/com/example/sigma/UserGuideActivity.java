// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
// CODE FOR THE USER GUIDE
// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
package com.example.sigma;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class UserGuideActivity extends AppCompatActivity {
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //start the guide.html file in assets in the activity_user_guide layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_guide);
        webView = findViewById(R.id.guideWebView);
        webView.loadUrl("file:///android_asset/guide.html");
    }
}
