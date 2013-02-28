package com.telecom.navigation;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class BaseActivity extends FragmentActivity {

    private boolean isFirstUse;

    protected static String mProId;

    protected static String mIMSI;

    protected static Date mStartTime;

    protected static Date mEndTime;

    protected static String mUserId;

    protected static String mUserPhone;

    protected static String mAppList;

    protected static String mDownloadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        SharedPreferences settings = getSharedPreferences(
                AdvertisementActivity.EXTRA_KEY_SHARE_PREF, Activity.MODE_PRIVATE);
        isFirstUse = settings.getBoolean(AdvertisementActivity.EXTRA_KEY_SHARE_FIRST, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.getItem(0).setTitle(isFirstUse ? R.string.txt_menu_close : R.string.txt_menu_exit);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

        switch (item.getItemId()) {
        case R.id.menu_item_close:
            if (isFirstUse) {
                Intent intent = new Intent(this, EvaluateActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
            break;
        case R.id.menu_item_about:
            break;
        }
        return super.onMenuItemSelected(featureId, item);
    }
}
