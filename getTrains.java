package com.example.ebihartourism;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class getTrains extends AppCompatActivity {

    private WebView webViewTrainsBwStations;
    private WebSettings webSettings;
    private WebViewClient trainsWebViewClient;
    private Button button_findTrain,button_liveStatus;
    private String url_liveStatus = "https://www.railyatri.in/live-train-status";
    private String url_findTrain = "https://www.railyatri.in/trains-between-stations";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_trains);

        button_findTrain = findViewById(R.id.buttonWebView_findTrains);
        button_liveStatus = findViewById(R.id.buttonWebView_Livestatus);

        webViewTrainsBwStations = findViewById(R.id.getTrains_bw_stn_webView);
        webSettings = webViewTrainsBwStations.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webViewTrainsBwStations.setWebViewClient(trainsWebViewClient);

        button_findTrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webViewTrainsBwStations.loadUrl(url_findTrain);
            }
        });

        button_liveStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webViewTrainsBwStations.loadUrl(url_liveStatus);
            }
        });
    }
}
