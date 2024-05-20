package com.example.text3;

import android.app.PendingIntent;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Dashboard extends Fragment {

    private static final int USAGE_ACCESS_REQUEST_CODE = 123;
    private static final String PREFS_NAME = "UnlockPrefs";
    private static final String UNLOCK_COUNT_KEY = "UnlockCount";
    private static final String TAG = "UsageStatsSample";
    private boolean logOpenCloseEvents = false;
    private String lastPackageName = "";
    private long lastOpenTimestamp = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_dashboard, container, false);
    }

    private void startAppBlockerService() {
        Intent serviceIntent = new Intent(getActivity(), AppBlockerService.class);
        getActivity().startService(serviceIntent);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!isUsageAccessGranted()) {
            requestUsageAccess();
        } else {
            updateUsageTime(view);
            updateUnlockCountAndDisplayWithoutIncrement(view);
        }

        retrieveUsageStats(requireContext());

        retrieveCategoryUsageStats(requireContext());

        UsageDataScheduler.scheduleUsageData(requireContext(), PendingIntent.FLAG_IMMUTABLE);

        startAppBlockerService();

        Button yellowButton = view.findViewById(R.id.yellowButton);
        Button blackButton = view.findViewById(R.id.blackButton);

        yellowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.contentFrame, new StatisticsScreen())
                        .commit();

                selectMenuItem(R.id.menu_button2);
            }
        });

        blackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.contentFrame, new BlockScreen())
                        .commit();

                selectMenuItem(R.id.menu_button3);
            }
        });

        Button mostUsedButton = view.findViewById(R.id.mostUsedButton);
        mostUsedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MostUsedApps.class);
                startActivity(intent);
            }
        });

        Button mostopenedButton = view.findViewById(R.id.mostOpenedButton);
        mostopenedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MostOpenedApps.class);
                startActivity(intent);
            }
        });

    }

    private void selectMenuItem(@IdRes int menuItemId) {
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.findItem(menuItemId);
        menuItem.setChecked(true);
    }

    private void updateUsageTime(View view) {
        Context context = requireContext();

        TextView timeUsageNumber = view.findViewById(R.id.timeUsageNumber);

        ImageView firstappimage1 = view.findViewById(R.id.image1);
        TextView firstapptext1 = view.findViewById(R.id.text1);
        TextView firstapptext2 = view.findViewById(R.id.text2);

        ImageView secondappimage1 = view.findViewById(R.id.image2);
        TextView secondapptext1 = view.findViewById(R.id.text3);
        TextView secondapptext2 = view.findViewById(R.id.text4);

        ImageView thirdappimage1 = view.findViewById(R.id.image3);
        TextView thirdapptext1 = view.findViewById(R.id.text5);
        TextView thirdapptext2 = view.findViewById(R.id.text6);

        ImageView fourthappimage1 = view.findViewById(R.id.image4);
        TextView fourthapptext1 = view.findViewById(R.id.text7);
        TextView fourthapptext2 = view.findViewById(R.id.text8);

        ImageView fifthappimage1 = view.findViewById(R.id.image5);
        TextView fifthapptext1 = view.findViewById(R.id.text9);
        TextView fifthapptext2 = view.findViewById(R.id.text10);

        ImageView firstappimage2 = view.findViewById(R.id.image6);
        TextView firstapptext3 = view.findViewById(R.id.text11);
        TextView firstapptext4 = view.findViewById(R.id.text12);

        ImageView secondappimage2 = view.findViewById(R.id.image7);
        TextView secondapptext3 = view.findViewById(R.id.text13);
        TextView secondapptext4 = view.findViewById(R.id.text14);

        ImageView thirdappimage2 = view.findViewById(R.id.image8);
        TextView thirdapptext3 = view.findViewById(R.id.text15);
        TextView thirdapptext4 = view.findViewById(R.id.text16);

        ImageView fourthappimage2 = view.findViewById(R.id.image9);
        TextView fourthapptext3 = view.findViewById(R.id.text17);
        TextView fourthapptext4 = view.findViewById(R.id.text18);

        ImageView fifthappimage2 = view.findViewById(R.id.image10);
        TextView fifthapptext3 = view.findViewById(R.id.text19);
        TextView fifthapptext4 = view.findViewById(R.id.text20);

        long dailyUsageTime = getDailyScreenOnTime();
        int unblockScreenNumber = getUnlockCount();

        String currentDate = getCurrentDate();

        insertUsageTime(currentDate, dailyUsageTime);
        insertUnblockNumber(currentDate, unblockScreenNumber);

        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        dbHelper.removeDuplicateEntries();

        List<BarEntry> barEntries = getDatabaseEntries();
        List<BarEntry> barEntries2 = getDatabaseEntries2();

        BarChart barChart = view.findViewById(R.id.barGraph);
        BarChart barChart2 = view.findViewById(R.id.barGraph2);

        barChart.setScaleEnabled(false);
        barChart2.setScaleEnabled(false);
        barChart.setPinchZoom(false);
        barChart2.setPinchZoom(false);
        BarDataSet barDataSet = new BarDataSet(barEntries, "Usage Time");
        BarDataSet barDataSet2 = new BarDataSet(barEntries2, "Unlock Time");
        barDataSet.setColor(ContextCompat.getColor(context, R.color.yellow));
        barDataSet2.setColor(ContextCompat.getColor(context, R.color.yellow));
        barDataSet.setValueTextColor(Color.WHITE);
        barDataSet2.setValueTextColor(Color.WHITE);

        barDataSet2.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value);
            }
        });

        BarData barData = new BarData(barDataSet);
        BarData barData2 = new BarData(barDataSet2);
        barData.setValueFormatter(new BarValueFormatter());
        barChart.setData(barData);
        barChart2.setData(barData2);

        XAxis xAxis = barChart.getXAxis();
        XAxis xAxis2 = barChart2.getXAxis();
        List<String> dateLabels = getDatabaseDates();
        List<String> dateLabels2 = getDatabaseDates2();
        Collections.reverse(dateLabels);
        Collections.reverse(dateLabels2);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dateLabels));
        xAxis2.setValueFormatter(new IndexAxisValueFormatter(dateLabels2));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis2.setGranularity(1f);
        xAxis.setLabelRotationAngle(45);
        xAxis2.setLabelRotationAngle(45);
        xAxis.setTextColor(Color.WHITE);
        xAxis2.setTextColor(Color.WHITE);
        xAxis.setDrawGridLines(false);
        xAxis2.setDrawGridLines(false);

        YAxis yAxisLeft = barChart.getAxisLeft();
        YAxis yAxisLeft2 = barChart2.getAxisLeft();
        yAxisLeft.setTextColor(Color.WHITE);
        yAxisLeft2.setTextColor(Color.WHITE);
        yAxisLeft.setValueFormatter(new YAxisValueFormatter());

        YAxis yAxisRight = barChart.getAxisRight();
        YAxis yAxisRight2 = barChart2.getAxisRight();
        yAxisRight.setTextColor(Color.WHITE);
        yAxisRight2.setTextColor(Color.WHITE);
        yAxisRight.setValueFormatter(new YAxisValueFormatter());

        Legend legend = barChart.getLegend();
        Legend legend2 = barChart2.getLegend();
        legend.setTextColor(Color.WHITE);
        legend2.setTextColor(Color.WHITE);

        barChart.getDescription().setEnabled(false);
        barChart2.getDescription().setEnabled(false);
        barChart.invalidate();
        barChart2.invalidate();

        Spinner optionStatsSpinner = view.findViewById(R.id.optionStats);

        optionStatsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position == 0) {
                    barChart.setVisibility(View.VISIBLE);
                    barChart2.setVisibility(View.GONE);
                } else if (position == 1) {
                    barChart.setVisibility(View.GONE);
                    barChart2.setVisibility(View.VISIBLE);
                } else {
                    barChart.setVisibility(View.GONE);
                    barChart2.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Continue your implementation here
            }
        });

        timeUsageNumber.setText(formatMillis(dailyUsageTime));

        getData();

        List<AppUsageInfo> appUsageList = getMostUsedApps();
        List<AppUsageUnlockInfo> appUsageUnlockInfo = getMostUsedUnlockedApps();

        if (!appUsageList.isEmpty()) {
            displayAppInfo(firstapptext1, firstapptext2, firstappimage1, appUsageList.get(0));
        }

        if (appUsageList.size() > 1) {
            displayAppInfo(secondapptext1, secondapptext2, secondappimage1, appUsageList.get(1));
        }

        if (appUsageList.size() > 2) {
            displayAppInfo(thirdapptext1, thirdapptext2, thirdappimage1, appUsageList.get(2));
        }

        if (appUsageList.size() > 3) {
            displayAppInfo(fourthapptext1, fourthapptext2, fourthappimage1, appUsageList.get(3));
        }

        if (appUsageList.size() > 4) {
            displayAppInfo(fifthapptext1, fifthapptext2, fifthappimage1, appUsageList.get(4));
        }

        if (!appUsageUnlockInfo.isEmpty()) {
            displayUnlockAppInfo(firstapptext3, firstapptext4, firstappimage2, appUsageUnlockInfo.get(0));
        }

        if (appUsageUnlockInfo.size() > 1) {
            displayUnlockAppInfo(secondapptext3, secondapptext4, secondappimage2, appUsageUnlockInfo.get(1));
        }

        if (appUsageUnlockInfo.size() > 2) {
            displayUnlockAppInfo(thirdapptext3, thirdapptext4, thirdappimage2, appUsageUnlockInfo.get(2));
        }

        if (appUsageUnlockInfo.size() > 3) {
            displayUnlockAppInfo(fourthapptext3, fourthapptext4, fourthappimage2, appUsageUnlockInfo.get(3));
        }

        if (appUsageUnlockInfo.size() > 4) {
            displayUnlockAppInfo(fifthapptext3, fifthapptext4, fifthappimage2, appUsageUnlockInfo.get(4));
        }
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

    private void displayAppInfo(TextView text1, TextView text2, ImageView imageView, AppUsageInfo appUsageInfo) {
        text1.setText(appUsageInfo.getAppName());
        text2.setText(formatMillis(appUsageInfo.getUsageTime()));
        setAppIcon(imageView, appUsageInfo.getPackageName());
    }

    private void displayUnlockAppInfo(TextView text1, TextView text2, ImageView imageView, AppUsageUnlockInfo appUsageUnlockInfo) {
        text1.setText(appUsageUnlockInfo.getAppName());
        text2.setText(String.valueOf(appUsageUnlockInfo.getLaunchCount()));
        setAppIcon(imageView, appUsageUnlockInfo.getPackageName());
    }

    private List<BarEntry> getDatabaseEntries2() {
        List<BarEntry> entries = new ArrayList<>();

        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
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

    private List<String> getDatabaseDates2() {
        List<String> dates = new ArrayList<>();

        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
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

    private List<String> getTimeIntervals() {
        List<String> timeIntervals = new ArrayList<>();
        timeIntervals.add("00:00-01:00");
        timeIntervals.add("01:01-02:00");
        timeIntervals.add("02:01-03:00");
        timeIntervals.add("03:01-04:00");
        timeIntervals.add("04:01-05:00");
        timeIntervals.add("05:01-06:00");
        timeIntervals.add("06:01-07:00");
        timeIntervals.add("07:01-08:00");
        timeIntervals.add("08:01-09:00");
        timeIntervals.add("09:01-10:00");
        timeIntervals.add("10:01-11:00");
        timeIntervals.add("11:01-12:00");
        timeIntervals.add("12:01-13:00");
        timeIntervals.add("13:01-14:00");
        timeIntervals.add("14:01-15:00");
        timeIntervals.add("15:01-16:00");
        timeIntervals.add("16:01-17:00");
        timeIntervals.add("17:01-18:00");
        timeIntervals.add("08:01-19:00");
        timeIntervals.add("19:01-20:00");
        timeIntervals.add("20:01-21:00");
        timeIntervals.add("21:01-22:00");
        timeIntervals.add("22:01-23:00");
        timeIntervals.add("23:01-23:59");
        return timeIntervals;
    }

    private void setAppIcon(ImageView imageView, String packageName) {
        try {
            PackageManager packageManager = requireContext().getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
            imageView.setImageDrawable(packageManager.getApplicationIcon(applicationInfo));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            imageView.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    private List<AppUsageUnlockInfo> getMostUsedUnlockedApps() {
        UsageStatsManager usageStatsManager = (UsageStatsManager) requireActivity().getSystemService(Context.USAGE_STATS_SERVICE);
        long startOfDay = getStartOfDay();
        long endOfDay = System.currentTimeMillis();

        UsageEvents usageEvents = usageStatsManager.queryEvents(startOfDay, endOfDay);
        SparseArray<AppUsageUnlockInfo> appUsageUnlockInfoMap = new SparseArray<>();

        while (usageEvents.hasNextEvent()) {
            UsageEvents.Event event = new UsageEvents.Event();
            usageEvents.getNextEvent(event);

            if (event.getEventType() == UsageEvents.Event.ACTIVITY_RESUMED) {
                String packageName = event.getPackageName();
                AppUsageUnlockInfo appUsageUnlockInfo = appUsageUnlockInfoMap.get(packageName.hashCode());

                if (appUsageUnlockInfo == null) {
                    appUsageUnlockInfo = new AppUsageUnlockInfo();
                    appUsageUnlockInfo.setPackageName(packageName);
                    appUsageUnlockInfo.setAppName(getAppNameFromPackage(requireContext(), packageName));
                    appUsageUnlockInfo.setLaunchCount(1); // Initialize to 1
                    appUsageUnlockInfoMap.put(packageName.hashCode(), appUsageUnlockInfo);
                } else {
                    appUsageUnlockInfo.setLaunchCount(appUsageUnlockInfo.getLaunchCount() + 1);
                }
            }
        }

        DatabaseHelper databaseHelper = new DatabaseHelper(requireContext());
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        for (int i = 0; i < appUsageUnlockInfoMap.size(); i++) {
            AppUsageUnlockInfo appUsageUnlockInfo = appUsageUnlockInfoMap.valueAt(i);
            databaseHelper.addUnblockAppUsage(appUsageUnlockInfo.getPackageName(), appUsageUnlockInfo.getAppName(), appUsageUnlockInfo.getLaunchCount());
        }
        db.close();

        List<AppUsageUnlockInfo> appUsageUnlockInfoList = new ArrayList<>();
        for (int i = 0; i < appUsageUnlockInfoMap.size(); i++) {
            appUsageUnlockInfoList.add(appUsageUnlockInfoMap.valueAt(i));
        }

        Collections.sort(appUsageUnlockInfoList, (app1, app2) -> Integer.compare(app2.getLaunchCount(), app1.getLaunchCount()));
        if (appUsageUnlockInfoList.size() > 5) {
            appUsageUnlockInfoList = appUsageUnlockInfoList.subList(0, 5);
        }

        for (AppUsageUnlockInfo appUsageUnlockInfo : appUsageUnlockInfoList) {
            Log.d("AppUsageUnlockList", appUsageUnlockInfo.getAppName() + " - Launch Count: " + appUsageUnlockInfo.getLaunchCount());
        }

        return appUsageUnlockInfoList;
    }

    private List<AppUsageInfo> getMostUsedApps() {
        String currentDate = getCurrentDate();
        UsageStatsManager usageStatsManager = (UsageStatsManager) requireActivity().getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> appUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, getStartOfDay(), System.currentTimeMillis());

        List<AppUsageInfo> appUsageList = new ArrayList<>();

        if (appUsageStats != null) {
            DatabaseHelper databaseHelper = new DatabaseHelper(requireContext());
            SQLiteDatabase db = databaseHelper.getWritableDatabase();

            for (UsageStats usageStats : appUsageStats) {
                if (usageStats.getTotalTimeInForeground() > 0) {
                    long lastTimeUsed = usageStats.getLastTimeUsed();
                    long currentTimeMillis = System.currentTimeMillis();
                    long startOfDay = getStartOfDay();

                    if (lastTimeUsed >= startOfDay && lastTimeUsed <= currentTimeMillis) {
                        AppUsageInfo appUsageInfo = new AppUsageInfo();
                        appUsageInfo.setPackageName(usageStats.getPackageName());
                        appUsageInfo.setAppName(getAppNameFromPackage(requireContext(), usageStats.getPackageName()));
                        appUsageInfo.setUsageTime(usageStats.getTotalTimeInForeground());
                        appUsageList.add(appUsageInfo);

                        databaseHelper.addAppUsage(appUsageInfo.getPackageName(), appUsageInfo.getAppName(), appUsageInfo.getUsageTime());

                        Log.d("AppUsageList", appUsageInfo.getAppName() + " - Usage Time: " + appUsageInfo.getUsageTime() + " - Last Time Used: " + lastTimeUsed);
                    }
                }
            }

            db.close();

            appUsageList = databaseHelper.getMostUsedApps(currentDate, 5);

            for (AppUsageInfo appUsageInfo : appUsageList) {
                Log.d("AppUsageList", appUsageInfo.getAppName() + " - " + appUsageInfo.getUsageTime());
            }
        }

        return appUsageList;
    }

    private long getStartOfDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
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

    public static class AppUsageInfo {
        private String packageName;
        private String appName;
        private long usageTime;

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public long getUsageTime() {
            return usageTime;
        }

        public void setUsageTime(long usageTime) {
            this.usageTime = usageTime;
        }
    }

    public static class AppUsageInfoTime {
        private String appName;
        private long usageTime;
        private Drawable appIcon;

        public AppUsageInfoTime(String appName, long usageTime, Drawable appIcon) {
            this.appName = appName;
            this.usageTime = usageTime;
            this.appIcon = appIcon;
        }

        public String getAppName() {
            return appName;
        }

        public long getUsageTime() {
            return usageTime;
        }

        public Drawable getAppIcon() {
            return appIcon;
        }
    }

    public static class AppUsageUnlockInfo {
        private String packageName;
        private String appName;
        private int launchCount;

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public int getLaunchCount() {
            return launchCount;
        }

        public void setLaunchCount(int launchCount) {
            this.launchCount = launchCount;
        }
    }

    private void updateUnlockCount(View view) {
        TextView deviceUnblokedNumber = view.findViewById(R.id.deviceUnblokedNumber);

        int unlockCount = getUnlockCount();
        deviceUnblokedNumber.setText(String.valueOf(unlockCount));
    }

    private boolean isUsageAccessGranted() {
        UsageStatsManager usageStatsManager = (UsageStatsManager) requireActivity().getSystemService(Context.USAGE_STATS_SERVICE);
        if (usageStatsManager != null) {
            long currentTime = System.currentTimeMillis();
            List<UsageStats> stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, currentTime - 1000 * 3600, currentTime);
            return stats != null && !stats.isEmpty();
        }
        return false;
    }

    private void requestUsageAccess() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivityForResult(intent, USAGE_ACCESS_REQUEST_CODE);
        }
    }
    private BroadcastReceiver unlockReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_USER_PRESENT.equals(intent.getAction())) {
                updateUnlockCountAndDisplayWithoutIncrement(requireView());
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(Intent.ACTION_USER_PRESENT);
        requireContext().registerReceiver(unlockReceiver, filter);

        startForegroundService();
    }

    @Override
    public void onPause() {
        super.onPause();
        requireContext().unregisterReceiver(unlockReceiver);
    }

    private void startForegroundService() {
        Intent serviceIntent = new Intent(requireContext(), UnlockCountService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requireContext().startForegroundService(serviceIntent);
        } else {
            requireContext().startService(serviceIntent);
        }
    }

    private int getUnlockCount() {
        int unlockCount = 0;

        UsageStatsManager mUsageStatsManager = (UsageStatsManager) getActivity().getSystemService(Context.USAGE_STATS_SERVICE);
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
            Toast.makeText(getActivity(), "Atsiprašau...", Toast.LENGTH_SHORT).show();
            return 0;
        }
    }

    private void incrementUnlockCount() {
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int currentCount = prefs.getInt(UNLOCK_COUNT_KEY, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(UNLOCK_COUNT_KEY, currentCount + 1);
        editor.apply();
    }

    private void updateUnlockCountAndDisplay(View view) {
        incrementUnlockCount();
        updateUnlockCount(view);
    }

    private void updateUnlockCountAndDisplayWithoutIncrement(View view) {
        String currentDate = getCurrentDate();

        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String lastSavedDate = prefs.getString("LastSavedDate", "");

        if (!currentDate.equals(lastSavedDate)) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(UNLOCK_COUNT_KEY, 0);
            editor.putString("LastSavedDate", currentDate);
            editor.apply();
        }

        updateUnlockCount(view);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == USAGE_ACCESS_REQUEST_CODE) {
            if (isUsageAccessGranted()) {
                updateUsageTime(requireView());
                updateUnlockCountAndDisplay(requireView());
            } else {
                // The user did not grant the permission. Make a message to user to inform him
            }
            Log.d("YourTag", "onActivityResult: This method is used");
        }
    }

    private List<BarEntry> getDatabaseEntries() {
        List<BarEntry> entries = new ArrayList<>();

        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
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

    private long getDailyScreenOnTime() {
        UsageStatsManager usageStatsManager = (UsageStatsManager) requireActivity().getSystemService(Context.USAGE_STATS_SERVICE);
        if (usageStatsManager != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            long startOfDay = calendar.getTimeInMillis();
            long endOfDay = startOfDay + 24 * 60 * 60 * 1000;

            long currentTime = System.currentTimeMillis();

            List<UsageStats> stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startOfDay, currentTime); // Changed end time to currentTime
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

    private String formatMillis(long millis) {
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        return String.format("%02d:%02d:%02d", hours, minutes % 60, seconds % 60);
    }

    private void insertUnblockNumber(String date, int unblockCount) {
        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
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

    private void insertUsageTime(String date, long usageTime) {
        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
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

    private List<String> getDatabaseDates() {
        List<String> dates = new ArrayList<>();

        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
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

    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void getData() {
        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
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
                while (cursor.moveToNext()) {
                    String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE));
                    long usageTime = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USAGE_TIME));

                    Log.d("Database", "Date: " + date + ", Usage Time: " + formatMillis(usageTime));
                }
            } finally {
                cursor.close();
            }
        }
        database.close();
    }
    private class YAxisValueFormatter extends ValueFormatter {
        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            long millis = (long) value;
            long seconds = millis / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;

            return String.format(Locale.getDefault(), "%02d:%02d", hours, minutes % 60);
        }
    }
    private class BarValueFormatter extends ValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            long millis = (long) value;
            long seconds = millis / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;

            return String.format(Locale.getDefault(), "%02d:%02d", hours, minutes % 60);
        }
    }
}