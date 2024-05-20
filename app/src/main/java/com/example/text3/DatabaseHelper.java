package com.example.text3;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UsageStatsDatabase";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_USAGE_STATS = "usage_stats";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_USAGE_TIME = "usage_time";

    public static final String TABLE_UNLOCK_COUNTS = "unlock_counts";
    public static final String COLUMN_UNLOCK_COUNT_ID = "_id";
    public static final String COLUMN_UNLOCK_COUNT = "unlock_count";
    public static final String COLUMN_UNLOCK_COUNT_DATE = "unlock_count_date";

    public static final String TABLE_APP_SWITCHES = "app_switches";
    public static final String SWOTCH_COLUMN_ID = "_id";
    public static final String COLUMN_PACKAGE_NAME = "package_name";
    public static final String COLUMN_SWITCH_POSITION = "switch_position";

    private static final String TABLE_CATEGORY_USAGE = "pie_chart_data";
    private static final String CATEGORY_COLUMN_DATE = "category_date";
    private static final String TABLE_COLUMN_CATEGORY = "category";
    private static final String CATEGORY_COLUMN_VALUE = "value";

    private static final String APP_TIME_USAGE = "app_time_usage";
    private static final String APP_TIME_COLUMN_DATE = "app_time_date";
    private static final String APP_TIME_COLUMN_PACKAGE = "app_time_package";
    private static final String APP_TIME_COLUMN_NAME = "app_time_name";
    private static final String APP_TIME_COLUMN_TIME = "app_time_time";

    private static final String BLOCKED_APP_LIST = "blocked_app_list";
    private static final String BLOCKED_APP_NAME = "blocked_app_name";

    private static final String TASKS_TABLE_NAME = "tasks";
    private static final String COL_ID = "ID";
    private static final String COLUMN_GOAL_TYPE = "GoalType";
    private static final String COLUMN_TIME = "Time";
    private static final String COLUMN_APP_NAME = "AppName";

    private static final String APP_UNBLOCK_TIME_USAGE = "app_unblock_time_usage";
    private static final String APP_UNBLOCK_TIME_COLUMN_DATE = "app_unblock_time_date";
    private static final String APP_UNBLOCK_TIME_COLUMN_PACKAGE = "app_unblock_time_package";
    private static final String APP_UNBLOCK_TIME_COLUMN_NAME = "app_unblock_time_name";
    private static final String APP_UNBLOCK_TIME_COLUMN_TIME = "app_unblock_time_time";

    private static final String APP_HISTORY_USAGE = "app_history_usage";
    private static final String APP_HISTORY_DATE = "app_history_date";
    private static final String APP_HISTORY_PACKAGE = "app_history_package";
    private static final String APP_HISTORY_NAME = "app_history_name";
    private static final String APP_HISTORY_OPEN = "app_history_open";
    private static final String APP_HISTORY_CLOSE = "app_history_close";

    private static final String DATABASE_CREATE_USAGE_STATS = "create table "
            + TABLE_USAGE_STATS + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_DATE
            + " text not null, " + COLUMN_USAGE_TIME + " integer);";

    private static final String DATABASE_CREATE_UNLOCK_COUNTS = "create table "
            + TABLE_UNLOCK_COUNTS + "(" + COLUMN_UNLOCK_COUNT_ID
            + " integer primary key autoincrement, " + COLUMN_UNLOCK_COUNT_DATE
            + " text not null, " + COLUMN_UNLOCK_COUNT + " integer);";

    private static final String DATABASE_CREATE_APP_SWITCHES = "CREATE TABLE "
            + TABLE_APP_SWITCHES + "(" + SWOTCH_COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_PACKAGE_NAME + " TEXT NOT NULL UNIQUE, "
            + COLUMN_SWITCH_POSITION + " INTEGER NOT NULL);";

    private static final String DATABASE_CREATE_CATEGORY_USAGE = "CREATE TABLE " + TABLE_CATEGORY_USAGE + " (" +
            CATEGORY_COLUMN_DATE + " TEXT, " +
            TABLE_COLUMN_CATEGORY + " INTEGER, " +
            CATEGORY_COLUMN_VALUE + " TEXT);";

    private static final String DATABASE_CREATE_APP_TIME_USAGE = "CREATE TABLE "
            + APP_TIME_USAGE + "("
            + APP_TIME_COLUMN_DATE + " TEXT NOT NULL, "
            + APP_TIME_COLUMN_PACKAGE + " TEXT NOT NULL, "
            + APP_TIME_COLUMN_NAME + " TEXT NOT NULL, "
            + APP_TIME_COLUMN_TIME + " INTEGER NOT NULL);";

    private static final String DATABASE_BLOCKED_APP_LIST = "CREATE TABLE "
            + BLOCKED_APP_LIST + "(" + BLOCKED_APP_NAME
            + " TEXT NOT NULL)";

    private static final String DATABASE_CREATE_NEW_TASKS = "CREATE TABLE "
            + TASKS_TABLE_NAME + "("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_GOAL_TYPE + " TEXT, "
            + COLUMN_TIME + " TEXT, "
            + COLUMN_APP_NAME + " TEXT);";

    private static final String DATABASE_CREATE_APP_UNBLOCK_TIME_USAGE = "CREATE TABLE "
            + APP_UNBLOCK_TIME_USAGE + "("
            + APP_UNBLOCK_TIME_COLUMN_DATE + " TEXT NOT NULL, "
            + APP_UNBLOCK_TIME_COLUMN_PACKAGE + " TEXT NOT NULL, "
            + APP_UNBLOCK_TIME_COLUMN_NAME + " TEXT NOT NULL, "
            + APP_UNBLOCK_TIME_COLUMN_TIME + " INTEGER NOT NULL);";

    private static final String DATABASE_CREATE_APP_HISTORY_USAGE = "CREATE TABLE "
            + APP_HISTORY_USAGE + "("
            + APP_HISTORY_DATE + " TEXT NOT NULL, "
            + APP_HISTORY_PACKAGE + " TEXT NOT NULL, "
            + APP_HISTORY_NAME + " TEXT NOT NULL, "
            + APP_HISTORY_OPEN + " INTEGER NOT NULL, "
            + APP_HISTORY_CLOSE + " INTEGER NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE_USAGE_STATS);
        database.execSQL(DATABASE_CREATE_UNLOCK_COUNTS);
        database.execSQL(DATABASE_CREATE_APP_SWITCHES);
        database.execSQL(DATABASE_CREATE_CATEGORY_USAGE);
        database.execSQL(DATABASE_CREATE_APP_TIME_USAGE);
        database.execSQL(DATABASE_BLOCKED_APP_LIST);
        database.execSQL(DATABASE_CREATE_NEW_TASKS);
        database.execSQL(DATABASE_CREATE_APP_UNBLOCK_TIME_USAGE);
        database.execSQL(DATABASE_CREATE_APP_HISTORY_USAGE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        // Handle database upgrades
    }

    public List<String> getBlockedAppNames() {
        List<String> blockedAppNames = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(BLOCKED_APP_LIST, new String[]{BLOCKED_APP_NAME}, null, null, null, null, null);

        if (cursor != null) {
            int appNameIndex = cursor.getColumnIndex(BLOCKED_APP_NAME);

            while (cursor.moveToNext()) {
                if (appNameIndex != -1) {
                    String appName = cursor.getString(appNameIndex);
                    blockedAppNames.add(appName);
                } else {
                    // log an error or handle column index is -1
                }
            }
            cursor.close();
        }
        db.close();

        return blockedAppNames;
    }

    public boolean insertTask(String goalType, String time, String appName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_GOAL_TYPE, goalType);
        contentValues.put(COLUMN_TIME, time);
        contentValues.put(COLUMN_APP_NAME, appName);
        long result = db.insert(TASKS_TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public void insertAppUsageData(String date, String packageName, String appName, long openTimestamp, long closeTimestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(APP_HISTORY_USAGE, null,
                APP_HISTORY_PACKAGE + " = ? AND " +
                        APP_HISTORY_NAME + " = ? AND " +
                        APP_HISTORY_OPEN + " = ? AND " +
                        APP_HISTORY_CLOSE + " = ?",
                new String[]{packageName, appName, String.valueOf(openTimestamp), String.valueOf(closeTimestamp)},
                null, null,null, null);

        if (cursor != null && cursor.getCount() > 0) {
            // Entry already exists, do nothing
            cursor.close();
            db.close();
            return;
        }

        // Entry doesn't exist, insert the new entry
        ContentValues values = new ContentValues();
        values.put(APP_HISTORY_DATE, date);
        values.put(APP_HISTORY_PACKAGE, packageName);
        values.put(APP_HISTORY_NAME, appName);
        values.put(APP_HISTORY_OPEN, openTimestamp);
        values.put(APP_HISTORY_CLOSE, closeTimestamp);
        db.insert(APP_HISTORY_USAGE, null, values);

        // Close cursor and database
        if (cursor != null) {
            cursor.close();
        }
        db.close();
    }

    public List<AppHistoryData> getAppHistoryForDate(Calendar selectedDate, PackageManager packageManager) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = dateFormat.format(selectedDate.getTime());

        List<AppHistoryData> appUsageList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                APP_HISTORY_DATE,
                APP_HISTORY_PACKAGE,
                APP_HISTORY_OPEN,
                APP_HISTORY_CLOSE
        };

        String selection = APP_HISTORY_DATE + " = ?";
        String[] selectionArgs = {formattedDate};

        String sortOrder = APP_HISTORY_OPEN + " DESC";

        Cursor cursor = db.query(
                APP_HISTORY_USAGE,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String packageName = cursor.getString(cursor.getColumnIndexOrThrow(APP_HISTORY_PACKAGE));
                long openTimestamp = cursor.getLong(cursor.getColumnIndexOrThrow(APP_HISTORY_OPEN));
                long closeTimestamp = cursor.getLong(cursor.getColumnIndexOrThrow(APP_HISTORY_CLOSE));

                String usageOpened = formatDate(openTimestamp);
                String usageClosed = formatDate(closeTimestamp);

                String appName = getAppName(packageManager, packageName);

                AppHistoryData appHistoryData = new AppHistoryData(packageName, appName, usageOpened, usageClosed);
                appUsageList.add(appHistoryData);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return appUsageList;
    }

    private String getAppName(PackageManager packageManager, String packageName) {
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
            return (String) packageManager.getApplicationLabel(applicationInfo);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageName; // Return package name if app name is not found
    }

    private String formatDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase(); // Get a reference to the writable database
        Cursor cursor = db.rawQuery("SELECT * FROM " + TASKS_TABLE_NAME, null);
        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(COL_ID);
            int typeIndex = cursor.getColumnIndex(COLUMN_GOAL_TYPE);
            int timeIndex = cursor.getColumnIndex(COLUMN_TIME);
            int appNameIndex = cursor.getColumnIndex(COLUMN_APP_NAME);
            if (idIndex == -1 || typeIndex == -1 || timeIndex == -1 || appNameIndex == -1) {
                Log.e("DatabaseHelper", "One or more column index is -1");
            } else {
                do {
                    int id = cursor.getInt(idIndex);
                    String type = cursor.getString(typeIndex);
                    String time = cursor.getString(timeIndex);
                    String appName = cursor.getString(appNameIndex);
                    taskList.add(new Task(id, type, time, appName));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return taskList;
    }

    public boolean taskExists(String goalType) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM " + TASKS_TABLE_NAME + " WHERE " + COLUMN_GOAL_TYPE + "=? AND (" +
                        COLUMN_GOAL_TYPE + "=? OR " + COLUMN_GOAL_TYPE + "=?)",
                new String[]{goalType, "Telefono naudojimo laikas", "Telefono atrakinimo skaičius"});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    public boolean taskExists2(String goalType, String appName) {
        if (goalType.equals("Programėlės naudojimo laikas") || goalType.equals("Programėlės įjungimo skaičius")) {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT 1 FROM " + TASKS_TABLE_NAME + " WHERE " + COLUMN_GOAL_TYPE + "=? AND " + COLUMN_APP_NAME + "=?",
                    new String[]{goalType, appName});
            boolean exists = cursor.moveToFirst();
            cursor.close();
            return exists;
        } else {
            return false;
        }
    }

    public boolean deleteTask(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TASKS_TABLE_NAME, COL_ID + " = ?", new String[]{String.valueOf(taskId)});
        db.close();
        return rowsAffected > 0;
    }

    public void saveCheckedApps(List<String> checkedAppNames) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(BLOCKED_APP_LIST, null, null);

            for (String appName : checkedAppNames) {
                ContentValues values = new ContentValues();
                values.put(BLOCKED_APP_NAME, appName);
                db.insert(BLOCKED_APP_LIST, null, values);
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void addAppUsage(String packageName, String appName, long usageTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String currentDate = getCurrentDate();

        Cursor cursor = db.query(APP_TIME_USAGE, null,
                APP_TIME_COLUMN_DATE + "=? AND " + APP_TIME_COLUMN_PACKAGE + "=?",
                new String[]{currentDate, packageName}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            ContentValues updateValues = new ContentValues();
            updateValues.put(APP_TIME_COLUMN_TIME, usageTime);
            db.update(APP_TIME_USAGE, updateValues,
                    APP_TIME_COLUMN_DATE + "=? AND " + APP_TIME_COLUMN_PACKAGE + "=?",
                    new String[]{currentDate, packageName});
        } else {
            values.put(APP_TIME_COLUMN_DATE, currentDate);
            values.put(APP_TIME_COLUMN_PACKAGE, packageName);
            values.put(APP_TIME_COLUMN_NAME, appName);
            values.put(APP_TIME_COLUMN_TIME, usageTime);
            db.insert(APP_TIME_USAGE, null, values);
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
    }

    public void addUnblockAppUsage(String packageName, String appName, int launchCount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String currentDate = getCurrentDate();

        Cursor cursor = db.query(APP_UNBLOCK_TIME_USAGE, null,
                APP_UNBLOCK_TIME_COLUMN_DATE + "=? AND " + APP_UNBLOCK_TIME_COLUMN_PACKAGE + "=?",
                new String[]{currentDate, packageName}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            ContentValues updateValues = new ContentValues();
            updateValues.put(APP_UNBLOCK_TIME_COLUMN_TIME, launchCount);
            db.update(APP_UNBLOCK_TIME_USAGE, updateValues,
                    APP_UNBLOCK_TIME_COLUMN_DATE + "=? AND " + APP_UNBLOCK_TIME_COLUMN_PACKAGE + "=?",
                    new String[]{currentDate, packageName});
        } else {
            values.put(APP_UNBLOCK_TIME_COLUMN_DATE, currentDate);
            values.put(APP_UNBLOCK_TIME_COLUMN_PACKAGE, packageName);
            values.put(APP_UNBLOCK_TIME_COLUMN_NAME, appName);
            values.put(APP_UNBLOCK_TIME_COLUMN_TIME, launchCount);
            db.insert(APP_UNBLOCK_TIME_USAGE, null, values);
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
    }

    public List<Dashboard.AppUsageUnlockInfo> getMostUnlockedAppsForDate(Calendar selectedDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = dateFormat.format(selectedDate.getTime());

        List<Dashboard.AppUsageUnlockInfo> appUsageList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT * FROM " + APP_UNBLOCK_TIME_USAGE +
                " WHERE " + APP_UNBLOCK_TIME_COLUMN_DATE + " = ?" +
                " ORDER BY " + APP_UNBLOCK_TIME_COLUMN_TIME + " DESC";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{formattedDate});

        if (cursor != null && cursor.moveToFirst()) {
            int packageNameIndex = cursor.getColumnIndexOrThrow(APP_UNBLOCK_TIME_COLUMN_PACKAGE);
            int appNameIndex = cursor.getColumnIndexOrThrow(APP_UNBLOCK_TIME_COLUMN_NAME);
            int usageTimeIndex = cursor.getColumnIndexOrThrow(APP_UNBLOCK_TIME_COLUMN_TIME);

            do {
                String packageName = cursor.getString(packageNameIndex);
                String appName = cursor.getString(appNameIndex);
                long usageTime = cursor.getLong(usageTimeIndex);

                Dashboard.AppUsageUnlockInfo appUsageInfo = new Dashboard.AppUsageUnlockInfo();
                appUsageInfo.setPackageName(packageName);
                appUsageInfo.setAppName(appName);
                appUsageInfo.setLaunchCount((int) usageTime);
                appUsageList.add(appUsageInfo);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return appUsageList;
    }

    public List<Dashboard.AppUsageInfo> getMostUsedAppsForDate(Calendar selectedDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = dateFormat.format(selectedDate.getTime());

        List<Dashboard.AppUsageInfo> appUsageList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT * FROM " + APP_TIME_USAGE +
                " WHERE " + APP_TIME_COLUMN_DATE + " = ?" +
                " ORDER BY " + APP_TIME_COLUMN_TIME + " DESC";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{formattedDate});

        if (cursor != null && cursor.moveToFirst()) {
            int packageNameIndex = cursor.getColumnIndexOrThrow(APP_TIME_COLUMN_PACKAGE);
            int appNameIndex = cursor.getColumnIndexOrThrow(APP_TIME_COLUMN_NAME);
            int usageTimeIndex = cursor.getColumnIndexOrThrow(APP_TIME_COLUMN_TIME);

            do {
                String packageName = cursor.getString(packageNameIndex);
                String appName = cursor.getString(appNameIndex);
                long usageTime = cursor.getLong(usageTimeIndex);

                Dashboard.AppUsageInfo appUsageInfo = new Dashboard.AppUsageInfo();
                appUsageInfo.setPackageName(packageName);
                appUsageInfo.setAppName(appName);
                appUsageInfo.setUsageTime(usageTime);
                appUsageList.add(appUsageInfo);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return appUsageList;
    }

    public List<Dashboard.AppUsageInfo> getMostUsedApps(String adate, int limit) {
        List<Dashboard.AppUsageInfo> appUsageList = new ArrayList<>();

        // Construct the query with a WHERE clause to filter by date
        String selectQuery = "SELECT * FROM " + APP_TIME_USAGE +
                " WHERE " + APP_TIME_COLUMN_DATE + " = ?" +   // Filter by date
                " ORDER BY " + APP_TIME_COLUMN_TIME + " DESC LIMIT " + limit;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{adate});

        if (cursor != null && cursor.moveToFirst()) {
            int packageNameIndex = cursor.getColumnIndexOrThrow(APP_TIME_COLUMN_PACKAGE);
            int appNameIndex = cursor.getColumnIndexOrThrow(APP_TIME_COLUMN_NAME);
            int usageTimeIndex = cursor.getColumnIndexOrThrow(APP_TIME_COLUMN_TIME);

            do {
                String packageName = cursor.getString(packageNameIndex);
                String appName = cursor.getString(appNameIndex);
                long usageTime = cursor.getLong(usageTimeIndex);

                Dashboard.AppUsageInfo appUsageInfo = new Dashboard.AppUsageInfo();
                appUsageInfo.setPackageName(packageName);
                appUsageInfo.setAppName(appName);
                appUsageInfo.setUsageTime(usageTime);
                appUsageList.add(appUsageInfo);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return appUsageList;
    }

    public List<CategoryApps.CategoryAppsData> getCategoryAppsForDate(Calendar selectedDate) {
        List<CategoryApps.CategoryAppsData> categoryAppsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = dateFormat.format(selectedDate.getTime());

        String[] projection = {CATEGORY_COLUMN_DATE, TABLE_COLUMN_CATEGORY, CATEGORY_COLUMN_VALUE};
        String selection = CATEGORY_COLUMN_DATE + " = ?";
        String[] selectionArgs = {formattedDate};
        Cursor cursor = db.query(TABLE_CATEGORY_USAGE, projection, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int dateColumnIndex = cursor.getColumnIndex(CATEGORY_COLUMN_DATE);
                if (dateColumnIndex == -1) {
                    Log.e("Error", "Column CATEGORY_COLUMN_DATE not found");
                }
                int categoryColumnIndex = cursor.getColumnIndex(TABLE_COLUMN_CATEGORY);
                if (categoryColumnIndex == -1) {
                    Log.e("Error", "Column TABLE_COLUMN_CATEGORY not found");
                }
                int valueColumnIndex = cursor.getColumnIndex(CATEGORY_COLUMN_VALUE);
                if (valueColumnIndex == -1) {
                    Log.e("Error", "Column CATEGORY_COLUMN_VALUE not found");
                }
                if (dateColumnIndex != -1 && categoryColumnIndex != -1 && valueColumnIndex != -1) {
                    String date = cursor.getString(dateColumnIndex);
                    String category = cursor.getString(categoryColumnIndex);
                    long value = cursor.getLong(valueColumnIndex);
                    categoryAppsList.add(new CategoryApps.CategoryAppsData(date, category, value));
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return categoryAppsList;
    }

    public void removeDuplicateEntries() {
        SQLiteDatabase database = getWritableDatabase();

        try {
            String selectQuery = "SELECT " + COLUMN_DATE + ", MIN(" + COLUMN_ID + ") as minId " +
                    "FROM " + TABLE_USAGE_STATS +
                    " GROUP BY " + COLUMN_DATE +
                    " HAVING COUNT(*) > 1";

            Cursor cursor = database.rawQuery(selectQuery, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE));
                    int minId = cursor.getInt(cursor.getColumnIndexOrThrow("minId"));

                    database.delete(TABLE_USAGE_STATS, COLUMN_DATE + " = ? AND " + COLUMN_ID + " <> ?",
                            new String[]{date, String.valueOf(minId)});
                } while (cursor.moveToNext());
            }
        } finally {
            if (database != null) {
                database.close();
            }
        }
    }

    public long getUsageTimeForInterval(long startTime, long endTime) {
        SQLiteDatabase db = this.getReadableDatabase();

        String startTimeStr = convertTimestampToString(startTime);
        String endTimeStr = convertTimestampToString(endTime);

        long totalUsageTime = 0;

        String[] projection = {COLUMN_USAGE_TIME};

        String selection = COLUMN_DATE + " BETWEEN ? AND ?";
        String[] selectionArgs = {startTimeStr, endTimeStr};

        Log.d("UsageStatsDebug", "Selection Criteria: " + selection + ", Args: " + Arrays.toString(selectionArgs));

        Cursor cursor = db.query(TABLE_USAGE_STATS, projection, selection, selectionArgs, null, null, null);

        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    totalUsageTime += cursor.getLong(0);
                }
            } finally {
                cursor.close();
            }
        }

        Log.d("UsageStatsDebug", "Total Usage Time for Interval: " + totalUsageTime);

        return totalUsageTime;
    }



    public long getUnblockForInterval(long startTime, long endTime) {
        SQLiteDatabase db = this.getReadableDatabase();

        String startTimeStr = convertTimestampToString(startTime);
        String endTimeStr = convertTimestampToString(endTime);

        long totalUsageTime = 0;

        String[] projection = {COLUMN_UNLOCK_COUNT};

        String selection = COLUMN_UNLOCK_COUNT_DATE + " BETWEEN ? AND ?";
        String[] selectionArgs = {startTimeStr, endTimeStr};

        Log.d("UsageStatsDebug", "Selection Criteria: " + selection + ", Args: " + Arrays.toString(selectionArgs));

        Cursor cursor = db.query(TABLE_UNLOCK_COUNTS, projection, selection, selectionArgs, null, null, null);

        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    totalUsageTime += cursor.getLong(0);
                }
            } finally {
                cursor.close();
            }
        }

        Log.d("UsageStatsDebug", "Total Usage Time for Interval: " + totalUsageTime);

        return totalUsageTime;
    }

    public void insertPieChartData(String category, long value) {
        SQLiteDatabase db = this.getWritableDatabase();

        String currentDate = getCurrentDate();

        String updateQuery = "UPDATE " + TABLE_CATEGORY_USAGE + " SET " +
                CATEGORY_COLUMN_VALUE + " = " + value +
                " WHERE " + TABLE_COLUMN_CATEGORY + " = '" + category + "'" +
                " AND " + CATEGORY_COLUMN_DATE + " = '" + currentDate + "';";
        db.execSQL(updateQuery);

        if (DatabaseUtils.queryNumEntries(db, TABLE_CATEGORY_USAGE,
                TABLE_COLUMN_CATEGORY + " = ? AND " + CATEGORY_COLUMN_DATE + " = ?",
                new String[]{category, currentDate}) == 0) {
            String insertQuery = "INSERT INTO " + TABLE_CATEGORY_USAGE + " (" +
                    TABLE_COLUMN_CATEGORY + ", " +
                    CATEGORY_COLUMN_VALUE + ", " +
                    CATEGORY_COLUMN_DATE + ") VALUES ('" +
                    category + "', " +
                    value + ", '" +
                    currentDate + "');";
            db.execSQL(insertQuery);
        }

        db.close();
    }

    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }

    private String convertTimestampToString(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    public void storeAppSwitchInfo(String packageName, boolean switchPosition) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_PACKAGE_NAME, packageName);
        values.put(COLUMN_SWITCH_POSITION, switchPosition ? 1 : 0);

        try {
            database.insertWithOnConflict(TABLE_APP_SWITCHES, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        } finally {
            if (database != null) {
                database.close();
            }
        }
    }

    public Cursor getAllAppSwitchInfo() {
        SQLiteDatabase database = getReadableDatabase();
        String[] columns = {COLUMN_ID, COLUMN_PACKAGE_NAME, COLUMN_SWITCH_POSITION};

        return database.query(TABLE_APP_SWITCHES, columns, null, null, null, null, null);
    }
}
