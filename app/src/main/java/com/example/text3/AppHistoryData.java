package com.example.text3;

public class AppHistoryData {
    private String packageName;
    private String appName;
    private String usageOpened;
    private String usageClosed;

    public AppHistoryData(String packageName, String appName, String usageOpened, String usageClosed) {
        this.packageName = packageName;
        this.appName = appName;
        this.usageOpened = usageOpened;
        this.usageClosed = usageClosed;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getAppName() {
        return appName;
    }

    public String getUsageOpened() {
        return usageOpened;
    }

    public String getUsageClosed() {
        return usageClosed;
    }
}
