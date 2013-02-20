package com.telecom.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class DownloadCompleteReceiver extends BroadcastReceiver {
    private static final String TAG = "DownloadCompleteReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            Log.d(TAG, " download complete! id : " + downId);
            Toast.makeText(context, "download complete", 1).show();
        }
    }
}
