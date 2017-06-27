package com.example.myfirstapp.retain_fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by ywd on 2017/6/27.
 * RetainFragment
 */

public class RetainFragment extends Fragment {

    // data object we want to retain
    private DataObject mDataObject;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // retain this fragment
        setRetainInstance(true);
    }

    public void setData(DataObject dataObject) {
        this.mDataObject = dataObject;
    }

    public DataObject getDataObject() {
        return mDataObject;
    }
}
