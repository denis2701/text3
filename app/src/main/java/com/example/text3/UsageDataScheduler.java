package com.example.text3;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.github.mikephil.charting.data.PieEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class UsageDataScheduler {

    private static final int REQUEST_CODE = 12345;
    private static final int HOUR_OF_DAY = 22;
    private static final int MINUTE = 30;

    public static void scheduleUsageData(Context context, int flag) {
        Intent intent = new Intent(context, UsageDataReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, flag);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, HOUR_OF_DAY);
        calendar.set(Calendar.MINUTE, MINUTE);
        calendar.set(Calendar.SECOND, 0);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public static class UsageDataReceiver extends BroadcastReceiver {
        private static final String TAG = "UsageStatsSample";
        private boolean logOpenCloseEvents = false;
        private String lastPackageName = "";
        private long lastOpenTimestamp = 0;
        private DatabaseHelper databaseHelper;

        @Override
        public void onReceive(Context context, Intent intent) {
            long dailyUsageTime =  getDailyScreenOnTime(context);
            int unblockScreenNumber = getUnlockCount(context);
            retrieveCategoryUsageStats(context);

            String currentDate = getCurrentDate();

            insertUsageTime(context, currentDate, dailyUsageTime);
            insertUnblockNumber(context, currentDate, unblockScreenNumber);

            DatabaseHelper dbHelper = new DatabaseHelper(context);
            dbHelper.removeDuplicateEntries();

            List<Dashboard.AppUsageInfo> appUsageList = getMostUsedApps(context);
            List<Dashboard.AppUsageUnlockInfo> appUsageUnlockInfo = getMostUsedUnlockedApps(context);

            retrieveUsageStats(context);
        }

        private long getDailyScreenOnTime(Context context) {
            UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            if (usageStatsManager != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                long startOfDay = calendar.getTimeInMillis();
                long endOfDay = startOfDay + 24 * 60 * 60 * 1000;

                long currentTime = System.currentTimeMillis();

                List<UsageStats> stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startOfDay, endOfDay);
                if (stats != null) {
                    long totalUsageTime = 0;
                    for (UsageStats usageStats : stats) {
                        long lastTimeUsed = usageStats.getLastTimeUsed();
                        if (lastTimeUsed >= startOfDay && lastTimeUsed < currentTime) {
                            totalUsageTime += usageStats.getTotalTimeInForeground();
                        }
                    }
                    return totalUsageTime;
                }
            }
            return 0;
        }
        private int getUnlockCount(Context context) {
            int unlockCount = 0;

            UsageStatsManager mUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            if (mUsageStatsManager != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                long startTime = calendar.getTimeInMillis();
                long endTime = System.currentTimeMillis();

                UsageEvents usageEvents = mUsageStatsManager.queryEvents(startTime, endTime);
                UsageEvents.Event currentEvent = new UsageEvents.Event();

                while (usageEvents.hasNextEvent()) {
                    usageEvents.getNextEvent(currentEvent);

                    if (currentEvent.getEventType() == UsageEvents.Event.KEYGUARD_HIDDEN) {
                        unlockCount++;
                    }
                }
                return unlockCount;
            } else {
                Toast.makeText(context, "Sorry...", Toast.LENGTH_SHORT).show();
                return 0;
            }
        }
        private String getCurrentDate() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = new Date();
            return dateFormat.format(date);
        }
        private void insertUsageTime(Context context, String date, long usageTime) {
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            Cursor cursor = null;

            try {
                cursor = database.query(
                        DatabaseHelper.TABLE_USAGE_STATS,
                        new String[]{DatabaseHelper.COLUMN_DATE, DatabaseHelper.COLUMN_USAGE_TIME},
                        DatabaseHelper.COLUMN_DATE + " = ?",
                        new String[]{date},
                        null,
                        null,
                        null
                );

                if (cursor != null && cursor.moveToFirst()) {
                    ContentValues updateValues = new ContentValues();
                    updateValues.put(DatabaseHelper.COLUMN_USAGE_TIME, usageTime);
                    database.update(DatabaseHelper.TABLE_USAGE_STATS, updateValues, DatabaseHelper.COLUMN_DATE + " = ?", new String[]{date});
                } else {
                    ContentValues values = new ContentValues();
                    values.put(DatabaseHelper.COLUMN_DATE, date);
                    values.put(DatabaseHelper.COLUMN_USAGE_TIME, usageTime);
                    database.insert(DatabaseHelper.TABLE_USAGE_STATS, null, values);
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
                database.close();
            }
        }
        private void insertUnblockNumber(Context context, String date, int unblockCount) {
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            Cursor cursor = null;

            try {
                cursor = database.query(
                        DatabaseHelper.TABLE_UNLOCK_COUNTS,
                        new String[]{DatabaseHelper.COLUMN_UNLOCK_COUNT_DATE, DatabaseHelper.COLUMN_UNLOCK_COUNT},
                        DatabaseHelper.COLUMN_UNLOCK_COUNT_DATE + " = ?",
                        new String[]{date},
                        null,
                        null,
                        null
                );

                if (cursor != null && cursor.moveToFirst()) {
                    ContentValues updateValues = new ContentValues();
                    updateValues.put(DatabaseHelper.COLUMN_UNLOCK_COUNT, unblockCount);
                    database.update(DatabaseHelper.TABLE_UNLOCK_COUNTS, updateValues, DatabaseHelper.COLUMN_UNLOCK_COUNT_DATE + " = ?", new String[]{date});
                } else {
                    ContentValues values = new ContentValues();
                    values.put(DatabaseHelper.COLUMN_UNLOCK_COUNT_DATE, date);
                    values.put(DatabaseHelper.COLUMN_UNLOCK_COUNT, unblockCount);
                    database.insert(DatabaseHelper.TABLE_UNLOCK_COUNTS, null, values);
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
                database.close();
            }
        }
        private List<Dashboard.AppUsageInfo> getMostUsedApps(Context context) {
            String currentDate = getCurrentDate();
            UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            List<UsageStats> appUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, getStartOfDay(), System.currentTimeMillis());

            List<Dashboard.AppUsageInfo> appUsageList = new ArrayList<>();

            if (appUsageStats != null) {
                DatabaseHelper databaseHelper = new DatabaseHelper(context);
                SQLiteDatabase db = databaseHelper.getWritableDatabase();

                for (UsageStats usageStats : appUsageStats) {
                    if (usageStats.getTotalTimeInForeground() > 0) {
                        Dashboard.AppUsageInfo appUsageInfo = new Dashboard.AppUsageInfo();
                        appUsageInfo.setPackageName(usageStats.getPackageName());
                        appUsageInfo.setAppName(getAppNameFromPackage(context, usageStats.getPackageName()));
                        appUsageInfo.setUsageTime(usageStats.getTotalTimeInForeground());
                        appUsageList.add(appUsageInfo);

                        databaseHelper.addAppUsage(appUsageInfo.getPackageName(), appUsageInfo.getAppName(), appUsageInfo.getUsageTime());
                    }
                }

                db.close();

                appUsageList = databaseHelper.getMostUsedApps(currentDate, 5);

                for (Dashboard.AppUsageInfo appUsageInfo : appUsageList) {
                    Log.d("AppUsageList", appUsageInfo.getAppName() + " - " + appUsageInfo.getUsageTime());
                }
            }

            return appUsageList;
        }

        private List<Dashboard.AppUsageUnlockInfo> getMostUsedUnlockedApps(Context context) {
            UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            long startOfDay = getStartOfDay();
            long endOfDay = System.currentTimeMillis();

            UsageEvents usageEvents = usageStatsManager.queryEvents(startOfDay, endOfDay);
            SparseArray<Dashboard.AppUsageUnlockInfo> appUsageUnlockInfoMap = new SparseArray<>();

            while (usageEvents.hasNextEvent()) {
                UsageEvents.Event event = new UsageEvents.Event();
                usageEvents.getNextEvent(event);

                if (event.getEventType() == UsageEvents.Event.ACTIVITY_RESUMED) {
                    String packageName = event.getPackageName();
                    Dashboard.AppUsageUnlockInfo appUsageUnlockInfo = appUsageUnlockInfoMap.get(packageName.hashCode());

                    if (appUsageUnlockInfo == null) {
                        appUsageUnlockInfo = new Dashboard.AppUsageUnlockInfo();
                        appUsageUnlockInfo.setPackageName(packageName);
                        appUsageUnlockInfo.setAppName(getAppNameFromPackage(context, packageName));
                        appUsageUnlockInfo.setLaunchCount(1);
                        appUsageUnlockInfoMap.put(packageName.hashCode(), appUsageUnlockInfo);
                    } else {
                        appUsageUnlockInfo.setLaunchCount(appUsageUnlockInfo.getLaunchCount() + 1);
                    }
                }
            }

            DatabaseHelper databaseHelper = new DatabaseHelper(context);
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            for (int i = 0; i < appUsageUnlockInfoMap.size(); i++) {
                Dashboard.AppUsageUnlockInfo appUsageUnlockInfo = appUsageUnlockInfoMap.valueAt(i);
                databaseHelper.addUnblockAppUsage(appUsageUnlockInfo.getPackageName(), appUsageUnlockInfo.getAppName(), appUsageUnlockInfo.getLaunchCount());
            }
            db.close();

            List<Dashboard.AppUsageUnlockInfo> appUsageUnlockInfoList = new ArrayList<>();
            for (int i = 0; i < appUsageUnlockInfoMap.size(); i++) {
                appUsageUnlockInfoList.add(appUsageUnlockInfoMap.valueAt(i));
            }

            Collections.sort(appUsageUnlockInfoList, (app1, app2) -> Integer.compare(app2.getLaunchCount(), app1.getLaunchCount()));
            if (appUsageUnlockInfoList.size() > 5) {
                appUsageUnlockInfoList = appUsageUnlockInfoList.subList(0, 5);
            }

            for (Dashboard.AppUsageUnlockInfo appUsageUnlockInfo : appUsageUnlockInfoList) {
                Log.d("AppUsageUnlockList", appUsageUnlockInfo.getAppName() + " - Launch Count: " + appUsageUnlockInfo.getLaunchCount());
            }

            return appUsageUnlockInfoList;
        }

        private String getAppNameFromPackage(Context context2, String packageName) {
            try {
                PackageManager packageManager = context2.getPackageManager();
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
                return (String) packageManager.getApplicationLabel(applicationInfo);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                return packageName;
            }
        }

        private long getStartOfDay() {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar.getTimeInMillis();
        }

        private void retrieveUsageStats(Context context) {
            if (context == null) {
                Log.e("retrieveUsageStats", "Context is null");
                return;
            }

            UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);

            if (usageStatsManager == null) {
                Log.e("UsageStatsManager", "UsageStatsManager is null");
                return;
            }

            long endTime = System.currentTimeMillis();
            long startTime = getTodayStartInMillis();

            UsageEvents usageEvents = usageStatsManager.queryEvents(startTime, endTime);

            List<AppHistoryData> usageStatsDataList = new ArrayList<>();

            if (usageEvents != null) {

                while (usageEvents.hasNextEvent()) {
                    UsageEvents.Event event = new UsageEvents.Event();
                    usageEvents.getNextEvent(event);
                    if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                        lastPackageName = event.getPackageName();
                        lastOpenTimestamp = event.getTimeStamp();
                        logOpenCloseEvents = true;
                    } else if (event.getEventType() == UsageEvents.Event.MOVE_TO_BACKGROUND && lastPackageName.equals(event.getPackageName()) && logOpenCloseEvents) {
                        long closeTimestamp = event.getTimeStamp();
                        if (closeTimestamp - lastOpenTimestamp > 60000) {
                            String openTime = formatDate(lastOpenTimestamp);
                            String closeTime = formatDate(closeTimestamp);
                            usageStatsDataList.add(new AppHistoryData(event.getPackageName(), getAppName(context, event.getPackageName()), openTime, closeTime));

                            DatabaseHelper databaseHelper = new DatabaseHelper(context);
                            databaseHelper.insertAppUsageData(formatDate2(lastOpenTimestamp), event.getPackageName(), getAppName(context, event.getPackageName()), lastOpenTimestamp, closeTimestamp);
                        }
                        logOpenCloseEvents = false;
                    }
                }
            } else {
                Log.d("UsageStatsManager", "No usage events retrieved.");
            }

            AppHistoryAdapter adapter = new AppHistoryAdapter(usageStatsDataList, context.getPackageManager());
        }

        private long getTodayStartInMillis() {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            return calendar.getTimeInMillis();
        }

        private String formatDate(long timestamp) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.format(new Date(timestamp));
        }

        private String formatDate2(long timestamp) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(new Date(timestamp));
        }

        private String getAppName(Context context, String packageName) {
            PackageManager packageManager = context.getPackageManager();
            try {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
                return (String) packageManager.getApplicationLabel(applicationInfo);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            return "";
        }

        private void retrieveCategoryUsageStats(Context context) {
            List<UsageStats> stats = getUsageStats(context);

            Map<String, Long> categoryUsageMap = new HashMap<>();

            PackageManager packageManager = context.getPackageManager();

            for (UsageStats usageStats : stats) {
                String packageName = usageStats.getPackageName();

                try {
                    ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
                    String category = getCategoryForApp(context, packageName);

                    long totalTimeInForeground = usageStats.getTotalTimeInForeground();
                    if (category != null && totalTimeInForeground > 0) {
                        categoryUsageMap.put(category, categoryUsageMap.getOrDefault(category, 0L) + totalTimeInForeground);
                    }

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }

            List<PieEntry> pieEntries = new ArrayList<>();

            for (Map.Entry<String, Long> entry : categoryUsageMap.entrySet()) {
                Long value = entry.getValue();
                System.out.println("Value for category " + entry.getKey() + ": " + value); // Log the value
                pieEntries.add(new PieEntry(entry.getValue(), entry.getKey()));
            }

            for (PieEntry entry : pieEntries) {
                Log.d("PieEntry", "Category: " + entry.getLabel() + ", Value: " + entry.getValue());
                DatabaseHelper dbHelper = new DatabaseHelper(context);
                dbHelper.insertPieChartData(entry.getLabel(), (long) entry.getValue());
            }
        }

        private List<UsageStats> getUsageStats(Context context) {
            UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            long currentTime = System.currentTimeMillis();
            List<UsageStats> stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, currentTime - 1000 * 60, currentTime);
            return stats;
        }

        private String getCategoryForApp(Context context, String packageName) {
            PackageManager packageManager = context.getPackageManager();
            try {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
                int category = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    category = applicationInfo.category;
                }

                switch (category) {
                    case ApplicationInfo.CATEGORY_ACCESSIBILITY:
                        return "Prieinamumas";
                    case ApplicationInfo.CATEGORY_AUDIO:
                        return "Muzika";
                    case ApplicationInfo.CATEGORY_GAME:
                        return "Žaidimai";
                    case ApplicationInfo.CATEGORY_IMAGE:
                        return "Galerija";
                    case ApplicationInfo.CATEGORY_MAPS:
                        return "Žemėlapiai ir Navigacija";
                    case ApplicationInfo.CATEGORY_NEWS:
                        return "Žinios";
                    case ApplicationInfo.CATEGORY_PRODUCTIVITY:
                        return "Produktyvumas";
                    case ApplicationInfo.CATEGORY_SOCIAL:
                        return "Socialiniai tinklai";
                    case ApplicationInfo.CATEGORY_UNDEFINED:
                        return "Kita";
                    case ApplicationInfo.CATEGORY_VIDEO:
                        return "Video ir kinas";
                    default:
                        return "Nenustatyta";
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            return "Undefined";
        }
    }
}
