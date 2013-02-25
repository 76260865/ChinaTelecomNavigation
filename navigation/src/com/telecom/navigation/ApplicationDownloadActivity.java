package com.telecom.navigation;

import java.util.ArrayList;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.telecom.model.AppInfo;
import com.telecom.model.Customer;
import com.telecom.util.ApkFileUtil;
import com.telecom.util.JsonUtil;

public class ApplicationDownloadActivity extends BaseActivity {

    private DownloadCompleteReceiver mReceiver;

    private MyAdapter mAdapater;

    private ArrayList<AppInfo> mAppInfoList = new ArrayList<AppInfo>();

    private DownloadManager mDownloadManager;

    private View mLayoutDownload;

    private View mLayoutDetail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.application_download_layout);

        mReceiver = new DownloadCompleteReceiver();
        registerReceiver(mReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        mDownloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

        ListView listView = (ListView) findViewById(R.id.lst_app);
        mAdapater = new MyAdapter(this);
        listView.setAdapter(mAdapater);
        listView.setOnItemClickListener(mOnItemClickListener);

        mLayoutDownload = findViewById(R.id.layout_download);
        mLayoutDetail = findViewById(R.id.layout_detail);

        // for (int i = 0; i < 5; i++) {
        // AppInfo info = new AppInfo();
        // mAppInfoList.add(info);
        // }
        new AppListDownloadTask().execute();
    }

    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mLayoutDownload.setVisibility(View.GONE);
            mLayoutDetail.setVisibility(View.VISIBLE);
            Button btnStartDownload = (Button) findViewById(R.id.btn_start_download);
            btnStartDownload.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    mLayoutDetail.setVisibility(View.GONE);
                    mLayoutDownload.setVisibility(View.VISIBLE);
                }
            });
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mAdapater.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
        for (AppInfo info : mAppInfoList) {
            mDownloadManager.remove(info.getDownloadId());
        }
        super.onDestroy();
    }

    public class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public MyAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mAppInfoList.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = mInflater.inflate(R.layout.applocation_list_item_layout, null);

            final TextView titleView = (TextView) convertView.findViewById(R.id.title);
            final TextView infoView = (TextView) convertView.findViewById(R.id.info);
            final Button btnDownload = (Button) convertView.findViewById(R.id.btn_download);

            final AppInfo info = mAppInfoList.get(position);
            titleView.setText(info.getAppName());
            infoView.setText(info.getAppDesc());

            int status = ApkFileUtil.UNINSTALLED;

            if (!TextUtils.isEmpty(info.getPackageName())) {
                status = ApkFileUtil.checkApkFileStatuts(getApplicationContext(), 0,
                        info.getPackageName());
                if (status == ApkFileUtil.INSTALLED) {
                    btnDownload.setText("运行");
                } else if (info.isDownloadComplete()) {
                    btnDownload.setText("安装");
                }
            }

            final int install_status = status;
            btnDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (install_status == ApkFileUtil.INSTALLED) {
                        ApkFileUtil.launchApp(getApplicationContext(), info.getPackageName());
                    } else if (info.isDownloadComplete()) {
                        ApkFileUtil.installApkFile(getApplicationContext(), "");
                    } else {
                        downloadApk(position);
                    }
                }
            });

            return convertView;
        }
    }

    private void downloadApk(int position) {

        // 创建下载请求
        DownloadManager.Request down = new DownloadManager.Request(
                Uri.parse("http://s1.bdstatic.com/r/www/img/i-1.0.0.png"));
        // 设置允许使用的网络类型，这里是移动网络和wifi都可以
        // Environment.getExternalStoragePublicDirectory("'");
        down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        down.setVisibleInDownloadsUi(true);
        // 设置下载后文件存放的位置
        down.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, "123.png");
        // 将下载请求放入队列
        long downloadId = mDownloadManager.enqueue(down);

        AppInfo info = mAppInfoList.get(position);
        info.setDownloadId(downloadId);

        // manager.remove(downloadId);
    }

    public class DownloadCompleteReceiver extends BroadcastReceiver {
        private static final String TAG = "DownloadCompleteReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                Log.d(TAG, " download complete! id : " + downId);
                Toast.makeText(context, "download complete", 1).show();
                for (AppInfo info : mAppInfoList) {
                    if (info.getDownloadId() == downId) {
                        info.setDownloadComplete(true);
                        info.setFilePath(mDownloadManager.getUriForDownloadedFile(downId).getPath());
                        info.setPackageName("adsadsads");
                        break;
                    }
                }
                mAdapater.notifyDataSetChanged();
            }
        }
    }

    private class AppListDownloadTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            mAppInfoList = JsonUtil.getAppList();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mAdapater.notifyDataSetChanged();
        }
    }
}
