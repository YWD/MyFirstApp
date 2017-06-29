package com.example.myfirstapp.base;

import android.app.Activity;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by ywd on 2017/6/29.
 * Activity基类
 */

public class BaseActivity extends Activity {

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
