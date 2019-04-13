package com.example.waterplants;

import android.app.Application;
import android.os.SystemClock;

public class DelayClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SystemClock.sleep(2000);
    }
}
