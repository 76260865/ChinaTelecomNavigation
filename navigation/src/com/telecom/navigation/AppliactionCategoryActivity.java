package com.telecom.navigation;

import android.content.Intent;
import android.graphics.Color;
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

import com.telecom.view.CirclePageIndicator;

public class AppliactionCategoryActivity extends FragmentActivity {
    AdvertisementFragmentAdapter mAdapter;
    ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // The look of this sample is set via a style in the manifest
        setContentView(R.layout.application_category_layout);

        mAdapter = new AdvertisementFragmentAdapter(getSupportFragmentManager());

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        indicator.setBackgroundColor(Color.BLACK);
    }

    public void onImgUserGuideClick(View view) {
        Intent intent = new Intent(this, UseGuideCategory.class);
        startActivity(intent);
    }

    class AdvertisementFragmentAdapter extends FragmentPagerAdapter {
        protected final int[] CONTENT = new int[] { R.drawable.type1, R.drawable.type2,
                R.drawable.type3, R.drawable.type4, R.drawable.type5, R.drawable.type6,
                R.drawable.type7, R.drawable.type8 };

        private int mCount = CONTENT.length;

        public AdvertisementFragmentAdapter(FragmentManager fm) {
            super(fm);
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
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
                mContent = savedInstanceState.getInt(KEY_CONTENT);
            }

            ImageView view = (ImageView) inflater.inflate(R.layout.advertisement_item_layout, null);
            view.setImageResource(mContent);

            return view;
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putInt(KEY_CONTENT, mContent);
        }
    }
}