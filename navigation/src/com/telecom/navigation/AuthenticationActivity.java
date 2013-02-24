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

import com.telecom.model.Customer;
import com.telecom.model.Master;
import com.telecom.util.JsonUtil;

public class AuthenticationActivity extends BaseActivity {
    private View mLayoutInputNumber;

    private View mLayoutLinearAuth;

    private TextView mTxtUserName;

    private TextView mTxtMaster;

    private String mPhoneNum;

    private MyAsyncTask mMyAsyncTask;

    private TelephonyManager mTelephonyMgr;

    private String mImsi;

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

        mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mImsi = mTelephonyMgr.getSubscriberId();

        IMSITask imsiTask = new IMSITask(mTxtUserName);
        imsiTask.execute();
    }

    public void onBtnAuthClick(View v) {
        mLayoutInputNumber.setVisibility(View.GONE);
        mLayoutLinearAuth.setVisibility(View.VISIBLE);

        mMyAsyncTask = new MyAsyncTask(mTxtUserName);
        mMyAsyncTask.execute();
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
            Master master = JsonUtil.getMasterInfo();
            mTxtMaster
                    .setText("天翼辅导员：" + master.getUserName() + "工号" + master.getUserId() + "为您服务");
        }
    }

    private class IMSITask extends AsyncTask<Void, Void, String> {

        private TextView mTxtMaster;

        public IMSITask(TextView txtView) {
            mTxtMaster = txtView;
        }

        @Override
        protected String doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Customer customer = JsonUtil.getCustomerInfoByIMSI();
            mTxtMaster.setText("您好:" + customer.getCustomerName());
        }
    }
}
