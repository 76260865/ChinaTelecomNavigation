package com.telecom.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.telecom.navigation.UseGuideCategory.GuideType;
import com.telecom.view.CirclePageIndicator;

public class UseGuideDetail extends FragmentActivity {
    private AdvertisementFragmentAdapter mAdapter;
    private ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // The look of this sample is set via a style in the manifest
        setContentView(R.layout.use_guide_detail);

        Intent intent = getIntent();
        int ordinal = intent.getExtras().getInt(UseGuideCategory.EXTRA_KEY_TYPE,
                GuideType.PHONE.ordinal());
        GuideType type = GuideType.values()[ordinal];
        int[] drawables;
        switch (type) {
        case PHONE:
            drawables = new int[] { R.drawable.teach_tele_01, R.drawable.teach_tele_02 };
            break;
        case SMS:
            drawables = new int[] { R.drawable.teach_sms_01, R.drawable.teach_sms_02 };
            break;
        case INTERNET:
            drawables = new int[] { R.drawable.teach_internet_01, R.drawable.teach_internet_02 };
            break;
        case CAMERA:
            drawables = new int[] { R.drawable.teach_camera_01, R.drawable.teach_camera_02 };
            break;
        case MEDIA:
            drawables = new int[] { R.drawable.teach_media_01, R.drawable.teach_media_02 };
            break;
        case ALBUM:
            drawables = new int[] { R.drawable.teach_camera_01, R.drawable.teach_camera_01 };
            break;
        default:
            drawables = new int[] { R.drawable.teach_tele_01, R.drawable.teach_tele_02 };
            break;
        }

        mAdapter = new AdvertisementFragmentAdapter(getSupportFragmentManager(), drawables);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        indicator.getBackground().setAlpha(0);
    }

    class AdvertisementFragmentAdapter extends FragmentPagerAdapter {
        protected int[] CONTENT;

        private int mCount;

        public AdvertisementFragmentAdapter(FragmentManager fm, int[] content) {
            super(fm);
            CONTENT = content;
            mCount = CONTENT.length;
        }

        @Override
        public Fragment getItem(int position) {
            return AdvertisementFragment.newInstance(CONTENT[position % CONTENT.length]);
        }

        @Override
        public int getCount() {
            return mCount;
        }

        public void setCount(int count) {
            if (count > 0 && count <= 10) {
                mCount = count;
                notifyDataSetChanged();
            }
        }
    }

    public final static class AdvertisementFragment extends Fragment {
        private static final String KEY_CONTENT = "AdvertisementFragment:Content";

        private int mContent;

        public static AdvertisementFragment newInstance(int content) {
            AdvertisementFragment fragment = new AdvertisementFragment();

            fragment.mContent = content;

            return fragment;
        }

        @Override
        public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
                mContent = savedInstanceState.getInt(KEY_CONTENT);
            }

            View view = inflater.inflate(R.layout.use_guide_detail_item, null);
            ImageView imgItem = (ImageView) view.findViewById(R.id.img_guide_detail_item);
            imgItem.setImageResource(mContent);

            return view;
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putInt(KEY_CONTENT, mContent);
        }
    }
}