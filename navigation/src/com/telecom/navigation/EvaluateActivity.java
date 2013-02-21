package com.telecom.navigation;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

public class EvaluateActivity extends BaseActivity {

    private View mLinearBottom;
    private RatingBar mRatingBar;
    private TextView mTxtTitle;
    private Button btn_back_to_main;

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
    }

    private OnTouchListener mOnTouchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                mRatingBar.setVisibility(View.GONE);
                mLinearBottom.setVisibility(View.VISIBLE);
                mTxtTitle.setText(R.string.txt_thanks_evaluate);

                SharedPreferences settings = getSharedPreferences(
                        AdvertisementActivity.EXTRA_KEY_SHARE_PREF, Activity.MODE_PRIVATE);
                Editor editor = settings.edit();
                editor.putBoolean(AdvertisementActivity.EXTRA_KEY_SHARE_FIRST, false);
                editor.commit();
                return true;
            }
            return false;
        }
    };
}
