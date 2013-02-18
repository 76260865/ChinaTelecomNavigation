package com.telecom.navigation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class UseGuideCategory extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.use_guide_layout);

    }

    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.img_type_phone:
            break;
        case R.id.img_type_mail:
            break;
        case R.id.img_type_internet:
            break;
        case R.id.img_type_camera:
            break;
        case R.id.img_type_media:
            break;
        case R.id.img_type_album:
            break;
        }
        Intent intent = new Intent(this, UseGuideDetail.class);
        startActivity(intent);
    }
}
