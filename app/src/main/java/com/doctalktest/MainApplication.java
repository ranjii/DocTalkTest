package com.doctalktest;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.doctalktest.data.RemoteRetrofitInterfaces;
import com.doctalktest.data.MainFactory;


import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by rajesh on 12/5/17.
 */

public class MainApplication extends Application {
    private RemoteRetrofitInterfaces mainService;
    private Scheduler scheduler;

    private static MainApplication get(Context context) {
        return (MainApplication) context.getApplicationContext();
    }

    public static MainApplication create(Context context) {
        return MainApplication.get(context);
    }

    public RemoteRetrofitInterfaces getPeopleService() {

        if (mainService == null) {
            mainService = MainFactory.create();
        }


        return mainService;
    }

    public Scheduler subscribeScheduler() {
        if (scheduler == null) {
            scheduler = Schedulers.io();
        }

        return scheduler;
    }

    public void setPeopleService(RemoteRetrofitInterfaces peopleService) {
        this.mainService = peopleService;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }
}