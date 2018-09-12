package com.example.vasha.vasharadio.youtube;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.example.vasha.vasharadio.R;

public class YoutubeActivity extends AppCompatActivity {

    WebView YTWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);

        YTWebView = (WebView)findViewById(R.id.webView);

        YTWebView.loadUrl("https://www.youtube.com");
    }
}
