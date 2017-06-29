package com.example.myfirstapp.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.example.myfirstapp.R;
import com.example.myfirstapp.utils.LogUtil;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
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

        getDeviceInfo(this);
    }


    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_PERMISSIONS);
            for (String permission :
                    packageInfo.requestedPermissions) {
                LogUtil.d("ywd", "permission:" + permission);
            }
//                String[] unGrantedPermissions = new String[];
                LinkedList<String> unGrantedPermissions = new LinkedList<>();
                String[] requestedPermissions = packageInfo.requestedPermissions;
                /*for (int i :
                        packageInfo.requestedPermissionsFlags) {
                    if (i != PackageManager.PERMISSION_GRANTED) {
                        unGrantedPermissions.add(requestedPermissions[i]);
                    }
                }*/
                for (String permission :
                        packageInfo.requestedPermissions) {
                    if (PackageManager.PERMISSION_GRANTED != checkSelfPermission(permission)) {
                        unGrantedPermissions.add(permission);
                    }
                }

//                requestPermissions(unGrantedPermissions.toArray(new String[unGrantedPermissions.size()]), REQUEST_PERMISSION);
//                unGrantedPermissions.clear();
//                unGrantedPermissions.add("android.permission.GRANT_RUNTIME_PERMISSIONS");
                if (unGrantedPermissions.size() > 0) {
                    requestPermissions(unGrantedPermissions.toArray(new String[unGrantedPermissions.size()]), REQUEST_PERMISSION);
                }

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
                /*if (true) {
                    Intent intent = new Intent("android.intent.action.MANAGE_APP_PERMISSIONS");
                    startActivity(intent);
                }*/
                for (int i = 0; i < permissions.length; i++) {
                    LogUtil.d("ywd", permissions[i]);
                    LogUtil.d("ywd", grantResults[i] + "");
                    LogUtil.d("ywd", "-----------------------------");
                }
                getDeviceInfo(this);
                /*for (int i :
                        grantResults) {
                    if (i != PackageManager.PERMISSION_GRANTED) {
                        // 该权限是否被不再询问
//                        shouldShowRequestPermissionRationale()
//                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
//                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Intent intent = new Intent();
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_SETTING_PERMISSION);
                    }
                }*/
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

    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = null;
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                device_id = tm.getDeviceId();
            }
            String mac = null;
            FileReader fstream = null;
            try {
                fstream = new FileReader("/sys/class/net/wlan0/address");
            } catch (FileNotFoundException e) {
                fstream = new FileReader("/sys/class/net/eth0/address");
            }
            BufferedReader in = null;
            if (fstream != null) {
                try {
                    in = new BufferedReader(fstream, 1024);
                    mac = in.readLine();
                } catch (IOException e) {
                } finally {
                    if (fstream != null) {
                        try {
                            fstream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                if (rest == PackageManager.PERMISSION_GRANTED) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (Exception e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }
}
