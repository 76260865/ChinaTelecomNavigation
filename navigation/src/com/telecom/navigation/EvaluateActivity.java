package com.telecom.navigation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.telecom.util.HttpUtil;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class EvaluateActivity extends BaseActivity {

    private View mLinearBottom;
    private RatingBar mRatingBar;
    private TextView mTxtTitle;
    private Button btn_back_to_main;

    private SimpleDateFormat mDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.evaluate_activity);

        mRatingBar = (RatingBar) findViewById(R.id.ratingbar);
        mRatingBar.setOnTouchListener(mOnTouchListener);
        mLinearBottom = findViewById(R.id.linear_bottom);
        mTxtTitle = (TextView) findViewById(R.id.txt_title);
        btn_back_to_main = (Button) findViewById(R.id.btn_back_to_main);

        SharedPreferences settings = getSharedPreferences(
                AdvertisementActivity.EXTRA_KEY_SHARE_PREF, Activity.MODE_PRIVATE);
        boolean isFirstUse = settings.getBoolean(AdvertisementActivity.EXTRA_KEY_SHARE_FIRST, true);

        if (!isFirstUse) {
            mTxtTitle.setVisibility(View.GONE);
            mRatingBar.setVisibility(View.GONE);
            btn_back_to_main.setVisibility(View.GONE);
            mLinearBottom.setVisibility(View.VISIBLE);
        }

        mDateFormat = new SimpleDateFormat("yyyy-MM-dd%HH:mm:ss");
    }

    private OnTouchListener mOnTouchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                mEndTime = new Date();

                List<BasicNameValuePair> paramsReport = new ArrayList<BasicNameValuePair>();
                paramsReport.add(new BasicNameValuePair("opt", "report"));
                paramsReport.add(new BasicNameValuePair("prod_id", "223344"));
                paramsReport.add(new BasicNameValuePair("imsi", "4600300000001"));
                paramsReport.add(new BasicNameValuePair("train_start_time", mDateFormat
                        .format(mStartTime)));
                paramsReport.add(new BasicNameValuePair("train_end_time", mDateFormat
                        .format(mEndTime)));
                paramsReport.add(new BasicNameValuePair("user_id", "123"));
                paramsReport.add(new BasicNameValuePair("user_phone", "189189189"));
                paramsReport.add(new BasicNameValuePair("app_list", mAppList == null ? "0"
                        : mAppList.substring(0, mAppList.length() - 1)));
                paramsReport.add(new BasicNameValuePair("service_level", ""
                        + (int) mRatingBar.getRating()));

                List<BasicNameValuePair> paramsStudy = new ArrayList<BasicNameValuePair>();
                paramsStudy.add(new BasicNameValuePair("opt", "study"));
                paramsStudy.add(new BasicNameValuePair("prod_id", mProId));
                paramsStudy.add(new BasicNameValuePair("imsi", mIMSI));
                paramsStudy.add(new BasicNameValuePair("train_start_time", mDateFormat
                        .format(mStartTime)));
                paramsStudy.add(new BasicNameValuePair("train_end_time", mDateFormat
                        .format(mEndTime)));

                new EvaluateTask(paramsReport, paramsStudy).execute();
                return true;
            }
            return false;
        }
    };

    private class EvaluateTask extends AsyncTask<Void, Void, Boolean> {

        private List<BasicNameValuePair> mParamsReport;
        private List<BasicNameValuePair> mParamsStudy;

        public EvaluateTask(List<BasicNameValuePair> paramsReport,
                List<BasicNameValuePair> paramsStudy) {
            mParamsReport = paramsReport;
            mParamsStudy = paramsStudy;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String resultReport = HttpUtil.doGet("http://118.121.17.250/bass/AppRequest",
                    mParamsReport);
            String resultStudy = HttpUtil.doGet("http://118.121.17.250/bass/AppRequest",
                    mParamsStudy);

            return TextUtils.isEmpty(resultReport) && TextUtils.isEmpty(resultStudy);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (!result) {
                mRatingBar.setVisibility(View.GONE);
                mLinearBottom.setVisibility(View.VISIBLE);
                mTxtTitle.setText(R.string.txt_thanks_evaluate);

                SharedPreferences settings = getSharedPreferences(
                        AdvertisementActivity.EXTRA_KEY_SHARE_PREF, Activity.MODE_PRIVATE);
                Editor editor = settings.edit();
                editor.putBoolean(AdvertisementActivity.EXTRA_KEY_SHARE_FIRST, false);
                editor.commit();
            } else {
                Toast.makeText(getApplicationContext(), R.string.msg_submit_error,
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        return true;
    }
}
