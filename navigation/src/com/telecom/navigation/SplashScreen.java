package com.telecom.navigation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreen extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);
        mHandler.sendEmptyMessageDelayed(0, 500);
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Intent intent = new Intent(SplashScreen.this, AdvertisementActivity.class);
            startActivity(intent);
            SplashScreen.this.finish();
        }
    };
}
