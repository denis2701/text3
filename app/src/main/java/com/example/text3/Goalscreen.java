package com.example.text3;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Goalscreen extends Fragment {

    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private DatabaseHelper dbHelper;
    private Map<String, Long> appUsageMap = new HashMap<>();

    public Goalscreen() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_goals, container, false);

        dbHelper = new DatabaseHelper(getActivity());
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<Task> taskList = dbHelper.getAllTasks();

        checkPhoneUsageTime(taskList);
        checkAppUsageTime(taskList);
        checkPhoneUnblockingTimes(taskList);
        checkAppUnblockingTimes(taskList);

        if (!taskList.isEmpty()) {
            taskAdapter = new TaskAdapter(taskList, dbHelper);
            recyclerView.setAdapter(taskAdapter);
        } else {
            Toast.makeText(getActivity(), "Jokių tikslų  nėra", Toast.LENGTH_SHORT).show();
        }

        rootView.findViewById(R.id.categoryButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CreateTaskActivity.class));
            }
        });

        return rootView;
    }

    private void checkPhoneUsageTime(List<Task> taskList) {
        long screenOnTimeToday = Utils.getDailyScreenOnTime(requireContext());
        for (Task task : taskList) {
            if ("Telefono naudojimo laikas".equals(task.getType())) {
                long goalTime = Utils.convertTimeToMillis(task.getTime());
                if (screenOnTimeToday > goalTime) {
                    sendNotification("Per ilgas naudojimosi telefonu laikas", "Ar pamiršai, kad nustatei tikslą naudotis telefonu mažiau?");
                    Log.d("TAG", String.valueOf(screenOnTimeToday));
                }
                break;
            }
        }
    }

    private void checkAppUsageTime(List<Task> taskList) {
        getDailyAppScreenOnTime();
        for (Task task : taskList) {
            if ("Programėlės naudojimo laikas".equals(task.getType())) {
                String appName = task.getAppName();
                if (appUsageMap.containsKey(appName)) {
                    long goalTime = Utils.convertTimeToMillis(task.getTime());
                    long appUsageTime = appUsageMap.get(appName);
                    if (appUsageTime > goalTime) {
                        sendNotification("Per ilgas programėlės naudojimo laikas", "Ar pamiršai, kad nustatei tikslą naudotis " + appName + " programėlę mažiau?");
                        Log.d("TAG", String.valueOf(appUsageTime));
                    }
                }
            }
        }
    }

    private void checkAppUnblockingTimes(List<Task> taskList) {
        Map<String, Integer> appUnlockCountMap = getAppUnlockCount();
        for (Task task : taskList) {
            if ("Programėlės įjungimo skaičius".equals(task.getType())) {
                String appName = task.getAppName();
                if (appUnlockCountMap.containsKey(appName)) {
                    int goalUnlockCount = Integer.parseInt(task.getTime());
                    int appUnlockCount = appUnlockCountMap.getOrDefault(appName, 0);
                    if (appUnlockCount > goalUnlockCount) {
                        sendNotification("Per daug įjungimų", "Ar pamiršai, kad nustatei tikslą įjungti " + appName + " programėlę mažiau kartų?");
                        Log.d("TAG", String.valueOf(appUnlockCount));
                    }
                }
            }
        }
    }

    private void checkPhoneUnblockingTimes(List<Task> taskList) {
        int screenUnblockingCount = getUnlockCount();
        for (Task task : taskList) {
            if ("Telefono atrakinimo skaičius".equals(task.getType())) {
                int storedUnblockingCount = Integer.parseInt(task.getTime());
                if (screenUnblockingCount > storedUnblockingCount) {
                    sendNotification("Per daug įjungi telefoną", "Ar pamiršai, kad nustatei tikslą naudotis telefonu mažiau?");
                    Log.d("TAG", String.valueOf(storedUnblockingCount));
                }
                break;
            }
        }
    }

    private int getUnlockCount() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int unlockCount = 0;
        if (db != null) {
            String todayDate = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                todayDate = java.time.LocalDate.now().toString();
            }

            Cursor cursor = db.rawQuery("SELECT unlock_count FROM unlock_counts WHERE unlock_count_date = ?", new String[]{todayDate});
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    unlockCount = cursor.getInt(0);
                }
                cursor.close();
            }
            db.close();
        }
        return unlockCount;
    }

    private Map<String, Integer> getAppUnlockCount() {
        UsageStatsManager usageStatsManager = (UsageStatsManager) requireActivity().getSystemService(Context.USAGE_STATS_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long startOfDay = calendar.getTimeInMillis();
        long endOfDay = System.currentTimeMillis();

        UsageEvents usageEvents = usageStatsManager.queryEvents(startOfDay, endOfDay);
        Map<String, Integer> appUnlockCountMap = new HashMap<>();

        while (usageEvents.hasNextEvent()) {
            UsageEvents.Event event = new UsageEvents.Event();
            usageEvents.getNextEvent(event);

            if (event.getEventType() == UsageEvents.Event.ACTIVITY_RESUMED) {
                String packageName = event.getPackageName();
                String appName = Utils.getAppNameFromPackage(requireContext(), packageName);

                int launchCount = appUnlockCountMap.getOrDefault(appName, 0);
                appUnlockCountMap.put(appName, launchCount + 1);
            }
        }

        return appUnlockCountMap;
    }

    private void getDailyAppScreenOnTime() {
        UsageStatsManager usageStatsManager = (UsageStatsManager) requireActivity().getSystemService(Context.USAGE_STATS_SERVICE);
        PackageManager packageManager = getActivity().getPackageManager();
        if (usageStatsManager != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            long startOfDay = calendar.getTimeInMillis();
            long endOfDay = startOfDay + 24 * 60 * 60 * 1000;

            List<UsageStats> stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startOfDay, endOfDay);
            if (stats != null) {
                for (UsageStats usageStats : stats) {
                    try {
                        CharSequence appName = packageManager.getApplicationLabel(packageManager.getApplicationInfo(usageStats.getPackageName(), PackageManager.GET_META_DATA));
                        appUsageMap.put(appName.toString(), usageStats.getTotalTimeInForeground());
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void sendNotification(String title, String message) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default", "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = requireActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        Notification.Builder builder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new Notification.Builder(getActivity(), "default")
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.piechart)
                    .setVibrate(new long[]{0, 1000, 1000, 1000});
        }

        NotificationManager notificationManager = requireActivity().getSystemService(NotificationManager.class);
        notificationManager.notify(1, builder.build());
    }
}