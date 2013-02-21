package com.telecom.navigation;

import com.telecom.util.NetworkUtil;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class SplashScreen extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        SharedPreferences settings = getSharedPreferences(
                AdvertisementActivity.EXTRA_KEY_SHARE_PREF, Activity.MODE_PRIVATE);
        boolean isFirstUse = settings.getBoolean(AdvertisementActivity.EXTRA_KEY_SHARE_FIRST, true);
        if (isFirstUse && !NetworkUtil.isNetworkConnected(this)) {
            Toast.makeText(getApplicationContext(), R.string.txt_connect_network, Toast.LENGTH_LONG)
                    .show();
        }
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
