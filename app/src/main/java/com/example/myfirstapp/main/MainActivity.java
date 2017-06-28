package com.example.myfirstapp.main;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.myfirstapp.R;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private static final int REQUEST_PERMISSION = 0;
    private static final int REQUEST_SETTING_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onStart() {
        super.onStart();
        checkPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_PERMISSIONS);
            /*for (String permission :
                    packageInfo.requestedPermissions) {
                LogUtil.d("ywd", "permission:" + permission);
            }*/
//                String[] unGrantedPermissions = new String[];
                LinkedList<String> unGrantedPermissions = new LinkedList<>();
                String[] requestedPermissions = packageInfo.requestedPermissions;
                for (int i :
                        packageInfo.requestedPermissionsFlags) {
                    if (i != PackageManager.PERMISSION_GRANTED) {
                        unGrantedPermissions.add(requestedPermissions[i]);
                    }
                }

                requestPermissions(unGrantedPermissions.toArray(new String[unGrantedPermissions.size()]), REQUEST_PERMISSION);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSION:
                for (int i :
                        grantResults) {
                    if (i != PackageManager.PERMISSION_GRANTED) {
                        // 该权限是否被不再询问
//                        shouldShowRequestPermissionRationale()
//                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_SETTING_PERMISSION);
                    }
                }
                break;
        }
    }

    /**
     * Called when the user tabs the send Button
     *
     * @param view the tabbed view
     */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}
