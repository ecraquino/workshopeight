package com.travelexperts.temobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;

public class StartupActivity extends Activity {
    WebView webView;
    ProgressBar progressBar;
    TextView txtProgress;
    int progressStatus = 0;
    Handler handler = new Handler();
    int showLength=8; // Length of startup splash screen to appear (in seconds)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        webView = (WebView) findViewById(R.id.web_view);
        webView.loadUrl("file:///android_asset/paper_plane.gif");
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txtProgress = (TextView) findViewById(R.id.txtProgress);
        // Start long running operation in a background thread

        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 100) {
                    progressStatus += 1;
                    // Update the progress bar and display the current value in the text view
                    handler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressStatus);
                            txtProgress.setText(progressStatus+"/"+progressBar.getMax());
                        }
                    });
                    try {
                        // Sleep for the show
                        Thread.sleep(showLength*10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        checkLoggedIn();
    }

    private void checkLoggedIn() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sp = getSharedPreferences("user", Context.MODE_PRIVATE);
                String svalue = sp.getString("login", "");

                if (svalue==""){
                    //Toast t=Toast.makeText(com.travelexperts.temobile.StartupActivity.this,svalue,Toast.LENGTH_LONG);
                    //t.show();
                    Intent intent=new Intent(getApplicationContext(),SignInActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    //Toast t=Toast.makeText(com.travelexperts.temobile.StartupActivity.this,svalue,Toast.LENGTH_LONG);
                    //t.show();
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, showLength*1000);
    }
}

