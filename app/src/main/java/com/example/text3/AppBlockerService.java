package com.example.text3;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.util.List;

public class AppBlockerService extends Service {

    private static final String TAG = "AppBlockerService";
    private static final String CHANNEL_ID = "UnlockCountChannel";

    private BroadcastReceiver appLaunchReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String packageName = getPackageNameFromIntent(intent);
            if (packageName != null) {
                Log.d(TAG, "Opened app package: " + packageName);

                if (isAppBlocked(packageName)) {
                    Intent overlayIntent = new Intent(context, OverlayActivity.class);
                    overlayIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(overlayIntent);
                }
            } else {
                Log.e(TAG, "Package name is null");
            }
        }
    };

    private String getPackageNameFromIntent(Intent intent) {
        if (intent != null && intent.getAction() != null && intent.getData() != null) {
            return intent.getData().getSchemeSpecificPart();
        }
        return null;
    }

    private boolean isAppBlocked(String packageName) {
        Log.d(TAG, "Checking if app is blocked: " + packageName);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        List<String> blockedAppNames = dbHelper.getBlockedAppNames();

        boolean isBlocked = blockedAppNames.contains(packageName);

        Log.d(TAG, "Is app " + packageName + " blocked? " + (isBlocked ? "Yes" : "No"));

        return isBlocked;
    }

    private BroadcastReceiver packageAddedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction() != null && intent.getData() != null) {
                String packageName = intent.getData().getSchemeSpecificPart();
                if (packageName != null) {
                    Log.d(TAG, "Installed app package: " + packageName);
                } else {
                    Log.e(TAG, "Package name is null");
                }
            } else {
                Log.e(TAG, "Intent or action or data is null");
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "AppBlockerService onCreate");

        IntentFilter launchFilter = new IntentFilter(Intent.ACTION_PACKAGE_REPLACED);
        launchFilter.addDataScheme("package");
        registerReceiver(appLaunchReceiver, launchFilter);

        IntentFilter addedFilter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        addedFilter.addDataScheme("package");
        registerReceiver(packageAddedReceiver, addedFilter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "AppBlockerService onDestroy");
        unregisterReceiver(appLaunchReceiver);
        unregisterReceiver(packageAddedReceiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Unlock Count Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }

    private Notification createNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Usage App")
                .setContentText("Running in the background")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setColor(ContextCompat.getColor(this, R.color.black))
                .setOngoing(true)
                .build();
    }
}