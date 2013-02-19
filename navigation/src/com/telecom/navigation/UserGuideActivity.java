package com.telecom.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class UserGuideActivity extends BaseActivity {

    public static final String EXTRA_KEY_TYPE = "extra_key_type";
    public static final String EXTRA_KEY_TITLE_STRING_ID = "extra_key_title_id";
    public static final String EXTRA_KEY_DESC_STRING_ID = "extra_key_desc_id";

    /* package */enum GuideType {
        PHONE, SMS, INTERNET, CAMERA, MEDIA, ALBUM
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.use_guide_layout);
    }

    public void onClick(View view) {
        GuideType type;
        int titleId;
        int detailId;
        switch (view.getId()) {
        case R.id.img_type_phone:
            type = GuideType.PHONE;
            titleId = R.string.txt_dial_bord;
            detailId = R.string.txt_dial_bord_description;
            break;
        case R.id.img_type_sms:
            type = GuideType.SMS;
            titleId = R.string.txt_sms;
            detailId = R.string.txt_sms_description;
            break;
        case R.id.img_type_internet:
            type = GuideType.INTERNET;
            titleId = R.string.txt_internet;
            detailId = R.string.txt_internet_description;
            break;
        case R.id.img_type_camera:
            type = GuideType.CAMERA;
            titleId = R.string.txt_camera;
            detailId = R.string.txt_camera_description;
            break;
        case R.id.img_type_media:
            type = GuideType.MEDIA;
            titleId = R.string.txt_media;
            detailId = R.string.txt_media_description;
            break;
        case R.id.img_type_album:
            type = GuideType.ALBUM;
            titleId = R.string.txt_album;
            detailId = R.string.txt_album_description;
            break;
        default:
            type = GuideType.PHONE;
            titleId = R.string.txt_dial_bord;
            detailId = R.string.txt_dial_bord_description;
            break;
        }

        Intent intent = new Intent(this, UserGuideDetail.class);
        intent.putExtra(EXTRA_KEY_TYPE, type.ordinal());
        intent.putExtra(EXTRA_KEY_TITLE_STRING_ID, titleId);
        intent.putExtra(EXTRA_KEY_DESC_STRING_ID, detailId);
        startActivity(intent);
    }
}
