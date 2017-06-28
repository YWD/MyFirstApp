package com.example.myfirstapp.utils;

import android.util.Log;

import com.example.myfirstapp.BuildConfig;

/**
 * Created by ywd on 2017/6/28.
 * 日志工具类
 */

public class LogUtil {

    private static final boolean isDebug = BuildConfig.DEBUG;

    public static int d(String tag, String message) {
        if (isDebug) {
            return Log.d(tag, message);
        }
        return -1;
    }
}
