package com.example.ebihartourism;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class safetyMeasures extends AppCompatActivity {
    private Button knowMoreBtn;
    WebView webView;
    private WebSettings webSettings;
    private WebViewClient trainsWebViewClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety_measures);

        knowMoreBtn = findViewById(R.id.know_more_safety_btn);
        webView = findViewById(R.id.safety_wv);

        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(trainsWebViewClient);



        knowMoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl("http://bstdc.bih.nic.in/information_centre.htm");
                Toast.makeText(safetyMeasures.this, "Scroll down to see more!", Toast.LENGTH_LONG).show();
            }
        });

    }
}
