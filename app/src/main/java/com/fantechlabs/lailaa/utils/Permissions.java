package com.fantechlabs.lailaa.utils;

import android.Manifest;
import android.os.Build;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.fantechlabs.lailaa.R;
import com.google.android.material.snackbar.Snackbar;


//******************************************************************
public class Permissions
//******************************************************************
{
    public static final Permission CAMERA = Permission.fromStrings(Manifest.permission.CAMERA);

    public static final Permission CONTACT = Permission.fromStrings(
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS);

    public static final Permission MICROPHONE = Permission.fromStrings(
            Manifest.permission.RECORD_AUDIO);

    public static final Permission PHONE = Permission.fromStrings(Manifest.permission.CALL_PHONE,
                                                                  Manifest.permission.READ_PHONE_STATE);

    public static final Permission STORAGE = Permission.fromStrings(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE);

    public static final Permission LOCATION = Permission.fromStrings(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION);

    public static final Permission DND = Build.VERSION.SDK_INT >= 23
                                         ? Permission.fromStrings(
            Manifest.permission.ACCESS_NOTIFICATION_POLICY)
                                         : Permission.fromStrings("");


    // ******************************************************************
    public static boolean shouldShowPermissionDeniedSnack(@NonNull String permission)
    // ******************************************************************
    {

        if (AndroidUtil.checkPermission(permission))
            return false;
        return true;
    }

    /**
     * Check if permission is denied, if yes notify the user with a Snackbar.
     * <p>
     * If the permission is denied, it shows a Snackbar with a button which can open system
     * app settings, where the user can grant the permission to the app.
     * If the permission is granted, the permission status cache in Permission.java is cleared as it
     * is obsolete.
     *
     * @param permission the permission to check
     * @param message the message to show if the permission is denied
     * @param action the action to perform before the app settings are opened
     * @param parent the View to which the Snackbar should be attached to
     * @param shouldOpenSettings
     * @param actionLabel
     * @return the Snackbar that if it was displayed, else null.
     */
    // ******************************************************************
    public static Snackbar showPermissionDeniedSnack(@NonNull String permission,
                                                     @StringRes int message,
                                                     @Nullable Runnable action,
                                                     @NonNull View parent,
                                                     boolean shouldOpenSettings,
                                                     @NonNull String actionLabel)
    // ******************************************************************
    {
        // check if the permission is really denied,
        if(shouldShowPermissionDeniedSnack(permission))
        {
            Snackbar snackbar = Snackbar.make(parent,message, Snackbar.LENGTH_INDEFINITE);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(AndroidUtil.getColor(R.color.permission_snackbar));
            snackbar.setActionTextColor(AndroidUtil.getColor(R.color.actionbar_background_color));
            snackbar.setAction(actionLabel, view ->
            {
                if (action != null)
                    action.run();
                if(shouldOpenSettings)
                    UIUtils.showAppSettings();
            });

            snackbar.show();
            return snackbar;
        }
        else
            return null;
    }
}
