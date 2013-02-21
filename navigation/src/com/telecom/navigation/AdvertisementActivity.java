package com.telecom.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.telecom.view.CirclePageIndicator;

public class AdvertisementActivity extends BaseActivity {
    private AdvertisementFragmentAdapter mAdapter;

    private ViewPager mPager;

    private GestureDetector mGestureDetector;

    public static final String EXTRA_KEY_SHARE_PREF = "extra_key_share_pref";

    public static final String EXTRA_KEY_SHARE_FIRST = "extra_key_first";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // The look of this sample is set via a style in the manifest
        setContentView(R.layout.advertisement_layout);

        SharedPreferences settings = getSharedPreferences(EXTRA_KEY_SHARE_PREF,
                Activity.MODE_PRIVATE);
        boolean isFirstUse = settings.getBoolean(EXTRA_KEY_SHARE_FIRST, true);

        if (isFirstUse) {
            mAdapter = new AdvertisementFragmentAdapter(getSupportFragmentManager(), new int[] {
                    R.drawable.ad1, R.drawable.ad2, R.drawable.ad3 });
        } else {
            mAdapter = new AdvertisementFragmentAdapter(getSupportFragmentManager(), new int[] {
                    R.drawable.ad1, R.drawable.ad2 });
        }

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        mPager.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGestureDetector.onTouchEvent(event);
            }
        });

        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        indicator.getBackground().setAlpha(0);

        mGestureDetector = new GestureDetector(this, new MyGestureListener(getApplicationContext()));
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

            ImageView view = (ImageView) inflater.inflate(R.layout.advertisement_item_layout, null);
            view.setBackgroundResource(mContent);

            if (mContent == R.drawable.ad2 || mContent == R.drawable.ad3) {
                view.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        SharedPreferences settings = getActivity().getSharedPreferences(
                                EXTRA_KEY_SHARE_PREF, Activity.MODE_PRIVATE);
                        boolean isFirstUse = settings.getBoolean(EXTRA_KEY_SHARE_FIRST, true);

                        Intent intent = new Intent(getActivity(),
                                isFirstUse ? AuthenticationActivity.class
                                        : AppliactionCategoryActivity.class);
                        startActivity(intent);
                        // TOOD:下载电信掌上营业厅
                        // FIXME: for test
                        // PackageInfo packageInfo =
                        // ApkFileUtil.getPackageInfo(getActivity(),
                        // "/sdcard/SplashScreen.apk");
                        // Toast.makeText(getActivity(),
                        // "packageInfo:"+packageInfo, 1).show();
                        // if (packageInfo != null) {
                        // String packageName = packageInfo.packageName;
                        // Toast.makeText(getActivity(),
                        // "packageName:"+packageName, 1).show();
                        // int i =
                        // ApkFileUtil.checkApkFileStatuts(getActivity(),
                        // packageInfo.versionCode, packageName);
                        // if (i == 100) {
                        // ApkFileUtil.launchApp(getActivity(), packageName);
                        // }
                        // }
                    }
                });
            }
            return view;
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putInt(KEY_CONTENT, mContent);
        }
    }

    private class MyGestureListener extends SimpleOnGestureListener {
        private Context mContext;

        public MyGestureListener(Context context) {
            mContext = context;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (mPager.getCurrentItem() == mPager.getAdapter().getCount() - 1 && velocityX < 0) {
                SharedPreferences settings = mContext.getSharedPreferences(EXTRA_KEY_SHARE_PREF,
                        Activity.MODE_PRIVATE);
                boolean isFirstUse = settings.getBoolean(EXTRA_KEY_SHARE_FIRST, true);

                Intent intent = new Intent(mContext, isFirstUse ? AuthenticationActivity.class
                        : AppliactionCategoryActivity.class);
                startActivity(intent);

                return true;
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }

    }
}