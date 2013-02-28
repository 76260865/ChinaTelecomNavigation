package com.telecom.navigation;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.telecom.model.Customer;
import com.telecom.util.ApkFileUtil;
import com.telecom.util.JsonUtil;
import com.telecom.util.NetworkUtil;
import com.telecom.view.CirclePageIndicator;

public class AdvertisementActivity extends BaseActivity {
    private AdvertisementFragmentAdapter mAdapter;

    private ViewPager mPager;

    private DownloadCompleteReceiver mReceiver;

    private DownloadManager mDownloadManager;

    private GestureDetector mGestureDetector;

    public static final String EXTRA_KEY_SHARE_PREF = "extra_key_share_pref";

    public static final String EXTRA_KEY_SHARE_FIRST = "extra_key_first";

    private TelephonyManager mTelephonyMgr;

    private String mFileName;

    private long mDownloadId = -1l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // The look of this sample is set via a style in the manifest
        setContentView(R.layout.advertisement_layout);

        mDownloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

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

        mStartTime = new Date();

        mReceiver = new DownloadCompleteReceiver();
        registerReceiver(mReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    protected void onDestroy() {
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
        if (mDownloadId > -1l) {
            mDownloadManager.remove(mDownloadId);
        }
        super.onDestroy();
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

            if (mContent == R.drawable.ad3) {
                view.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (!NetworkUtil.isNetworkConnected(getActivity())) {
                            Toast.makeText(getActivity(), R.string.txt_connect_network,
                                    Toast.LENGTH_LONG).show();
                            return;
                        }
                        ((AdvertisementActivity) getActivity()).downloadApk();
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

    private void downloadApk() {
        if (TextUtils.isEmpty(mDownloadUrl)) {
            return;
        }
        // 创建下载请求
        DownloadManager.Request down = new DownloadManager.Request(Uri.parse(mDownloadUrl));

        // 设置允许使用的网络类型，这里是移动网络和wifi都可以
        down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
                | DownloadManager.Request.NETWORK_WIFI);
        down.setVisibleInDownloadsUi(true);
        down.setTitle(getString(R.string.download_title));
        mFileName = System.currentTimeMillis() + ".apk";
        // 设置下载后文件存放的位置
        down.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, mFileName);
        // 将下载请求放入队列
        mDownloadId = mDownloadManager.enqueue(down);
    }

    public class DownloadCompleteReceiver extends BroadcastReceiver {
        private static final String TAG = "DownloadCompleteReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {

            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
                long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(downId);
                Cursor cursor = mDownloadManager.query(query);

                if (cursor.moveToFirst() && mDownloadId == downId) {
                    switch (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
                    case DownloadManager.STATUS_SUCCESSFUL: {
                        Toast.makeText(context, R.string.msg_download_complete, Toast.LENGTH_LONG)
                                .show();
                        Log.d(TAG, " download complete! id : " + downId);

                        File path = Environment
                                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                        String realPath = new File(path, mFileName).getPath();
                        ApkFileUtil.installApkFile(getApplicationContext(), realPath);
                        break;
                    }
                    case DownloadManager.STATUS_FAILED: {
                        Toast.makeText(context, R.string.msg_download_failed, Toast.LENGTH_LONG)
                                .show();
                        break;
                    }
                    default:
                        break;
                    }
                }

            }
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

                Intent intent = new Intent(
                        mContext,
                        isFirstUse && NetworkUtil.isNetworkConnected(AdvertisementActivity.this) ? AuthenticationActivity.class
                                : AppliactionCategoryActivity.class);
                startActivity(intent);

                return true;
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }
}