package com.telecom.navigation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.telecom.util.HttpUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

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
        mActivitis.add(this);
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
                if (TextUtils.isEmpty(mProId) || TextUtils.isEmpty(mIMSI)
                        || TextUtils.isEmpty(mUserId) || TextUtils.isEmpty(mUserPhone)) {
                    Toast.makeText(getApplicationContext(), R.string.msg_toast_not_enough_info,
                            Toast.LENGTH_LONG).show();
                    break;
                }
                Intent intent = new Intent(this, EvaluateActivity.class);
                startActivity(intent);
            } else {
                showDialog(0);
            }
            break;
        case R.id.menu_item_about:
            break;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = new AlertDialog.Builder(this).setIcon(android.R.drawable.btn_star)
                .setTitle("").setMessage("确定退出程序吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences settings = getSharedPreferences(
                                AdvertisementActivity.EXTRA_KEY_SHARE_PREF, Activity.MODE_PRIVATE);
                        boolean isStudy = settings.getBoolean("is_study", false);
                        if (isStudy) {
                            postStudy();
                        } else {
                            exit();
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();

        return dialog;
    }

    private void postStudy() {
        mEndTime = new Date();
        SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd%HH:mm:ss");
        List<BasicNameValuePair> paramsStudy = new ArrayList<BasicNameValuePair>();
        paramsStudy.add(new BasicNameValuePair("opt", "study"));
        paramsStudy.add(new BasicNameValuePair("prod_id", mProId));
        paramsStudy.add(new BasicNameValuePair("imsi", mIMSI));
        paramsStudy.add(new BasicNameValuePair("train_start_time", mDateFormat.format(mStartTime)));
        paramsStudy.add(new BasicNameValuePair("train_end_time", mDateFormat.format(mEndTime)));

        new EvaluateTask(paramsStudy).execute();
    }

    private class EvaluateTask extends AsyncTask<Void, Void, Boolean> {

        private List<BasicNameValuePair> mParamsStudy;

        public EvaluateTask(List<BasicNameValuePair> paramsStudy) {
            mParamsStudy = paramsStudy;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String resultStudy = HttpUtil.doGet("http://118.121.17.250/bass/AppRequest",
                    mParamsStudy);

            return TextUtils.isEmpty(resultStudy);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            exit();
        }
    }

    protected void exit() {
        SharedPreferences settings = getSharedPreferences(
                AdvertisementActivity.EXTRA_KEY_SHARE_PREF, Activity.MODE_PRIVATE);
        Editor edit = settings.edit();
        edit.putBoolean("is_study", true);
        edit.commit();

        for (Activity activity : mActivitis) {
            activity.finish();
        }

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public static ArrayList<Activity> mActivitis = new ArrayList<Activity>();
}
