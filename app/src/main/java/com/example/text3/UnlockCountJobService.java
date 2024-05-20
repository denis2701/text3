package com.example.text3;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;

public class UnlockCountJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        Intent service = new Intent(getApplicationContext(), UnlockCountService.class);
        startService(service);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}