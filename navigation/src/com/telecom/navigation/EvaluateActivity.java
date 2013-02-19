package com.telecom.navigation;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RatingBar;
import android.widget.TextView;

public class EvaluateActivity extends BaseActivity {

    private View mLinearBottom;
    private RatingBar mRatingBar;
    private TextView mTxtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.evaluate_activity);

        mRatingBar = (RatingBar) findViewById(R.id.ratingbar);
        mRatingBar.setOnTouchListener(mOnTouchListener);
        mLinearBottom = findViewById(R.id.linear_bottom);
        mTxtTitle = (TextView) findViewById(R.id.txt_title);
    }

    private OnTouchListener mOnTouchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                mRatingBar.setVisibility(View.GONE);
                mLinearBottom.setVisibility(View.VISIBLE);
                mTxtTitle.setText(R.string.txt_thanks_evaluate);
                return true;
            }
            return false;
        }
    };
}
