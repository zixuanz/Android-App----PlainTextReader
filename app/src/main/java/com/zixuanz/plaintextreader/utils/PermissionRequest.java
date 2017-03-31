package com.zixuanz.plaintextreader.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v13.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.zixuanz.plaintextreader.R;

/**
 * Created by Zixuan Zhao on 9/27/16.
 */

public class PermissionRequest {

    private static final int PERMISSION_READ_EXTERNAL_STORAGE = 1;

    public static void requestPermission(Activity activity){
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {


            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_READ_EXTERNAL_STORAGE);
            }

        }

    }

    public static void requestPermissionResult(Activity activity, int requestCode, String[] permissions, int[] grantResults){
        if (requestCode == PermissionRequest.PERMISSION_READ_EXTERNAL_STORAGE)
        {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
            {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    final Activity activityTemp = activity;
                    new AlertDialog.Builder(activity)
                            .setTitle(R.string.Warning)
                            .setMessage(R.string.permission_explain)
                            .setPositiveButton(R.string.Gotit, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(activityTemp,
                                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_READ_EXTERNAL_STORAGE);
                                }
                            })
                            .create().show();

                }else{
                    activity.finishAffinity();
                }
            }
        }
    }

}
