package com.example.febin.group21_hw05;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class Web_View extends AppCompatActivity {

    TextView textViewHeading;
    final String VIDEO_LINK="VIDEO";
    final String VIDEO_TITLE="VIDEO_TITLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web__view);
        setTitle("WebView");
        textViewHeading=(TextView)findViewById(R.id.textViewHeading);

        String title=getIntent().getStringExtra(VIDEO_TITLE);
        textViewHeading.setText(""+title+ " Trailer");

        String url=getIntent().getStringExtra(VIDEO_LINK);
        url=url.substring(url.indexOf("v=")+2);
        Log.d("URL:", url);

        String frameVideo = "<html><body> <br> <iframe width=\"320\" height=\"315\" src=\"https://www.youtube.com/embed/"+url+"\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
        Log.d("frame:", frameVideo);
        WebView webview = (WebView) findViewById(R.id.webView);
        webview.setWebChromeClient(new WebChromeClient());
        //String frameVideo = "<html><body>Youtube video .. <br> <iframe width=\"320\" height=\"315\" src=\"https://www.youtube.com/embed/eTPtu-ExAK4\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webview.loadData(frameVideo, "text/html", "utf-8");

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
