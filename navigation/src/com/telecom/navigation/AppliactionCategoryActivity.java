package com.telecom.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.telecom.view.CirclePageIndicator;

public class AppliactionCategoryActivity extends BaseActivity {
    AdvertisementFragmentAdapter mAdapter;
    ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // The look of this sample is set via a style in the manifest
        setContentView(R.layout.application_category_layout);

        mAdapter = new AdvertisementFragmentAdapter(getSupportFragmentManager());

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        indicator.getBackground().setAlpha(0);
    }

    public void onImgUserGuideClick(View view) {
        Intent intent = new Intent(this, UserGuideActivity.class);
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
            return AdvertisementFragment.newInstance(CONTENT[position % CONTENT.length], position);
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

        private int mPosition;

        public static AdvertisementFragment newInstance(int content, int position) {
            AdvertisementFragment fragment = new AdvertisementFragment();

            fragment.mContent = content;
            fragment.mPosition = position;

            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
                mContent = savedInstanceState.getInt(KEY_CONTENT);
            }

            ImageView view = (ImageView) inflater.inflate(R.layout.advertisement_item_layout, null);
            view.setBackgroundResource(mContent);

            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ApplicationDownloadActivity.class);
                    intent.putExtra("position", mPosition + 1);
                    startActivity(intent);
                }
            });
            return view;
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putInt(KEY_CONTENT, mContent);
        }
    }
}