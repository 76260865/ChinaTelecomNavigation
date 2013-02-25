package com.telecom.model;

import android.graphics.Bitmap;

public class AppInfo {

    private Bitmap mIcon;

    private String mAppName;

    private String mAppDesc;

    private String mPackageName;

    private String mVersionCode;

    private long mDownloadId;

    private boolean mDownloadComplete;

    private String mAppIconUri;

    private String mAppId;

    private String mDownLink;

    private String mAppScreen;

    private String mFilePath;

    public String getFilePath() {
        return mFilePath;
    }

    public void setFilePath(String filePath) {
        this.mFilePath = filePath;
    }

    public String getAppIconUri() {
        return mAppIconUri;
    }

    public void setAppIconUri(String appIconUri) {
        this.mAppIconUri = appIconUri;
    }

    public String getAppId() {
        return mAppId;
    }

    public void setAppId(String appId) {
        this.mAppId = appId;
    }

    public String getDownLink() {
        return mDownLink;
    }

    public void setDownLink(String downLink) {
        this.mDownLink = downLink;
    }

    public String getAppScreen() {
        return mAppScreen;
    }

    public void setAppScreen(String appScreen) {
        this.mAppScreen = appScreen;
    }

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

    public String getVersionCode() {
        return mVersionCode;
    }

    public void setVersionCode(String versionCode) {
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
