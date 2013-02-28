package com.telecom.navigation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.telecom.model.Customer;
import com.telecom.util.JsonUtil;
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
        if (isFirstUse) {
            int stringId = NetworkUtil.isNetworkConnected(this) ? R.string.msg_toast_contact_server
                    : R.string.txt_connect_network;
            Toast.makeText(getApplicationContext(), stringId, Toast.LENGTH_LONG).show();
        }

        Editor editor = settings.edit();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd%HH:mm:ss");
        String formatString = formatter.format(date);
        editor.putString(EXTRA_KEY_START_TIME, formatString);
        editor.commit();

        new IMSITask().execute();
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Intent intent = new Intent(SplashScreen.this, AdvertisementActivity.class);
            startActivity(intent);
            SplashScreen.this.finish();
        }
    };

    private class IMSITask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Customer customer = JsonUtil.getCustomerInfoByIMSI(mIMSI);
            if (customer != null) {
                mProId = customer.getProdId();
                mDownloadUrl = customer.getmApkUri();

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(Calendar.MINUTE, -customer.getInvalidTime());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date date = sdf.parse(customer.getmAccountTime());
                    // TODO: change to after
                    if (calendar.getTime().before(date)) {
                        SharedPreferences settings = getSharedPreferences(
                                AdvertisementActivity.EXTRA_KEY_SHARE_PREF, Activity.MODE_PRIVATE);
                        Editor editor = settings.edit();
                        editor.putBoolean(AdvertisementActivity.EXTRA_KEY_SHARE_FIRST, false);
                        editor.commit();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mHandler.sendEmptyMessage(0);
        }
    }

}
