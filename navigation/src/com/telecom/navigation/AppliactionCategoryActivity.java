package com.telecom.navigation;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.telecom.model.AppInfo;
import com.telecom.util.ApkFileUtil;
import com.telecom.util.JsonUtil;
import com.telecom.view.CirclePageIndicator;

public class AppliactionCategoryActivity extends BaseActivity {

    private ViewPager mPager;

    private LayoutInflater mLayoutInflater;

    /** Bitmap cache */
    private LruCache<Integer, Bitmap> mBitmapCache;

    // Use 2M memory for this memory cache.
    private static final int cacheSize = 1024 * 1024 * 2;

    protected final int[] CONTENT = new int[] { R.drawable.ic_type1, R.drawable.ic_type2,
            R.drawable.ic_type3, R.drawable.ic_type4, R.drawable.ic_type5, R.drawable.ic_type6,
            R.drawable.ic_type7, R.drawable.ic_type8 };

    protected final int[] BACKGROUND = new int[] { R.drawable.ic_type1_background,
            R.drawable.ic_type2_background, R.drawable.ic_type3_background,
            R.drawable.ic_type4_background, R.drawable.ic_type5_background,
            R.drawable.ic_type6_background, R.drawable.ic_type7_background,
            R.drawable.ic_type8_background };

    private View mLayoutCategory;
    private View mLayoutDownladParent;
    private static final String URI_APP = "http://118.121.17.250";

    private DownloadCompleteReceiver mReceiver;

    private MyAdapter mAdapater;

    private ArrayList<AppInfo> mAppInfoList = new ArrayList<AppInfo>();

    private DownloadManager mDownloadManager;

    private View mLayoutDownload;

    private View mLayoutDetail;

    private ImageView mImageAppScreenView;

    private ProgressDialog mProgressDialog;

    private ArrayList<Long> mDownloadIds = new ArrayList<Long>();
    private int mIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // The look of this sample is set via a style in the manifest
        setContentView(R.layout.application_category_layout);

        mLayoutInflater = LayoutInflater.from(this);

