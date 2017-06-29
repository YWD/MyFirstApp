package com.example.myfirstapp.main;

import android.app.Application;

/**
 * Created by ywd on 2017/6/28.
 * MyApp
 */

public class MyApp extends Application {

    private MyApp application;

    @Override
    public void onCreate() {
        super.onCreate();

        application = this;

    }
}
