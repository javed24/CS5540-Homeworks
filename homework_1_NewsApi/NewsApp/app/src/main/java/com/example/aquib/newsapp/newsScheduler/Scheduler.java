package com.example.aquib.newsapp.newsScheduler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

/**
 * Created by aquib on 7/27/17.
 */

public class Scheduler {
    private static final int SCHEDULE_INTERVAL_MINUTES = 60;
    private static final int SYNC_FLEXTIME_SECONDS = 60;
    private static final String JOB_TAG = "scheduler_tag";
    private static final String TAG = "Scheduler";

    private static boolean sInitialized;

    //sets a schedule for refreshing the database
    synchronized public static void scheduleRefresh(@NonNull final Context context) {
        //if scheduling has been done already, skip it
        if (sInitialized) return;
        Log.d(TAG, "Scheduler running");
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        Job contraintRefresh = dispatcher.newJobBuilder()
                .setService(ServiceNews.class)
                .setTag(JOB_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(0, 20))
                .setReplaceCurrent(true)
                .build();

        dispatcher.schedule(contraintRefresh);
        sInitialized = true;

    }
}
