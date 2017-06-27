package com.example.myfirstapp.retain_fragment;

import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.myfirstapp.R;

public class TestActivity extends AppCompatActivity {

    private static final String TAG_RETAINED_FRAGMENT = "RetainedFragment";
    private RetainFragment mRetainedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        FragmentManager fm = getFragmentManager();
        // find the retained fragment on activity restarts
        mRetainedFragment = (RetainFragment) fm.findFragmentByTag(TAG_RETAINED_FRAGMENT);
        if (mRetainedFragment == null) {
            mRetainedFragment = new RetainFragment();
            fm.beginTransaction().add(mRetainedFragment, TAG_RETAINED_FRAGMENT).commit();

            // You should never pass an object that is tied to the activity, because it will cause
            // memory leakage.
            mRetainedFragment.setData(loadData());
        }
        // the data is available in mRetainedFragment.getData() even after
        // subsequent configuration change restarts




    }

    @Override
    protected void onPause() {
        super.onPause();
        // This is often used in onPause() to determine whether the activity is simply pausing or
        // completely finishing.
        if (isFinishing()) {
            getFragmentManager().beginTransaction().remove(mRetainedFragment).commit();
        }
    }

    private DataObject loadData() {
        return new DataObject("data");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }
}
