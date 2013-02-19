package com.telecom.navigation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class UseGuideCategory extends Activity {

    public static final String EXTRA_KEY_TYPE = "extra_key_type";

    /* package */enum GuideType {
        PHONE, MAIL, INTERNET, CAMERA, MEDIA, ALBUM
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.use_guide_layout);

    }

    public void onClick(View view) {
        GuideType type;
        switch (view.getId()) {
        case R.id.img_type_phone:
            type = GuideType.PHONE;
            break;
        case R.id.img_type_mail:
            type = GuideType.MAIL;
            break;
        case R.id.img_type_internet:
            type = GuideType.INTERNET;
            break;
        case R.id.img_type_camera:
            type = GuideType.CAMERA;
            break;
        case R.id.img_type_media:
            type = GuideType.MEDIA;
            break;
        case R.id.img_type_album:
            type = GuideType.ALBUM;
            break;
        default:
            type = GuideType.PHONE;
            break;
        }
        Intent intent = new Intent(this, UseGuideDetail.class);
        intent.putExtra(EXTRA_KEY_TYPE, type.ordinal());
        startActivity(intent);
    }
}
