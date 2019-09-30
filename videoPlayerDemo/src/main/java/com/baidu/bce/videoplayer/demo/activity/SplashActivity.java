package com.baidu.bce.videoplayer.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;


import com.baidu.bce.videoplayer.demo.R;
import com.baidu.bce.videoplayer.demo.util.NetworkUtils;

import java.util.TimerTask;

public class SplashActivity extends Activity {
    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pushactivity_splash);
        if (!checkNetwork()) {
            return;
        }
        new Handler().postDelayed(new TimerTask() {
            @Override
            public void run() {
                SplashActivity.this.startActivity(new Intent(SplashActivity.this, PushActivity.class));
                finish();
            }
        }, 700);
        // checkUpdate();
    }

    private boolean checkNetwork() {
        if (!NetworkUtils.isConnected(this)) {
            Toast.makeText(this, "请检查网络状态！", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void checkUpdate() {
        Toast.makeText(this, "检查新版本...", Toast.LENGTH_SHORT).show();
    }
}
