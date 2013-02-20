package com.telecom.model;

import android.graphics.Bitmap;

public class AppInfo {

    private Bitmap mIcon;

    private String mAppName;

    private String mAppDesc;

    private String mPackageName;

    private int mVersionCode;

    private long mDownloadId;

    private boolean mDownloadComplete;

    public Bitmap getIcon() {
        return mIcon;
    }

    public void setIcon(Bitmap icon) {
        this.mIcon = icon;
    }

    public String getAppName() {
        return mAppName;
    }

    public void setAppName(String appName) {
        this.mAppName = appName;
    }

    public String getAppDesc() {
        return mAppDesc;
    }

    public void setAppDesc(String appDesc) {
        this.mAppDesc = appDesc;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public void setPackageName(String packageName) {
        this.mPackageName = packageName;
    }

    public int getVersionCode() {
        return mVersionCode;
    }

    public void setVersionCode(int versionCode) {
        this.mVersionCode = versionCode;
    }

    public long getDownloadId() {
        return mDownloadId;
    }

    public void setDownloadId(long downloadId) {
        this.mDownloadId = downloadId;
    }

    public boolean isDownloadComplete() {
        return mDownloadComplete;
    }

    public void setDownloadComplete(boolean downloadComplete) {
        this.mDownloadComplete = downloadComplete;
    }
}
