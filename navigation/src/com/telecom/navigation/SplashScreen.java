package com.telecom.navigation;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.telecom.model.Customer;
import com.telecom.util.JsonUtil;
import com.telecom.util.NetworkUtil;

public class SplashScreen extends BaseActivity {

    public static final String EXTRA_KEY_START_TIME = "extra_key_start_time";

    private TelephonyManager mTelephonyMgr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        // TODO: comment
//        mIMSI = mTelephonyMgr.getSubscriberId();
         mIMSI = "460030497828541";

        SharedPreferences settings = getSharedPreferences(
                AdvertisementActivity.EXTRA_KEY_SHARE_PREF, Activity.MODE_PRIVATE);
        boolean isFirstUse = settings.getBoolean(AdvertisementActivity.EXTRA_KEY_SHARE_FIRST, true);
        if (isFirstUse) {
            int stringId = NetworkUtil.isNetworkConnected(this) ? R.string.msg_toast_contact_server
                    : R.string.txt_connect_network;
            Toast.makeText(getApplicationContext(), stringId, Toast.LENGTH_SHORT).show();
        }

        Editor editor = settings.edit();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd%HH:mm:ss");
        String formatString = formatter.format(date);
        editor.putString(EXTRA_KEY_START_TIME, formatString);
        editor.commit();

        new IMSITask().execute();
        mDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);

        new DownLoadAvertisementTask().execute();
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Intent intent = new Intent(SplashScreen.this, AdvertisementActivity.class);
            startActivity(intent);
            AdvertisementActivity.bmpAdvertisements = bmpAdvertisements;
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
                    // TODO: after for test
                    // if (calendar.getTime().before(date)) {
                    // SharedPreferences settings = getSharedPreferences(
                    // AdvertisementActivity.EXTRA_KEY_SHARE_PREF,
                    // Activity.MODE_PRIVATE);
                    // Editor editor = settings.edit();
                    // editor.putBoolean(AdvertisementActivity.EXTRA_KEY_SHARE_FIRST,
                    // false);
                    // editor.commit();
                    // }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // mHandler.sendEmptyMessage(0);
        }
    }

    private Bitmap[] bmpAdvertisements = new Bitmap[3];
    private static final String BASE_URI = "http://118.121.17.250";
    private DisplayMetrics mDisplayMetrics;

    private class DownLoadAvertisementTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            String[] bmpUris = JsonUtil.getAdvertisements(mDisplayMetrics.heightPixels);
            if (bmpUris == null)
                return false;

            for (int i = 0; i < bmpUris.length; i++) {
                String uri = bmpUris[i];
                if (!TextUtils.isEmpty(uri)) {
                    bmpAdvertisements[i] = getBitmapFromUrl(BASE_URI + uri);
                }
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                mHandler.sendEmptyMessage(0);
            }
        }

    }

    private Bitmap getBitmapFromUrl(String imgUrl) {
        URL url;
        Bitmap bitmap = null;
        try {
            url = new URL(imgUrl);
            InputStream is = url.openConnection().getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bitmap = BitmapFactory.decodeStream(bis);
            bis.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
