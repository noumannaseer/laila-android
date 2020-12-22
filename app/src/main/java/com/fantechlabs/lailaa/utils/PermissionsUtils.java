package com.fantechlabs.lailaa.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.view.View;

import androidx.annotation.IntDef;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.fantechlabs.lailaa.R;
import com.google.android.material.snackbar.Snackbar;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import lombok.val;

//***********************************************************************
public class PermissionsUtils
//***********************************************************************
{
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ GRANTED, DENIED, BLOCKED_OR_NEVER_ASKED })
    public @interface PermissionStatus
    {
    }


    public static final int GRANTED = 0;
    public static final int DENIED = 1;
    public static final int BLOCKED_OR_NEVER_ASKED = 2;

    //***********************************************************************
    @PermissionStatus
    public static int getPermissionStatus(Activity activity, String androidPermissionName)
    //***********************************************************************
    {
        if (ContextCompat.checkSelfPermission(activity,
                androidPermissionName) != PackageManager.PERMISSION_GRANTED)
        {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    androidPermissionName))
            {
                return BLOCKED_OR_NEVER_ASKED;
            }
            return DENIED;
        }
        return GRANTED;
    }


    //***********************************************************************
    public static final void showSnackBarWithSettings(String permissionName, View mainView, Activity activity)
    //***********************************************************************
    {
        val snackBar = Snackbar.make(mainView,
                AndroidUtil.getString(R.string.permssion_allow_dialog,
                        permissionName), Snackbar.LENGTH_INDEFINITE);
        snackBar.setAction(R.string.settings, view -> {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
            intent.setData(uri);
            activity.startActivity(intent);
        });

        View snackBarView = snackBar.getView();
        snackBarView.setTranslationY(-(80));
        snackBar.setActionTextColor(activity.getResources()
                .getColor(android.R.color.holo_red_light));
        snackBar.show();
    }
}