        mBitmapCache = new LruCache<Integer, Bitmap>(cacheSize);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new MyPagerAdapter());

        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        indicator.getBackground().setAlpha(0);

        mLayoutCategory = findViewById(R.id.layout_category);
        mLayoutDownladParent = findViewById(R.id.layout_download_parent);
        mReceiver = new DownloadCompleteReceiver();
        registerReceiver(mReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        mDownloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

        ListView listView = (ListView) findViewById(R.id.lst_app);
        mAdapater = new MyAdapter(this);
        listView.setAdapter(mAdapater);
        listView.setOnItemClickListener(mOnItemClickListener);

        mLayoutDownload = findViewById(R.id.layout_download);
        mLayoutDetail = findViewById(R.id.layout_detail);
    }

    @Override
    public void onBackPressed() {
        if (mIndex == 2) {
            mIndex--;
            mLayoutDetail.setVisibility(View.GONE);
            mLayoutDownload.setVisibility(View.VISIBLE);
        } else if (mIndex == 1) {
            mIndex--;
            mLayoutDownladParent.setVisibility(View.GONE);
            mLayoutCategory.setVisibility(View.VISIBLE);
            mAppInfoList.clear();
        } else {
            showDialog(0);
        }
    }

    @Override
    protected void exit() {
        for (Long id : mDownloadIds) {
            if (id > 0) {
                mDownloadManager.remove(id);
            }
        }
        mDownloadIds.clear();
        super.exit();
    }

    @Override
    protected void onDestroy() {
        mBitmapCache.evictAll();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
        super.onDestroy();
    }

    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            mIndex++;
            mLayoutDownload.setVisibility(View.GONE);
            mLayoutDetail.setVisibility(View.VISIBLE);
            final AppInfo info = mAppInfoList.get(position);
            Button btnStartDownload = (Button) findViewById(R.id.btn_start_download);
            mImageAppScreenView = (ImageView) findViewById(R.id.img_app_screen);
            TextView txtAppName = (TextView) findViewById(R.id.txt_app_name);
            txtAppName.setText(info.getAppName());
            TextView txtAppIntroduce = (TextView) findViewById(R.id.txt_app_introduce);
            txtAppIntroduce.setText(info.getAppDesc());

            if (info.getAppScreenIcon() == null) {
                mImageAppScreenView.setImageBitmap(null);
                new AppScreenIconDownloadTask(info).execute(URI_APP + info.getAppScreen());
            } else {
                mImageAppScreenView.setImageBitmap(info.getAppScreenIcon());
            }

            if (info.getDownloadId() > 0) {
                btnStartDownload.setText(R.string.btn_back_txt);
            } else {
                btnStartDownload.setText(R.string.btn_start_download_txt);
            }
            btnStartDownload.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    mLayoutDetail.setVisibility(View.GONE);
                    mLayoutDownload.setVisibility(View.VISIBLE);
                    mIndex--;
                    if (info.getDownloadId() < 0) {
                        downloadApk(position);
                    }
                }
            });
        }
    };

    public class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public MyAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mAppInfoList.size();
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = mInflater.inflate(R.layout.applocation_list_item_layout, null);

            final ImageView imgView = (ImageView) convertView.findViewById(R.id.img);
            final TextView titleView = (TextView) convertView.findViewById(R.id.title);
            final TextView infoView = (TextView) convertView.findViewById(R.id.info);
            final Button btnDownload = (Button) convertView.findViewById(R.id.btn_download);

            final AppInfo info = mAppInfoList.get(position);
            if (info.getIcon() == null) {
                new AppIconDownloadTask(imgView, info).execute(URI_APP + info.getAppIconUri());
            } else {
                imgView.setImageBitmap(info.getIcon());
            }

            titleView.setText(info.getAppName());
            infoView.setText(info.getAppDesc());

            int status = ApkFileUtil.UNINSTALLED;

            if (!TextUtils.isEmpty(info.getPackageName())) {
                status = ApkFileUtil.checkApkFileStatuts(getApplicationContext(), 0,
                        info.getPackageName());
                if (status == ApkFileUtil.INSTALLED) {
                    btnDownload.setText(R.string.btn_run_txt);
                } else if (info.isDownloadComplete()) {
                    btnDownload.setText(R.string.btn_install_txt);
                }
            }

            final int install_status = status;
            btnDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (install_status == ApkFileUtil.INSTALLED) {
                        ApkFileUtil.launchApp(getApplicationContext(), info.getPackageName());
                    } else if (info.isDownloadComplete()) {
                        ApkFileUtil.installApkFile(getApplicationContext(), info.getFilePath());
                        mAppList += info.getAppId() + ",";
                    } else {
                        downloadApk(position);
                    }
                }
            });

            return convertView;
        }
    }

    private void downloadApk(int position) {
        AppInfo info = mAppInfoList.get(position);
        // 创建下载请求
        DownloadManager.Request down = new DownloadManager.Request(Uri.parse(URI_APP
                + info.getDownLink()));

        // 设置允许使用的网络类型，这里是移动网络和wifi都可以
        down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
                | DownloadManager.Request.NETWORK_WIFI);
        down.setVisibleInDownloadsUi(true);
        down.setTitle(info.getAppName());
        info.fileName = info.getAppId() + System.currentTimeMillis() + ".apk";
        // 设置下载后文件存放的位置
        down.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, info.fileName);

        if (info.getDownloadId() > 0) {
            mDownloadManager.remove(info.getDownloadId());
        }
        // 将下载请求放入队列
        long downloadId = mDownloadManager.enqueue(down);

        info.setDownloadId(downloadId);
        mDownloadIds.add(downloadId);
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

                if (cursor.moveToFirst()) {
                    switch (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
                    case DownloadManager.STATUS_SUCCESSFUL: {
                        Toast.makeText(context, R.string.msg_download_complete, Toast.LENGTH_LONG)
                                .show();
                        Log.d(TAG, " download complete! id : " + downId);

                        for (AppInfo info : mAppInfoList) {
                            if (info.getDownloadId() == downId) {
                                info.setDownloadComplete(true);
                                File path = Environment
                                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                                String realPath = new File(path, info.fileName).getPath();

                                Log.d(TAG, realPath);

                                info.setFilePath(realPath);
                                PackageInfo packageInfo = ApkFileUtil.getPackageInfo(
                                        getApplicationContext(), info.getFilePath());
                                Log.d(TAG, "packageInfo:" + packageInfo);

                                if (packageInfo != null) {
                                    info.setPackageName(packageInfo.packageName);
                                }
                                
                                
                                ApkFileUtil.installApkFile(getApplicationContext(), info.getFilePath());
                                mAppList += info.getAppId() + ",";
                                break;
                            }
                        }
                        mAdapater.notifyDataSetChanged();
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

    private class AppListDownloadTask extends AsyncTask<Void, Void, Void> {
        private int mPosition;

        public AppListDownloadTask(int position) {
            mPosition = position;
        }

        @Override
        protected Void doInBackground(Void... params) {
            mAppInfoList = JsonUtil.getAppList(mPosition);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (mAppInfoList.size() == 0) {
                Toast.makeText(getApplicationContext(), R.string.msg_get_app_list_failed,
                        Toast.LENGTH_LONG).show();
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
                return;
            }
            mAdapater.notifyDataSetChanged();
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
        }
    }

    private class AppIconDownloadTask extends AsyncTask<String, Void, Bitmap> {

        private ImageView mImgView;
        private AppInfo mAppInfo;

        public AppIconDownloadTask(ImageView imgView, AppInfo appInfo) {
            mImgView = imgView;
            mAppInfo = appInfo;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return getBitmapFromUrl(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            mAppInfo.setIcon(result);
            mImgView.setImageBitmap(result);
        }
    }

    private class AppScreenIconDownloadTask extends AsyncTask<String, Void, Bitmap> {

        private AppInfo mAppInfo;

        public AppScreenIconDownloadTask(AppInfo appInfo) {
            mAppInfo = appInfo;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return getBitmapFromUrl(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            mAppInfo.setAppScreenIcon(result);
            mImageAppScreenView.setImageBitmap(result);
        }
    }

    private Bitmap getBitmapFromUrl(String imgUrl) {
        URL url;
        Bitmap bitmap = null;
        try {
            url = new URL(imgUrl);
            InputStream is = url.openConnection().getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bitmap = BitmapFactory.decodeStream(bis);
            bis.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private Bitmap getBitmapFromCache(Integer key) {
        return mBitmapCache.get(key);
    }

    public void onImgUserGuideClick(View view) {
        Intent intent = new Intent(this, UserGuideActivity.class);
        startActivity(intent);
    }

    class MyPagerAdapter extends PagerAdapter {
        private ArrayList<LinearLayout> views = new ArrayList<LinearLayout>();

        public MyPagerAdapter() {

            for (int i = 0; i < CONTENT.length; i++) {
                LinearLayout view = (LinearLayout) mLayoutInflater.inflate(
                        R.layout.application_category_item_layout, null);
                views.add(view);
            }
        }

        @Override
        public Object instantiateItem(View v, final int position) {
            LinearLayout view = views.get(position);
            Bitmap bitmap = getBitmapFromCache(position);
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(getResources(), CONTENT[position]);
            }
            ImageView imgView = (ImageView) view.findViewById(R.id.img_advtisement);
            imgView.setImageBitmap(bitmap);
            view.setBackgroundResource(BACKGROUND[position]);

            ((ViewPager) v).addView(view);
            view.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // Intent intent = new
                    // Intent(AppliactionCategoryActivity.this,
                    // ApplicationDownloadActivity.class);
                    // intent.putExtra("position", position + 1);
                    // startActivity(intent);

                    // show the download layout:
                    mLayoutCategory.setVisibility(View.GONE);
                    mLayoutDownladParent.setVisibility(View.VISIBLE);
                    new AppListDownloadTask(position + 1).execute();
                    mIndex++;
                }
            });
            return view;
        }

        @Override
        public void destroyItem(View v, int position, Object arg2) {
            LinearLayout view = views.get(position);
            ((ViewPager) v).removeView(view);
        }

        @Override
        public int getCount() {
            return CONTENT.length;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }
}