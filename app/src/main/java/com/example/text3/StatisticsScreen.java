package com.example.text3;

import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

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

public class StatisticsScreen extends Fragment {

    private BarChart barChart;
    private BarChart barChart2;
    private BarChart barChart3;
    private BarChart barChart4;
    private BarChart barChart5;
    private BarChart barChart6;
    private static final String TAG = "UsageStatsSample";
    private boolean logOpenCloseEvents = false;
    private String lastPackageName = "";
    private long lastOpenTimestamp = 0;


    private RecyclerView recyclerView;

    public StatisticsScreen() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_statisticsscreen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateUsageTime2(view);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Set layout manager

        databaseHelper = new DatabaseHelper(getActivity());

        retrieveUsageStats();
    }

    public void updateUsageTime2(View view) {
        Context context = requireContext();
        Context context2 = requireContext();
        Context context3 = requireContext();
        Context context4 = requireContext();
        Context context5 = requireContext();
        Context context6 = requireContext();

        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        dbHelper.removeDuplicateEntries();

        List<BarEntry> barEntries = getDatabaseEntries();
        List<BarEntry> barEntries2 = getDatabaseEntries2();

        barChart = view.findViewById(R.id.barGraphLayout1);
        barChart2 = view.findViewById(R.id.barGraphLayout2);
        barChart3 = view.findViewById(R.id.barGraphLayout3);
        barChart4 = view.findViewById(R.id.barGraphLayout4);
        barChart5 = view.findViewById(R.id.barGraphLayout5);
        barChart6 = view.findViewById(R.id.barGraphLayout6);

        barChart.setScaleEnabled(false);
        barChart.setPinchZoom(false);
        barChart2.setScaleEnabled(false);
        barChart2.setPinchZoom(false);
        barChart3.setScaleEnabled(false);
        barChart3.setPinchZoom(false);
        barChart4.setScaleEnabled(false);
        barChart4.setPinchZoom(false);
        barChart5.setScaleEnabled(false);
        barChart5.setPinchZoom(false);
        barChart6.setScaleEnabled(false);
        barChart6.setPinchZoom(false);

        BarDataSet barDataSet = new BarDataSet(barEntries, "Usage Time");
        BarDataSet barDataSet2 = new BarDataSet(barEntries2, "Screen Unlock Time");
        BarDataSet barDataSet3 = new BarDataSet(getDatabaseEntries3(), "Usage Time");
        BarDataSet barDataSet4 = new BarDataSet(getDatabaseEntries4(), "Usage Time");
        BarDataSet barDataSet5 = new BarDataSet(getDatabaseEntries5(), "Screen Unlock Time");
        BarDataSet barDataSet6 = new BarDataSet(getDatabaseEntries6(), "Screen Unlock Time");

        barDataSet.setColor(ContextCompat.getColor(context, R.color.yellow));
        barDataSet2.setColor(ContextCompat.getColor(context2, R.color.yellow));
        barDataSet3.setColor(ContextCompat.getColor(context3, R.color.yellow));
        barDataSet4.setColor(ContextCompat.getColor(context4, R.color.yellow));
        barDataSet5.setColor(ContextCompat.getColor(context5, R.color.yellow));
        barDataSet6.setColor(ContextCompat.getColor(context6, R.color.yellow));

        barDataSet.setValueTextColor(Color.WHITE);
        barDataSet2.setValueTextColor(Color.WHITE);
        barDataSet3.setValueTextColor(Color.WHITE);
        barDataSet4.setValueTextColor(Color.WHITE);
        barDataSet5.setValueTextColor(Color.WHITE);
        barDataSet6.setValueTextColor(Color.WHITE);

        barDataSet2.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value);
            }
        });

        barDataSet5.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value);
            }
        });

        BarData barData = new BarData(barDataSet);
        BarData barData2 = new BarData(barDataSet2);
        BarData barData3 = new BarData(barDataSet3);
        BarData barData4 = new BarData(barDataSet4);
        BarData barData5 = new BarData(barDataSet5);
        BarData barData6 = new BarData(barDataSet6);

        barData.setValueFormatter(new StatisticsScreen.BarValueFormatter2());
        barData3.setValueFormatter(new StatisticsScreen.BarValueFormatter2());
        barData4.setValueFormatter(new StatisticsScreen.BarValueFormatter2());

        barChart.setData(barData);
        barChart2.setData(barData2);
        barChart3.setData(barData3);
        barChart4.setData(barData4);
        barChart5.setData(barData5);
        barChart6.setData(barData6);

        XAxis xAxis = barChart.getXAxis();
        XAxis xAxis2 = barChart2.getXAxis();
        XAxis xAxis3 = barChart3.getXAxis();
        XAxis xAxis4 = barChart4.getXAxis();
        XAxis xAxis5 = barChart5.getXAxis();
        XAxis xAxis6 = barChart6.getXAxis();

        List<String> dateLabels = getDatabaseDates();
        List<String> dateLabels2 = getDatabaseDates2();
        List<String> dateLabels3 = getTimeIntervals();
        List<String> dateLabels4 = getTimeIntervals2();
        List<String> dateLabels5 = getTimeIntervals();
        List<String> dateLabels6 = getTimeIntervals2();
        Collections.reverse(dateLabels);
        Collections.reverse(dateLabels2);

        xAxis.setValueFormatter(new IndexAxisValueFormatter(dateLabels));
        xAxis2.setValueFormatter(new IndexAxisValueFormatter(dateLabels2));
        xAxis3.setValueFormatter(new IndexAxisValueFormatter(dateLabels3));
        xAxis4.setValueFormatter(new IndexAxisValueFormatter(dateLabels4));
        xAxis5.setValueFormatter(new IndexAxisValueFormatter(dateLabels5));
        xAxis6.setValueFormatter(new IndexAxisValueFormatter(dateLabels6));

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis3.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis4.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis5.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis6.setPosition(XAxis.XAxisPosition.BOTTOM);

        xAxis.setGranularity(1f);
        xAxis2.setGranularity(1f);
        xAxis3.setGranularity(1f);
        xAxis4.setGranularity(1f);
        xAxis5.setGranularity(1f);
        xAxis6.setGranularity(1f);

        xAxis.setLabelRotationAngle(45);
        xAxis2.setLabelRotationAngle(45);
        xAxis3.setLabelRotationAngle(45);
        xAxis4.setLabelRotationAngle(45);
        xAxis5.setLabelRotationAngle(45);
        xAxis6.setLabelRotationAngle(45);

        xAxis.setTextColor(Color.WHITE);
        xAxis2.setTextColor(Color.WHITE);
        xAxis3.setTextColor(Color.WHITE);
        xAxis4.setTextColor(Color.WHITE);
        xAxis5.setTextColor(Color.WHITE);
        xAxis6.setTextColor(Color.WHITE);

        xAxis.setDrawGridLines(false);
        xAxis2.setDrawGridLines(false);
        xAxis3.setDrawGridLines(false);
        xAxis4.setDrawGridLines(false);
        xAxis5.setDrawGridLines(false);
        xAxis6.setDrawGridLines(false);

        YAxis yAxisLeft = barChart.getAxisLeft();
        YAxis yAxisLeft2 = barChart2.getAxisLeft();
        YAxis yAxisLeft3 = barChart3.getAxisLeft();
        YAxis yAxisLeft4 = barChart4.getAxisLeft();
        YAxis yAxisLeft5 = barChart5.getAxisLeft();
        YAxis yAxisLeft6 = barChart6.getAxisLeft();
        yAxisLeft.setTextColor(Color.WHITE);
        yAxisLeft2.setTextColor(Color.WHITE);
        yAxisLeft3.setTextColor(Color.WHITE);
        yAxisLeft4.setTextColor(Color.WHITE);
        yAxisLeft5.setTextColor(Color.WHITE);
        yAxisLeft6.setTextColor(Color.WHITE);
        yAxisLeft.setValueFormatter(new StatisticsScreen.YAxisValueFormatter2());
        yAxisLeft3.setValueFormatter(new StatisticsScreen.YAxisValueFormatter2());
        yAxisLeft4.setValueFormatter(new StatisticsScreen.YAxisValueFormatter2());

        YAxis yAxisRight = barChart.getAxisRight();
        YAxis yAxisRight2 = barChart2.getAxisRight();
        YAxis yAxisRight3 = barChart3.getAxisRight();
        YAxis yAxisRight4 = barChart4.getAxisRight();
        YAxis yAxisRight5 = barChart5.getAxisRight();
        YAxis yAxisRight6 = barChart6.getAxisRight();
        yAxisRight.setTextColor(Color.WHITE);
        yAxisRight2.setTextColor(Color.WHITE);
        yAxisRight3.setTextColor(Color.WHITE);
        yAxisRight4.setTextColor(Color.WHITE);
        yAxisRight5.setTextColor(Color.WHITE);
        yAxisRight6.setTextColor(Color.WHITE);
        yAxisRight.setValueFormatter(new StatisticsScreen.YAxisValueFormatter2());
        yAxisRight3.setValueFormatter(new StatisticsScreen.YAxisValueFormatter2());
        yAxisRight4.setValueFormatter(new StatisticsScreen.YAxisValueFormatter2());

        Legend legend = barChart.getLegend();
        Legend legend2 = barChart2.getLegend();
        Legend legend3 = barChart3.getLegend();
        Legend legend4 = barChart4.getLegend();
        Legend legend5 = barChart3.getLegend();
        Legend legend6 = barChart4.getLegend();
        legend.setTextColor(Color.WHITE);
        legend2.setTextColor(Color.WHITE);
        legend3.setTextColor(Color.WHITE);
        legend4.setTextColor(Color.WHITE);
        legend5.setTextColor(Color.WHITE);
        legend6.setTextColor(Color.WHITE);

        barChart.getDescription().setEnabled(false);
        barChart2.getDescription().setEnabled(false);
        barChart3.getDescription().setEnabled(false);
        barChart4.getDescription().setEnabled(false);
        barChart5.getDescription().setEnabled(false);
        barChart6.getDescription().setEnabled(false);

        barChart.invalidate();
        barChart2.invalidate();
        barChart3.invalidate();
        barChart4.invalidate();
        barChart5.invalidate();
        barChart6.invalidate();

        Button mostUsedButton = view.findViewById(R.id.categoryButton);
        mostUsedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CategoryApps.class);
                startActivity(intent);
            }
        });

        Button appHistoryButton = view.findViewById(R.id.appHistoryButton);
        appHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AppHistory.class);
                startActivity(intent);
            }
        });

        List<UsageStats> stats = getUsageStats();

        Map<String, Long> categoryUsageMap = new HashMap<>();

        PackageManager packageManager = requireContext().getPackageManager();

        for (UsageStats usageStats : stats) {
            String packageName = usageStats.getPackageName();

            try {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
                String category = getCategoryForApp(requireContext(), packageName);

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
            dbHelper.insertPieChartData(entry.getLabel(), (long) entry.getValue());
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.WHITE);

        pieDataSet.setSliceSpace(3f);
        pieDataSet.setSelectionShift(5f);
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setSelectionShift(5f);
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setSelectionShift(5f);
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setSelectionShift(5f);
        pieDataSet.setValueTextSize(10f);

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new StatisticsScreen.PieValueFormatter());

        PieChart pieChart = view.findViewById(R.id.piegraphlayout);

        pieChart.setHoleColor(Color.TRANSPARENT);

        pieData.setDrawValues(true);
        pieDataSet.setDrawValues(true);
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.setEntryLabelTextSize(12f);
        pieChart.setEntryLabelTypeface(Typeface.DEFAULT_BOLD);
        pieChart.getDescription().setEnabled(false);
        pieChart.getDescription().setPosition(1f, 1f);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);

        Legend legend7 = pieChart.getLegend();
        legend7.setTextColor(Color.WHITE);
        legend7.setTextSize(12f);
        legend7.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend7.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend7.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend7.setDrawInside(false);

        Spinner optionStats2Spinner = view.findViewById(R.id.optionStats2);
        Spinner optionStats3Spinner = view.findViewById(R.id.optionUnblock2);

        optionStats2Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position == 0) {
                    barChart.setVisibility(View.VISIBLE);
                    barChart3.setVisibility(View.GONE);
                    barChart4.setVisibility(View.GONE);
                } else if (position == 1) {
                    barChart.setVisibility(View.GONE);
                    barChart3.setVisibility(View.VISIBLE);
                    barChart4.setVisibility(View.GONE);
                } else if (position == 2) {
                    barChart.setVisibility(View.GONE);
                    barChart3.setVisibility(View.GONE);
                    barChart4.setVisibility(View.VISIBLE);
                } else {
                    barChart.setVisibility(View.GONE);
                    barChart3.setVisibility(View.GONE);
                    barChart4.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        optionStats3Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position == 0) {
                    barChart2.setVisibility(View.VISIBLE);
                    barChart5.setVisibility(View.GONE);
                    barChart6.setVisibility(View.GONE);
                } else if (position == 1) {
                    barChart2.setVisibility(View.GONE);
                    barChart5.setVisibility(View.VISIBLE);
                    barChart6.setVisibility(View.GONE);
                } else if (position == 2) {
                    barChart2.setVisibility(View.GONE);
                    barChart5.setVisibility(View.GONE);
                    barChart6.setVisibility(View.VISIBLE);
                } else {
                    barChart2.setVisibility(View.GONE);
                    barChart5.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private DatabaseHelper databaseHelper;

    private void retrieveUsageStats() {
        Context context = getActivity();
        if (context == null) {
            return;
        }

        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);

        if (usageStatsManager == null) {
            Log.e(TAG, "UsageStatsManager is null");
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

                        databaseHelper.insertAppUsageData(formatDate2(lastOpenTimestamp), event.getPackageName(), getAppName(context, event.getPackageName()), lastOpenTimestamp, closeTimestamp);
                    }
                    logOpenCloseEvents = false;
                }
            }
        } else {
            Log.d(TAG, "No usage events retrieved.");
        }

        AppHistoryAdapter adapter = new AppHistoryAdapter(usageStatsDataList, context.getPackageManager());

        recyclerView.setAdapter(adapter);
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

    private List<BarEntry> getDatabaseEntries3() {
        List<BarEntry> entries = new ArrayList<>();

        UsageStatsManager usageStatsManager = (UsageStatsManager) requireContext().getSystemService(Context.USAGE_STATS_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long startTime = calendar.getTimeInMillis();

        for (int i = 0; i < 24; i++) {
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            calendar.set(Calendar.HOUR_OF_DAY, i);

            long intervalStartTime = calendar.getTimeInMillis();
            long intervalEndTime = intervalStartTime + TimeUnit.HOURS.toMillis(1);

            UsageEvents usageEvents = usageStatsManager.queryEvents(intervalStartTime, intervalEndTime);

            long totalIntervalTime = calculateTotalTimeInForeground(usageEvents);

            if (totalIntervalTime > 3600001) {
                totalIntervalTime = 0;
            }

            Log.d("UsageStatsDebug", "Hour " + i + ": Total Time in Foreground = " + totalIntervalTime);

            entries.add(new BarEntry(i, totalIntervalTime));
        }

        return entries;
    }

    private List<BarEntry> getDatabaseEntries5() {
        List<BarEntry> entries = new ArrayList<>();

        UsageStatsManager usageStatsManager = (UsageStatsManager) requireContext().getSystemService(Context.USAGE_STATS_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long startTime = calendar.getTimeInMillis();

        for (int i = 0; i < 24; i++) {
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            calendar.set(Calendar.HOUR_OF_DAY, i);

            long intervalStartTime = calendar.getTimeInMillis();
            long intervalEndTime = intervalStartTime + TimeUnit.HOURS.toMillis(1);

            UsageEvents usageEvents = usageStatsManager.queryEvents(intervalStartTime, intervalEndTime);

            int unlockCount = countUnlockEvents(usageEvents);

            unlockCount = Math.round((float) unlockCount / 4f);

            Log.d("UsageStatsDebug", "Hour " + i + ": Unlock Count = " + unlockCount);

            entries.add(new BarEntry(i, unlockCount));
        }

        return entries;
    }

    private int countUnlockEvents(UsageEvents usageEvents) {
        int unlockCount = 0;
        boolean lastScreenState = false;

        while (usageEvents.hasNextEvent()) {
            UsageEvents.Event event = new UsageEvents.Event();
            usageEvents.getNextEvent(event);

            if (event.getEventType() == UsageEvents.Event.SCREEN_INTERACTIVE) {
                if (!lastScreenState) {
                    unlockCount++;
                }
                lastScreenState = true;
            } else if (event.getEventType() == UsageEvents.Event.SCREEN_NON_INTERACTIVE) {
                lastScreenState = false;
            }
        }

        return unlockCount;
    }

    private List<BarEntry> getDatabaseEntries4() {
        List<BarEntry> entries = new ArrayList<>();

        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());

        Calendar calendar = Calendar.getInstance();
        long currentTime = calendar.getTimeInMillis();

        calendar.add(Calendar.WEEK_OF_YEAR, -4);
        long startTime = calendar.getTimeInMillis();

        int numberOfIntervals = 4;
        long intervalDuration = TimeUnit.DAYS.toMillis(7);

        for (int i = numberOfIntervals - 1; i >= 0; i--) {
            long intervalEndTime = currentTime - i * intervalDuration;
            long intervalStartTime = intervalEndTime - intervalDuration;

            long totalIntervalTime = dbHelper.getUsageTimeForInterval(intervalStartTime, intervalEndTime);

            entries.add(new BarEntry(numberOfIntervals - 1 - i, totalIntervalTime));
        }

        Log.d("UsageStatsDebug", "Entries: " + entries.toString());

        return entries;
    }

    private List<BarEntry> getDatabaseEntries6() {
        List<BarEntry> entries = new ArrayList<>();

        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());

        Calendar calendar = Calendar.getInstance();
        long currentTime = calendar.getTimeInMillis();

        calendar.add(Calendar.WEEK_OF_YEAR, -4);
        long startTime = calendar.getTimeInMillis();

        int numberOfIntervals = 4;
        long intervalDuration = TimeUnit.DAYS.toMillis(7);

        for (int i = numberOfIntervals - 1; i >= 0; i--) {
            long intervalEndTime = currentTime - i * intervalDuration;
            long intervalStartTime = intervalEndTime - intervalDuration;

            long totalIntervalTime = dbHelper.getUnblockForInterval(intervalStartTime, intervalEndTime);

            entries.add(new BarEntry(numberOfIntervals - 1 - i, totalIntervalTime));
        }

        Log.d("UsageStatsDebug", "Entries: " + entries.toString());

        return entries;
    }

    private List<String> getTimeIntervals2() {
        List<String> timeIntervals = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        UsageStatsManager usageStatsManager = (UsageStatsManager) requireContext().getSystemService(Context.USAGE_STATS_SERVICE);

        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();

        calendar.add(Calendar.WEEK_OF_YEAR, -4);
        long startTime = calendar.getTimeInMillis();

        int numberOfIntervals = 4;
        long intervalDuration = TimeUnit.DAYS.toMillis(7);

        for (int i = 0; i < numberOfIntervals; i++) {
            long intervalStartTime = startTime + i * intervalDuration;
            long intervalEndTime = intervalStartTime + intervalDuration - 1;

            String formattedInterval = dateFormat.format(new Date(intervalStartTime)) + " - " + dateFormat.format(new Date(intervalEndTime));
            timeIntervals.add(formattedInterval);
        }

        return timeIntervals;
    }

    private long calculateTotalTimeInForeground(UsageEvents usageEvents) {
        long totalIntervalTime = 0;
        long eventStartTime = 0;

        while (usageEvents.hasNextEvent()) {
            UsageEvents.Event event = new UsageEvents.Event();
            usageEvents.getNextEvent(event);

            if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                eventStartTime = event.getTimeStamp();
            } else if (event.getEventType() == UsageEvents.Event.MOVE_TO_BACKGROUND) {
                totalIntervalTime += event.getTimeStamp() - eventStartTime;
            }
        }

        return totalIntervalTime;
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
        timeIntervals.add("18:01-19:00");
        timeIntervals.add("19:01-20:00");
        timeIntervals.add("20:01-21:00");
        timeIntervals.add("21:01-22:00");
        timeIntervals.add("22:01-23:00");
        timeIntervals.add("23:01-23:59");
        return timeIntervals;
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

    private List<UsageStats> getUsageStats() {
        UsageStatsManager usageStatsManager = (UsageStatsManager) requireContext().getSystemService(Context.USAGE_STATS_SERVICE);
        long currentTime = System.currentTimeMillis();
        List<UsageStats> stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, currentTime - 1000 * 60, currentTime);
        return stats;
    }

    private class PieValueFormatter extends ValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            long millis = (long) value;
            long seconds = millis / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;

            return String.format(Locale.getDefault(), "%02d:%02d", hours, minutes % 60);
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

    private class YAxisValueFormatter2 extends ValueFormatter {
        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            long millis = (long) value;
            long seconds = millis / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;

            return String.format(Locale.getDefault(), "%02d:%02d", hours, minutes % 60);
        }
    }
    private class BarValueFormatter2 extends ValueFormatter {
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