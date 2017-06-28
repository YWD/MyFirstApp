package com.example.myfirstapp.main;

import android.app.Application;
import android.content.Context;

import com.baidu.mobstat.StatService;
import com.example.myfirstapp.BuildConfig;

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

        initBaiDuMTJ();
    }

    private void initBaiDuMTJ() {
        StatService.setDebugOn(BuildConfig.DEBUG);
        // 自动埋点，建议在Application中调用。否则可能造成部分页面遗漏，无法完整统计。
        // @param autoTrace：如果设置为true，打开自动埋点；反之关闭
        // @param autoTrackWebview：
        // 如果设置为true，则自动track所有webview，如果有对webview绑定WebChromeClient，
        // 为避免影响APP本身回调，请调用trackWebView接口；
        // 如果设置为false，则不自动track webview，如需对特定webview进行统计，需要对特定
        // webview调用trackWebView()即可。
        StatService.autoTrace(application, true, true);
    }

}
