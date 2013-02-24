package com.telecom.navigation;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.telecom.util.NetworkUtil;

public class SplashScreen extends BaseActivity {

    public static final String EXTRA_KEY_START_TIME = "extra_key_start_time";

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

        Editor editor = settings.edit();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd%HH:mm:ss");
        String formatString = formatter.format(date);
        editor.putString(EXTRA_KEY_START_TIME, formatString);
        editor.commit();

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
