package com.urban.androidhomework;

import android.app.Application;

import com.urban.androidhomework.repository.NetworkClient;

public class UrbanHomeworkApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        NetworkClient.get().setup(getApplicationContext());
    }
}
