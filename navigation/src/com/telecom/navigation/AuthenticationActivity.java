package com.telecom.navigation;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AuthenticationActivity extends BaseActivity {
    private View mLayoutInputNumber;

    private View mLayoutLinearAuth;

    private TextView mTxtUserName;

    private TextView mTxtMaster;

    private String mPhoneNum;

    private MyAsyncTask mMyAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // The look of this sample is set via a style in the manifest
        setContentView(R.layout.authentication_activity);

        mPhoneNum = getPhoneNum();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Toast.makeText(
                getApplicationContext(),
                "屏幕分辨率为:" + dm.widthPixels + " * " + dm.heightPixels + "density:" + dm.density
                        + " densityDpi:" + dm.densityDpi, 1).show();
        mLayoutInputNumber = findViewById(R.id.linear_input_number);
        mLayoutLinearAuth = findViewById(R.id.linear_auth);
        mTxtUserName = (TextView) findViewById(R.id.txt_user_name);
        mTxtMaster = (TextView) findViewById(R.id.txt_master);

        mMyAsyncTask = new MyAsyncTask(mTxtUserName);
        mMyAsyncTask.execute();
    }

    public void onBtnAuthClick(View v) {
        mMyAsyncTask = new MyAsyncTask(mTxtMaster);
        mMyAsyncTask.execute();

        mLayoutInputNumber.setVisibility(View.GONE);
        mLayoutLinearAuth.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMyAsyncTask != null) {
            mMyAsyncTask.cancel(true);
        }
    }

    public void onBtnNextClick(View v) {
        Intent intent = new Intent(this, AppliactionCategoryActivity.class);
        startActivity(intent);
    }

    private String getPhoneNum() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getLine1Number();
    }

    private class MyAsyncTask extends AsyncTask<Void, Void, String> {

        private TextView mTxtMaster;

        public MyAsyncTask(TextView txtView) {
            mTxtMaster = txtView;
        }

        @Override
        protected String doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // mTxtMaster.setText(result);
        }

    }
}
