package com.telecom.navigation;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
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
import android.widget.Toast;

import com.telecom.util.ApkFileUtil;
import com.telecom.view.CirclePageIndicator;

public class AdvertisementActivity extends BaseActivity {
    AdvertisementFragmentAdapter mAdapter;
    ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // The look of this sample is set via a style in the manifest
        setContentView(R.layout.advertisement_layout);

        mAdapter = new AdvertisementFragmentAdapter(getSupportFragmentManager());

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        indicator.getBackground().setAlpha(0);
    }

    class AdvertisementFragmentAdapter extends FragmentPagerAdapter {
        protected final int[] CONTENT = new int[] { R.drawable.ad1, R.drawable.ad2, R.drawable.ad3 };

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
        public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
                mContent = savedInstanceState.getInt(KEY_CONTENT);
            }

            ImageView view = (ImageView) inflater.inflate(R.layout.advertisement_item_layout, null);
            view.setBackgroundResource(mContent);

            if (mContent == R.drawable.ad3) {
                view.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), AuthenticationActivity.class);
                        startActivity(intent);
//FIXME: for test
//                        PackageInfo packageInfo = ApkFileUtil.getPackageInfo(getActivity(),
//                                "/sdcard/SplashScreen.apk");
//                        Toast.makeText(getActivity(), "packageInfo:"+packageInfo, 1).show();
//                        if (packageInfo != null) {
//                            String packageName = packageInfo.packageName;
//                            Toast.makeText(getActivity(), "packageName:"+packageName, 1).show();
//                            int i = ApkFileUtil.checkApkFileStatuts(getActivity(),
//                                    packageInfo.versionCode, packageName);
//                            if (i == 100) {
//                                ApkFileUtil.launchApp(getActivity(), packageName);
//                            }
//                        }
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
}