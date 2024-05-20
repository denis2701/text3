package com.example.text3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AppOpsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent; // Import Intent
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static final int USAGE_STATS_PERMISSION_REQUEST = 1001;
    private static final int OVERLAY_PERMISSION_REQUEST = 1002;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigation);

        PhoneUsageNotifier.setAlarm(this);

        startService(new Intent(this, AppBlockerService.class));

        String packageName = getPackageName();
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (!hasUsageStatsPermission() || !hasOverlayPermission() || !pm.isIgnoringBatteryOptimizations(packageName)) {
            Intent intent = new Intent(this, WelcomeScreen.class);
            startActivity(intent);
            finish();
            return;
        } else {
            startBackgroundService();
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.contentFrame, new Dashboard()).commit();
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menu_button1) {
                    replaceFragment(new Dashboard());
                    return true;
                } else if (item.getItemId() == R.id.menu_button2) {
                    replaceFragment(new StatisticsScreen());
                    return true;
                } else if (item.getItemId() == R.id.menu_button3) {
                    replaceFragment(new BlockScreen());
                    return true;
                } else if (item.getItemId() == R.id.menu_button5) {
                    replaceFragment(new Goalscreen());
                    return true;
                } else if (item.getItemId() == R.id.menu_button4) {
                    replaceFragment(new SettingsScreen());
                    return true;
                }
                return false;
            }
        });

        registerBootReceiver();

    }

    private void registerBootReceiver() {
        BroadcastReceiver bootReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Intent serviceIntent = new Intent(context, UnlockCountService.class);
                ContextCompat.startForegroundService(context, serviceIntent);
            }
        };

        IntentFilter filter = new IntentFilter(Intent.ACTION_BOOT_COMPLETED);
        registerReceiver(bootReceiver, filter);
    }

    private boolean hasUsageStatsPermission() {
        AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    private void requestUsageStatsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !hasUsageStatsPermission()) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivityForResult(intent, USAGE_STATS_PERMISSION_REQUEST);
        }
    }

    private boolean hasOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(this);
        }
        return true;
    }

    private void requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !hasOverlayPermission()) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST);
        }
    }

    private void startBackgroundService() {
        Intent serviceIntent = new Intent(this, UnlockCountService.class);
        startService(serviceIntent);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contentFrame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}