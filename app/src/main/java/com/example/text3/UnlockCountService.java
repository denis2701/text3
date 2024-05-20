package com.example.text3;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.text3.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnlockCountService extends JobIntentService {

    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "UnlockCountChannel";
    private static final int JOB_ID = 1001;
    private static final long JOB_INTERVAL = 60 * 10000;
    private static final long INTERVAL = 1000;
    private Handler mHandler = new Handler();
    private String titleText = "TimeTrack";
    private String notificationText = "Veikia fone";
    String title = "TimeTrack";
    String contentText = "Veikia fone";
    private String[] contentTextPhrases = {
            "Nustatyk telefono naudojimo laiko ribas.",
            "Išjunk nebūtinų programų pranešimus.",
            "Skirk daugiau laiko skaitymui ir mankštai.",
            "Paslėpk telefoną dirbant ar praleisdami laiką su kitais žmonėmis.",
            "Atkreipk dėmesį į akis į akį bendravimą, o ne į telefonus.",
            "Skatink nevirtualius santykius, kuriant telefonui laisvus plotus.",
            "Daryk pertraukas nuo telefono naudojimo.",
            "Naudok tradicinį žadintuvą, kad ryte nebūtų pagundos patikrinti telefono.",
            "Nustatyk laiko ribas socialiniams tinklams ir laikykis jų.",
            "Suplanuok išvykas ar pomėgius laisvu laiku.",
            "Stebėk savo telefono naudojimą, paspaudus pranešimą.",
            "Sumažink pagundas keisdami telefono spalvą į mažiau patrauklią.",
            "Skatink save naudoti vieną programą užblokuodamas kitas.",
            "Padėk telefoną nuo rankų, kad sumažinti pagundą jį patikrinti.",
            "Naudok programėlių blokavimą, kad nenaudoti socialinių tinklų.",
            "Išjunk telefoną jei niekam nereikia su tavimi susisiekti.",
            "Skirk laiko fiziniam aktyvumui.",
            "Naudok tikslus, kurie stebės tavo naudojimo progresą.",
            "Buk kantrūs sau, siekiant sumažinti telefono naudojimą.",
            "Skirk laiko aktyviam klausymuisi per pokalbius.",
            "Pateik telefono susijusias užduotis kitam asmeniui, jei įmanoma.",
            "Stebėk savo duomenų naudojimą paspaudus pranešimą.",
            "Bandyk mažinti nereikalingą ekrano naudojimo laiką.",
            "Skirk laiko kūrybiniams darbams ar pomėgiams.",
            "Naudok įrenginio naudojimo stebėjimo programas, siekiant m.",
            "Sumažinkite pranešimų srautą, išjungdami nereikalingų programų pranešimus.",
            "Naudokite programų ribojimo funkcijas, kad sumažintumėte laiką, praleistą su tam tikromis programomis.",
            "Būkite sąmoningi savo tikslams sumažinti telefono naudojimą ir stebėkite jų įgyvendinimą.",
            "Pasidalinkite savo noru sumažinti telefono naudojimą su draugais ir šeimos nariais.",
            "Atkreipkite dėmesį į tai, kiek laiko praleidžiate prie telefono ir nustatykite savo prioritetus.",
            "Praleiskite laiko su draugais ar gamta, atsijungę nuo telefono, kad atkurtumėte pusiausvyrą.",
            "Naudokite programų stebėjimo programas, kad matytumėte, kaip dažnai ir kiek laiko leidžiate su telefonu.",
            "Naudokite Neblaškymo režimą, kad sutelktumėte dėmesį į svarbias užduotis ar susitikimus.",
            "Pabandykite naudoti telefoną tik tam tikru laiku per dieną, kad sumažintumėte jo nuolatinį naudojimą.",
            "Naudokite telefoną kaip priemonę dėmesio ir laiko valdymui, o ne kaip nuolatinę pramogą.",
            "Stebėkite, kiek laiko praleidžiate prie telefono kiekvieną dieną ir nustatykite sau ribas.",
            "Pabandykite atlikti nevirtualius užsiėmimus, tokius kaip meno kūrimas ar rankdarbiai, kad sumažintumėte telefono naudojimą.",
            "Naudokite telefono palaikymo grupes ar bendruomenes, kad gautumėte palaikymo ir motyvacijos sumažinti naudojimą.",
            "Sukurkite telefono laisvus plotus savo namuose, kurie bus skirti ne telefono veikloms.",
            "Atsijunkite nuo savo telefono kelionėse ar gamtos pasivaikščiojimuose, kad gautumėte laiko atsikvėpti.",
            "Stenkitės naudoti telefoną tik tam tikras paskirtis, pvz., komunikacijai ar informacijos paieškai.",
            "Sukurkite rutiną, kurioje numatyti laiką be telefono, kad atstatytumėte savo dėmesį.",
            "Stenkitės valdyti savo telefono naudojimą, o ne leisti jam jums valdyti.",
            "Naudokite telefono tylėjimo funkciją, kad sumažintumėte pertraukimų ir pranešimų skaičių.",
            "Atsikratykite telefono prietaisų, kurie gali jus pagundyti jį dažniau patikrinti, pvz., dedamas į kitą kambarį.",
            "Pamėginkite nešioti fizinį laikrodį, o ne naudoti telefono laikrodį, kad sumažintumėte nuolatinį telefono naudojimą.",
            "Praleiskite laiko su vaikais ar šeimos nariais, atsijungę nuo telefono, kad sukurtumėte stipresnius ryšius.",
            "Pabandykite atkreipti dėmesį į tai, kiek laiko praleidžiate prie telefono ir stenkitės sumažinti šį laiką.",
            "Sukurkite telefono laisvą zoną savo miegamajame, kad skatintumėte gerą miego higieną.",
            "Naudojate telefono užraktą, kad suteiktumėte sau pertrauką nuo jo naudojimo tam tikru laikotarpiu.",
            "Sukurkite telefono panaudojimo grafiką ir stenkitės laikytis jo kuo labiau.",
            "Atsikratykite nereikalingų programų ar pramogų, kad sumažintumėte pagundas.",
            "Sukurkite sąmoningumą dėl savo telefono naudojimo įpročių ir stenkitės juos pakeisti.",
            "Pasakokite savo draugams ar šeimai apie savo pastangas sumažinti telefono naudojimą ir paprašykite jų palaikymo.",
            "Stenkitės naudoti telefono funkcijas taip, kad jos padėtų jums efektyviai tvarkytis su telefono naudojimu, o ne priešingai.",
            "Praleiskite laiko lauke ar gamtoje, atsijungę nuo telefono, kad atgautumėte savo ryšį su aplinka.",
            "Stenkitės naudoti telefono pagalbos programas ar palaikymo grupes, jei jaučiate, kad negalite kontroliuoti savo telefono naudojimo.",
    };

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            updateNotification();
            mHandler.postDelayed(this, INTERVAL);
        }
    };

    private WindowManager mWindowManager;
    private View mOverlayView;
    private TextView mOverlayText;
    private List<String> blockedAppNames;

    private BroadcastReceiver unlockReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_USER_PRESENT.equals(intent.getAction())) {
                incrementUnlockCount();
                startSendingNotifications();
                scheduleJob();
            }
        }
    };

    private void startSendingNotifications() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateNotification();
                mHandler.postDelayed(this, INTERVAL);
            }
        }, INTERVAL);
    }

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, UnlockCountService.class, JOB_ID, work);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler.postDelayed(mRunnable, INTERVAL);
        createNotificationChannel();

        IntentFilter filter = new IntentFilter(Intent.ACTION_USER_PRESENT);
        registerReceiver(unlockReceiver, filter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(1, createNotification());
        }

        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        blockedAppNames = databaseHelper.getBlockedAppNames();

        StringBuilder blockedAppsLog = new StringBuilder("Blocked Apps: ");
        for (String appName : blockedAppNames) {
            blockedAppsLog.append(appName).append(", ");
        }
        if (!blockedAppNames.isEmpty()) {
            blockedAppsLog.setLength(blockedAppsLog.length() - 2);
        }
        android.util.Log.d("AppList", blockedAppsLog.toString());
    }

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(unlockReceiver);
        mHandler.removeCallbacks(mRunnable);
        if (mOverlayView != null) {
            mWindowManager.removeView(mOverlayView);
        }
    }

    @Override
    protected void onHandleWork(@Nullable Intent intent) {
        Log.d("YourTag", "Periodic task executed");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void receiveBlockAppNames(List<String> blockappnames) {
        this.blockedAppNames = blockappnames;
        StringBuilder logMessage = new StringBuilder("Received blocked app names: ");
        for (String appName : blockappnames) {
            logMessage.append(appName).append(", ");
        }
        if (!blockappnames.isEmpty()) {
            logMessage.setLength(logMessage.length() - 2);
        }
        android.util.Log.d("AppList", logMessage.toString());

        StringBuilder updatedBlockedAppsLog = new StringBuilder("Updated Blocked Apps: ");
        for (String appName : blockedAppNames) {
            updatedBlockedAppsLog.append(appName).append(", ");
        }
        if (!blockedAppNames.isEmpty()) {
            updatedBlockedAppsLog.setLength(updatedBlockedAppsLog.length() - 2);
        }
        android.util.Log.d("AppList", updatedBlockedAppsLog.toString());
        updateNotification();
    }

    private String getRandomText() {
        // Generate a random index within the range of contentTextPhrases array length
        int randomIndex = (int) (Math.random() * contentTextPhrases.length);
        // Retrieve a random phrase from the array
        return contentTextPhrases[randomIndex];
    }

    private void incrementUnlockCount() {
        SharedPreferences prefs = getSharedPreferences("UnlockPrefs", Context.MODE_PRIVATE);
        int currentCount = prefs.getInt("UnlockCount", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("UnlockCount", currentCount + 1);
        editor.apply();

        Log.d("YourTag", "incrementUnlockCount: This method is used");
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Unlock Count Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }

    private void scheduleJob() {
        ComponentName componentName = new ComponentName(this, UnlockCountJobService.class);
        JobInfo jobInfo = new JobInfo.Builder(JOB_ID, componentName)
                .setPeriodic(JOB_INTERVAL)
                .setPersisted(true)
                .build();

        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        if (jobScheduler != null) {
            jobScheduler.schedule(jobInfo);
        }
    }

    @SuppressLint("WrongConstant")
    private String getRunningApps() {
        StringBuilder runningApps = new StringBuilder();
        long endTime = System.currentTimeMillis();
        long startTime = endTime - INTERVAL;

        UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        UsageEvents usageEvents = usageStatsManager.queryEvents(startTime, endTime);

        UsageEvents.Event event = new UsageEvents.Event();
        while (usageEvents.hasNextEvent()) {
            usageEvents.getNextEvent(event);
            if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                PackageManager packageManager = getPackageManager();
                try {
                    CharSequence appName = packageManager.getApplicationLabel(packageManager.getApplicationInfo(event.getPackageName(), PackageManager.GET_META_DATA));
                    runningApps.append(appName).append("\n");
                    Log.d("RunningApp", "App: " + appName.toString());
                    for (String blockedApp : blockedAppNames) {
                        if (event.getPackageName().equals(blockedApp)) {
                            showOverlayWindow();
                            break;
                        }
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        return runningApps.toString();
    }

    private int count = 0;

    private void updateNotification() {
        DatabaseHelper dHelper = new DatabaseHelper(getApplicationContext());
        List<Task> taskList = dHelper.getAllTasks();
        if (count == 10) {
            if (checkPhoneUsageTime(taskList)) {
                titleText = "Įspėjimas apie telefono naudojimą";
                notificationText = "Ar pamiršai, kad nustatei tikslą naudotis telefonu mažiau?";
                String runningApps = getRunningApps();
                Intent notificationIntent = new Intent(this, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this,
                        0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

                Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle(titleText)
                        .setContentText(notificationText)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentIntent(pendingIntent)
                        .setSilent(true)
                        .build();

                startForeground(NOTIFICATION_ID, notification);
                count = 0;
            } else if (checkAppUsageTime(taskList)) {
                titleText = "Programėlės naudojimo įspėjimas";
                notificationText = "Ar pamiršai, kad nustatei tikslą naudotis programėlėmis mažiau?";
                String runningApps = getRunningApps();
                Intent notificationIntent = new Intent(this, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this,
                        0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

                Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle(titleText)
                        .setContentText(notificationText)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentIntent(pendingIntent)
                        .setSilent(true)
                        .build();

                startForeground(NOTIFICATION_ID, notification);
                count = 0;
            } else if (checkAppUnblockingTimes(taskList)) {
                titleText = "Programėlės įjungimo įspėjimas";
                notificationText = "Ar pamiršai, kad nustatei tikslą naudotis programėlėmis mažiau?";
                String runningApps = getRunningApps();
                Intent notificationIntent = new Intent(this, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this,
                        0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

                Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle(titleText)
                        .setContentText(notificationText)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentIntent(pendingIntent)
                        .setSilent(true)
                        .build();

                startForeground(NOTIFICATION_ID, notification);
                count = 0;
            } else if (checkPhoneUnblockingTimes(taskList)) {
                titleText = "Telefono atrakinimo įspėjimas";
                notificationText = "Ar pamiršai, kad nustatei tikslą naudotis telefonu mažiau?";
                String runningApps = getRunningApps();
                Intent notificationIntent = new Intent(this, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this,
                        0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

                Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle(titleText)
                        .setContentText(notificationText)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentIntent(pendingIntent)
                        .setSilent(true)
                        .build();

                startForeground(NOTIFICATION_ID, notification);
                count = 0;
            }
            else {
                titleText = "TimeTrack";
                String randomText = getRandomText();
                notificationText = randomText;
                String runningApps = getRunningApps();
                Intent notificationIntent = new Intent(this, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this,
                        0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

                Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle(titleText)
                        .setContentText(notificationText)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentIntent(pendingIntent)
                        .setSilent(true)
                        .build();

                startForeground(NOTIFICATION_ID, notification);
                count = 0;
            }
        }

        else {
            String runningApps = getRunningApps();
            Intent notificationIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,
                    0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle(titleText)
                    .setContentText(notificationText)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .setSilent(true)
                    .build();

            startForeground(NOTIFICATION_ID, notification);
            count++;
        }
    }

    private Notification createNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("TimeTrack")
                .setContentText("Veikia fone")
                .setSmallIcon(R.drawable.piechart)
                .setColor(ContextCompat.getColor(this, R.color.black))
                .setOngoing(true)
                .build();
    }

    private void showOverlayWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }

        if (mOverlayView == null) {
            mOverlayView = LayoutInflater.from(this).inflate(R.layout.overlay_window, null);

            mOverlayText = mOverlayView.findViewById(R.id.overlay_text);
            Button overlayButton = mOverlayView.findViewById(R.id.overlay_button);
            overlayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeOverlayWindow();

                    Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.example.text3");
                    if (launchIntent != null) {
                        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(launchIntent);
                    } else {
                        Log.e("BackgroundService", "App is not installed: com.example.text3");
                    }
                }
            });

            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                            WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                    PixelFormat.TRANSLUCENT);

            params.gravity = Gravity.CENTER;

            mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            mWindowManager.addView(mOverlayView, params);
        }
    }

    private void closeOverlayWindow() {
        if (mOverlayView != null && mWindowManager != null) {
            mWindowManager.removeView(mOverlayView);
            mOverlayView = null;
            mWindowManager = null;
        }
    }

    private DatabaseHelper databHelper;
    private Map<String, Long> appUsageMap = new HashMap<>();

    private boolean checkPhoneUsageTime(List<Task> taskList) {
        long screenOnTimeToday = getDailyScreenOnTime();
        boolean exceededLimit = false;
        for (Task task : taskList) {
            if ("Telefono naudojimo laikas".equals(task.getType())) {
                long goalTime = convertTimeToMillis(task.getTime());
                if (screenOnTimeToday > goalTime) {
                    exceededLimit = true;
                    break;
                }
            }
        }
        return exceededLimit;
    }

    private boolean checkAppUsageTime(List<Task> taskList) {
        getDailyAppScreenOnTime();
        boolean exceededLimit = false;
        for (Task task : taskList) {
            if ("Programėlės naudojimo laikas".equals(task.getType())) {
                String appName = task.getAppName();
                if (appUsageMap.containsKey(appName)) {
                    long goalTime = convertTimeToMillis(task.getTime());
                    long appUsageTime = appUsageMap.get(appName);
                    if (appUsageTime > goalTime) {
                        exceededLimit = true;
                        break;
                    }
                }
            }
        }
        return exceededLimit;
    }

    private boolean checkAppUnblockingTimes(List<Task> taskList) {
        boolean exceededLimit = false;
        Map<String, Integer> appUnlockCountMap = getAppUnlockCount();
        for (Task task : taskList) {
            if ("Programėlės įjungimo skaičius".equals(task.getType())) {
                String appName = task.getAppName();
                if (appUnlockCountMap.containsKey(appName)) {
                    int goalUnlockCount = Integer.parseInt(task.getTime());
                    int appUnlockCount = appUnlockCountMap.getOrDefault(appName, 0);
                    if (appUnlockCount > goalUnlockCount) {
                        exceededLimit = true;
                        break;
                    }
                }
            }
        }
        return exceededLimit;
    }

    private boolean checkPhoneUnblockingTimes(List<Task> taskList) {
        boolean exceededLimit = false;
        int screenUnblockingCount = getUnlockCount();
        for (Task task : taskList) {
            if ("Telefono atrakinimo skaičius".equals(task.getType())) {
                int storedUnblockingCount = Integer.parseInt(task.getTime());
                if (screenUnblockingCount > storedUnblockingCount) {
                    exceededLimit = true;
                    break;
                }
            }
        }
        return exceededLimit;
    }
    private int getUnlockCount() {
        if (databHelper == null) {
            return 0;
        }
        SQLiteDatabase db = databHelper.getReadableDatabase();
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
        UsageStatsManager usageStatsManager = (UsageStatsManager) getApplicationContext().getSystemService(Context.USAGE_STATS_SERVICE);
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
                String appName = getAppNameFromPackage(getApplicationContext(), packageName);

                int launchCount = appUnlockCountMap.getOrDefault(appName, 0);
                appUnlockCountMap.put(appName, launchCount + 1);
            }
        }

        return appUnlockCountMap;
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

    private long getDailyScreenOnTime() {
        UsageStatsManager usageStatsManager = (UsageStatsManager) getApplicationContext().getSystemService(Context.USAGE_STATS_SERVICE);
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
                long totalUsageTime = 0;
                for (UsageStats usageStats : stats) {
                    totalUsageTime += usageStats.getTotalTimeInForeground();
                }
                return totalUsageTime;
            }
        }
        return 0;
    }

    private void getDailyAppScreenOnTime() {
        UsageStatsManager usageStatsManager = (UsageStatsManager) getApplicationContext().getSystemService(Context.USAGE_STATS_SERVICE);
        PackageManager packageManager = getApplicationContext().getPackageManager();
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
    private long convertTimeToMillis(String time) {
        String[] parts = time.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return (hours * 60 + minutes) * 60 * 1000;
    }
}