package com.telecom.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

public class AuthenticationActivity extends BaseActivity {
    private View linear_input_number;
    private View linear_auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // The look of this sample is set via a style in the manifest
        setContentView(R.layout.authentication_activity);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Toast.makeText(
                getApplicationContext(),
                "屏幕分辨率为:" + dm.widthPixels + " * " + dm.heightPixels + "density:" + dm.density
                        + " densityDpi:" + dm.densityDpi, 1).show();
        linear_input_number = findViewById(R.id.linear_input_number);
        linear_auth = findViewById(R.id.linear_auth);
    }

    public void onBtnAuthClick(View v) {
        linear_input_number.setVisibility(View.GONE);
        linear_auth.setVisibility(View.VISIBLE);
    }

    public void onBtnNextClick(View v) {
        Intent intent = new Intent(this, AppliactionCategoryActivity.class);
        startActivity(intent);
    }
}
