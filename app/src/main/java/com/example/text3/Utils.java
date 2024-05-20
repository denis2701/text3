package com.example.text3;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.github.mikephil.charting.data.BarEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Utils {
    public static List<UsageStats> getUsageStats(Context context) {
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        long currentTime = System.currentTimeMillis();
        List<UsageStats> stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, currentTime - 1000 * 60, currentTime);
        return stats;
    }

    public static long getTodayStartInMillis() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static String formatDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(timestamp));
    }

    public static String formatDate2(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date(timestamp));
    }

    public static String getAppName(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
            return (String) packageManager.getApplicationLabel(applicationInfo);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static List<String> getDatabaseDates(Context context) {
        List<String> dates = new ArrayList<>();

        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        String[] projection = {
                DatabaseHelper.COLUMN_DATE
        };

        String sortOrder = DatabaseHelper.COLUMN_DATE + " DESC";
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_USAGE_STATS,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE));
                    dates.add(date);
                }
            } finally {
                cursor.close();
            }
        }

        database.close();

        return dates;
    }

    public static List<String> getDatabaseDates2(Context context) {
        List<String> dates = new ArrayList<>();

        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        String[] projection = {
                DatabaseHelper.COLUMN_UNLOCK_COUNT_DATE
        };

        String sortOrder = DatabaseHelper.COLUMN_UNLOCK_COUNT_DATE + " DESC";
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_UNLOCK_COUNTS,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_UNLOCK_COUNT_DATE));
                    dates.add(date);
                }
            } finally {
                cursor.close();
            }
        }

        database.close();

        return dates;
    }

    public static List<BarEntry> getDatabaseEntries(Context context) {
        List<BarEntry> entries = new ArrayList<>();

        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        String[] projection = {
                DatabaseHelper.COLUMN_DATE,
                DatabaseHelper.COLUMN_USAGE_TIME
        };

        String sortOrder = DatabaseHelper.COLUMN_DATE + " DESC";
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_USAGE_STATS,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        if (cursor != null) {
            try {
                int counter = 0;
                int numEntries = cursor.getCount();
                while (cursor.moveToNext() && counter < 7) {
                    int reversedCounter = numEntries - 1 - counter;
                    String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE));
                    long usageTime = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USAGE_TIME));
                    entries.add(new BarEntry(reversedCounter, usageTime));
                    counter++;
                }
            } finally {
                cursor.close();
            }
        }
        database.close();

        return entries;
    }

    public static List<BarEntry> getDatabaseEntries2(Context context) {
        List<BarEntry> entries = new ArrayList<>();

        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        String[] projection = {
                DatabaseHelper.COLUMN_UNLOCK_COUNT_DATE,
                DatabaseHelper.COLUMN_UNLOCK_COUNT
        };

        String sortOrder = DatabaseHelper.COLUMN_UNLOCK_COUNT_DATE + " DESC";
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_UNLOCK_COUNTS,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        if (cursor != null) {
            try {
                int counter = 0;
                int numEntries = cursor.getCount();
                while (cursor.moveToNext() && counter < 7) {
                    int reversedCounter = numEntries - 1 - counter;
                    String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_UNLOCK_COUNT_DATE));
                    long usageTime = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_UNLOCK_COUNT));
                    entries.add(new BarEntry(reversedCounter, usageTime));
                    counter++;
                }
            } finally {
                cursor.close();
            }
        }

        database.close();

        return entries;
    }

    public static String getCategoryForApp(Context context, String packageName) {
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

    public static long getDailyScreenOnTime(Context context) {
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        if (usageStatsManager != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            long startOfDay = calendar.getTimeInMillis();
            long currentTime = System.currentTimeMillis();

            List<UsageStats> stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startOfDay, currentTime);
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

    public static String getAppNameFromPackage(Context context, String packageName) {
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
            return (String) packageManager.getApplicationLabel(applicationInfo);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return packageName;
        }
    }

    public static long convertTimeToMillis(String time) {
        String[] parts = time.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return (hours * 60 + minutes) * 60 * 1000;
    }

}
