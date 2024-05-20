package com.example.text3;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AppHistory extends AppCompatActivity {

    private List<AppHistoryData> appUsageList;
    private DatabaseHelper databaseHelper;
    private Button buttonAppUsage;
    private Calendar selectedDate;
    private TextView textDateCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apphistory);

        textDateCategories = findViewById(R.id.textDateCategories2);

        databaseHelper = new DatabaseHelper(this);

        buttonAppUsage = findViewById(R.id.appUsageButton);

        buttonAppUsage.setOnClickListener(v -> openCalendar());

        selectedDate = Calendar.getInstance();
        updateAppUsageList(selectedDate);

        updateDateText(selectedDate);
    }

    private void openCalendar() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DatePickerDialogTheme,
                (view, year1, monthOfYear, dayOfMonth1) -> {
                    selectedDate = Calendar.getInstance();
                    selectedDate.set(year1, monthOfYear, dayOfMonth1);
                    updateAppUsageList(selectedDate);
                    updateDateText(selectedDate); // Update the date text here
                }, year, month, dayOfMonth);

        datePickerDialog.show();
    }

    private void updateDateText(Calendar selectedDate) {
        textDateCategories.setText(formatDate(selectedDate.getTimeInMillis()));
    }

    private void updateAppUsageList(Calendar selectedDate) {
        if (appUsageList == null) {
            appUsageList = new ArrayList<>();
        } else {
            appUsageList.clear();
        }
        List<AppHistoryData> appUsageDataList = databaseHelper.getAppHistoryForDate(selectedDate, getPackageManager());
        if (appUsageDataList != null) {
            appUsageList.addAll(appUsageDataList);
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        AppHistoryAdapter adapter = new AppHistoryAdapter(appUsageList, getPackageManager());
        recyclerView.setAdapter(adapter);
    }

    private String formatDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }
    public static class AppHistoryInfo {
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
}