package com.example.text3;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class AppList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AppListAdapter adapter;
    private List<String> appNames;
    private List<String> blockedAppNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applist);

        recyclerView = findViewById(R.id.appListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        appNames = getInstalledApps();

        adapter = new AppListAdapter(this, appNames);
        recyclerView.setAdapter(adapter);

        Button saveButton = findViewById(R.id.appUsageButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCheckedAppNames();
                onBackPressed();
            }
        });
    }

    private List<String> getInstalledApps() {
        List<String> appNames = new ArrayList<>();
        PackageManager packageManager = getPackageManager();
        List<ApplicationInfo> apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo app : apps) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (isDesiredCategory(app.category)) {
                    CharSequence label = app.loadLabel(packageManager);
                    String appName = label != null ? label.toString() : app.packageName;
                    appNames.add(appName);
                }
            }
        }
        return appNames;
    }

    private void saveCheckedAppNames() {
        List<String> checkedPackageNames = new ArrayList<>();
        for (int i = 0; i < adapter.getItemCount(); i++) {
            AppListAdapter.ViewHolder viewHolder = (AppListAdapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
            if (viewHolder != null && viewHolder.appCheckbox.isChecked()) {
                checkedPackageNames.add(getPackageForAppName(appNames.get(i)));
            }
        }

        StringBuilder savedAppsLog = new StringBuilder("Saved Apps: ");
        for (String packageName : checkedPackageNames) {
            savedAppsLog.append(packageName).append(", ");
        }
        if (!checkedPackageNames.isEmpty()) {
            savedAppsLog.setLength(savedAppsLog.length() - 2);
        }
        android.util.Log.d("AppList", savedAppsLog.toString());

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        databaseHelper.saveCheckedApps(checkedPackageNames);

        blockedAppNames = databaseHelper.getBlockedAppNames();

        UnlockCountService unlockCountService = new UnlockCountService();
        unlockCountService.receiveBlockAppNames(blockedAppNames);
    }

    private String getPackageForAppName(String appName) {
        PackageManager packageManager = getPackageManager();
        List<ApplicationInfo> apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo app : apps) {
            CharSequence label = app.loadLabel(packageManager);
            if (label != null && label.toString().equals(appName)) {
                return app.packageName;
            }
        }
        return null;
    }

    private boolean isDesiredCategory(int category) {
        switch (category) {
            case ApplicationInfo.CATEGORY_AUDIO:
                return true;
            case ApplicationInfo.CATEGORY_GAME:
                return true;
            case ApplicationInfo.CATEGORY_NEWS:
                return true;
            case ApplicationInfo.CATEGORY_SOCIAL:
                return true;
            case ApplicationInfo.CATEGORY_VIDEO:
                return true;
            default:
                return false;
        }
    }
}