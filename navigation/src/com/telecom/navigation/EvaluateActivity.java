package com.telecom.navigation;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.RatingBar;
import android.widget.TextView;

public class EvaluateActivity extends Activity {

    private View mLinearBottom;
    private RatingBar mRatingBar;
    private TextView mTxtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
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
