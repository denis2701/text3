package com.example.text3;

import android.app.DatePickerDialog;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MostUsedApps extends AppCompatActivity {

    private final List<Dashboard.AppUsageInfo> appUsageList;
    private DatabaseHelper databaseHelper;
    private Button buttonAppUsage;
    private Calendar selectedDate;
    private TextView textDateCategories;

    public MostUsedApps() {
        this.appUsageList = new ArrayList<>();
    }

    public MostUsedApps(List<Dashboard.AppUsageInfo> appUsageList) {
        this.appUsageList = appUsageList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostusedapps);

        textDateCategories = findViewById(R.id.textDateCategories2);

        databaseHelper = new DatabaseHelper(this);

        buttonAppUsage = findViewById(R.id.appUsageButton);

        buttonAppUsage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCalendar();
            }
        });

        selectedDate = Calendar.getInstance();
        updateAppUsageList(selectedDate);

        updateDateText(selectedDate);
    }

    private void openCalendar() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(MostUsedApps.this, R.style.DatePickerDialogTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        selectedDate = Calendar.getInstance();
                        selectedDate.set(year, month, dayOfMonth);
                        updateAppUsageList(selectedDate);
                        updateDateText(selectedDate); // Update the date text here
                    }
                }, year, month, dayOfMonth);

        datePickerDialog.show();
    }

    private void updateDateText(Calendar selectedDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = dateFormat.format(selectedDate.getTime());
        textDateCategories.setText(formattedDate);
    }

    private void updateAppUsageList(Calendar selectedDate) {
        List<Dashboard.AppUsageInfo> appUsageList = databaseHelper.getMostUsedAppsForDate(selectedDate);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        AppUsageAdapter adapter = new AppUsageAdapter(this, appUsageList);
        recyclerView.setAdapter(adapter);
    }

    public static class AppUsageAdapter extends RecyclerView.Adapter<AppUsageAdapter.AppUsageViewHolder> {

        private Context context;
        private List<Dashboard.AppUsageInfo> appUsageList;

        public AppUsageAdapter(Context context, List<Dashboard.AppUsageInfo> appUsageList) {
            this.context = context;
            this.appUsageList = appUsageList;
        }

        @NonNull
        @Override
        public AppUsageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_item_layout, parent, false);
            return new AppUsageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AppUsageViewHolder holder, int position) {
            Dashboard.AppUsageInfo appUsageInfo = appUsageList.get(position);
            holder.appNameTextView.setText(appUsageInfo.getAppName());
            holder.setAppIcon(appUsageInfo.getPackageName());
            String usageTimeText = formatUsageTime(appUsageInfo.getUsageTime());
            holder.usageTimeTextView.setText(usageTimeText);
        }

        @Override
        public int getItemCount() {
            return appUsageList.size();
        }

        static class AppUsageViewHolder extends RecyclerView.ViewHolder {
            TextView appNameTextView;
            TextView usageTimeTextView;
            ImageView appIconImageView;

            public AppUsageViewHolder(@NonNull View itemView) {
                super(itemView);
                appNameTextView = itemView.findViewById(R.id.appNameTextView);
                usageTimeTextView = itemView.findViewById(R.id.usageTimeTextView);
                appIconImageView = itemView.findViewById(R.id.appIconImageView);
            }

            public void setAppIcon(String packageName) {
                try {
                    PackageManager packageManager = itemView.getContext().getPackageManager();
                    ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
                    Drawable appIcon = packageManager.getApplicationIcon(applicationInfo);
                    appIconImageView.setImageDrawable(appIcon);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    appIconImageView.setImageResource(R.drawable.ic_launcher_background);
                }
            }
        }
        private String formatUsageTime(long usageTime) {
            long hours = usageTime / (1000 * 60 * 60);
            long minutes = (usageTime % (1000 * 60 * 60)) / (1000 * 60);
            long seconds = ((usageTime % (1000 * 60 * 60)) % (1000 * 60)) / 1000;
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
    }
}