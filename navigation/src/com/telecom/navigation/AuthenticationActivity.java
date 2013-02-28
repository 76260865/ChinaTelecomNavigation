package com.telecom.navigation;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
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

    private IMSITask mImsiTask;

    private TelephonyManager mTelephonyMgr;

    private EditText mEditUserPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // The look of this sample is set via a style in the manifest
        setContentView(R.layout.authentication_activity);

        mPhoneNum = getPhoneNum();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        // Toast.makeText(
        // getApplicationContext(),
        // "屏幕分辨率为:" + dm.widthPixels + " * " + dm.heightPixels + "density:" +
        // dm.density
        // + " densityDpi:" + dm.densityDpi, 1).show();
        mLayoutInputNumber = findViewById(R.id.linear_input_number);
        mLayoutLinearAuth = findViewById(R.id.linear_auth);
        mTxtUserName = (TextView) findViewById(R.id.txt_user_name);
        mTxtMaster = (TextView) findViewById(R.id.txt_master);
        mEditUserPhone = (EditText) findViewById(R.id.edit_user_phone);

        mTelephonyMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mIMSI = mTelephonyMgr.getSubscriberId();

        mImsiTask = new IMSITask(mTxtUserName);
        mImsiTask.execute();
    }

    public void onBtnAuthClick(View v) {
        if (TextUtils.isEmpty(mEditUserPhone.getText())) {
            Toast.makeText(this, R.string.str_format_input_phone, Toast.LENGTH_LONG).show();
            return;
        }

        mLayoutInputNumber.setVisibility(View.GONE);
        mLayoutLinearAuth.setVisibility(View.VISIBLE);

        mMyAsyncTask = new MyAsyncTask(mTxtMaster);
        mMyAsyncTask.execute();

        mUserPhone = mEditUserPhone.getText().toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMyAsyncTask != null) {
            mMyAsyncTask.cancel(true);
        }
        mImsiTask.cancel(true);
    }

    public void onBtnNextClick(View v) {
        Intent intent = new Intent(this, AppliactionCategoryActivity.class);
        startActivity(intent);
    }

    private String getPhoneNum() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getLine1Number();
    }

    private class MyAsyncTask extends AsyncTask<Void, Void, Master> {

        private TextView mTxtMaster;

        public MyAsyncTask(TextView txtView) {
            mTxtMaster = txtView;
        }

        @Override
        protected Master doInBackground(Void... params) {
            Master master = JsonUtil.getMasterInfo(mPhoneNum);
            if (master != null) {
                mUserId = master.getUserId();
            }
            return master;
        }

        @Override
        protected void onPostExecute(Master result) {

            if (result == null || TextUtils.isEmpty(result.getUserId())
                    || TextUtils.isEmpty(result.getUserName())) {
                Toast.makeText(getApplicationContext(), R.string.txt_toast_get_message_failed,
                        Toast.LENGTH_LONG).show();
                return;
            }

            mTxtMaster.setText(getString((R.string.str_format_master_info), result.getUserName(),
                    result.getUserId()));
        }
    }

    private class IMSITask extends AsyncTask<Void, Void, String> {

        private TextView mTxtMaster;

        public IMSITask(TextView txtView) {
            mTxtMaster = txtView;
        }

        @Override
        protected String doInBackground(Void... params) {
            Customer customer = JsonUtil.getCustomerInfoByIMSI(mIMSI);
            if (customer != null) {
                mProId = customer.getProdId();
            }
            return customer == null ? null : customer.getCustomerName();
        }

        @Override
        protected void onPostExecute(String result) {
            if (TextUtils.isEmpty(result)) {
                Toast.makeText(getApplicationContext(), R.string.txt_toast_get_message_failed,
                        Toast.LENGTH_LONG).show();
            }
            mTxtMaster.setText(getString(R.string.str_format_master_name, result));
        }
    }
}
