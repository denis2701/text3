package com.example.text3;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MostOpenedApps extends AppCompatActivity {

    private final List<Dashboard.AppUsageUnlockInfo> appUsageList;
    private DatabaseHelper databaseHelper;
    private Button buttonAppUsage;
    private Calendar selectedDate;
    private TextView textDateCategories;

    public MostOpenedApps() {
        this.appUsageList = new ArrayList<>();
    }

    public MostOpenedApps(List<Dashboard.AppUsageUnlockInfo> appUsageList) {
        this.appUsageList = appUsageList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostopenedapps);

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

        DatePickerDialog datePickerDialog = new DatePickerDialog(MostOpenedApps.this, R.style.DatePickerDialogTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        selectedDate = Calendar.getInstance();
                        selectedDate.set(year, month, dayOfMonth);
                        updateAppUsageList(selectedDate);
                        updateDateText(selectedDate);
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
        List<Dashboard.AppUsageUnlockInfo> appUsageList = databaseHelper.getMostUnlockedAppsForDate(selectedDate);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        MostOpenedApps.AppUnblockAdapter adapter = new MostOpenedApps.AppUnblockAdapter(this, appUsageList);
        recyclerView.setAdapter(adapter);
    }

    public static class AppUnblockAdapter extends RecyclerView.Adapter<MostOpenedApps.AppUnblockAdapter.AppUsageViewHolder> {

        private Context context;
        private List<Dashboard.AppUsageUnlockInfo> appUsageList;

        public AppUnblockAdapter(Context context, List<Dashboard.AppUsageUnlockInfo> appUsageList) {
            this.context = context;
            this.appUsageList = appUsageList;
        }

        @NonNull
        @Override
        public MostOpenedApps.AppUnblockAdapter.AppUsageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_item_layout, parent, false);
            return new MostOpenedApps.AppUnblockAdapter.AppUsageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MostOpenedApps.AppUnblockAdapter.AppUsageViewHolder holder, int position) {
            Dashboard.AppUsageUnlockInfo appUsageInfo = appUsageList.get(position);
            holder.appNameTextView.setText(appUsageInfo.getAppName());
            holder.setAppIcon(appUsageInfo.getPackageName());
            holder.usageTimeTextView.setText(String.valueOf(appUsageInfo.getLaunchCount()));
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
