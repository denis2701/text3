package com.example.text3;

public class Task {
    private int id;
    private String type;
    private String time;
    private String appName;

    public Task(int id, String type, String time, String appName) {
        this.id = id;
        this.type = type;
        this.time = time;
        this.appName = appName;
    }

    public Task(String type, String time, String appName) {
        this.type = type;
        this.time = time;
        this.appName = appName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
