package com.fantechlabs.lailaa.utils.permissions;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.fantechlabs.lailaa.utils.AndroidUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

//******************************************************************
public class Permission
//******************************************************************
{
    public static final String TAG = Permission.class.getCanonicalName();
    private static final HashMap<String, ArrayList<OnDenied>> sOnDenied = new HashMap<>();
    private final String[] mPermissionStrings;
    private static Fragment mFragment;
    private static Activity mActivity;

    //******************************************************************
    private Permission(@NonNull String[] permissions)
    //******************************************************************
    {
        mPermissionStrings = permissions;
    }


    public Permission(String[] mPermissionStrings, Activity mActivity)
    {
        this.mPermissionStrings = mPermissionStrings;
        this.mActivity = mActivity;
        mFragment = null;
        API = Build.VERSION.SDK_INT >= 23
              ? new Api23(mFragment, mActivity)
              : new Api21();
    }

    public Permission(String[] mPermissionStrings, Fragment mFragment)
    {
        this.mPermissionStrings = mPermissionStrings;
        this.mFragment = mFragment;
        mActivity = null;
        API = Build.VERSION.SDK_INT >= 23
              ? new Api23(mFragment, mActivity)
              : new Api21();
    }

    public void permissionGranted(String permissionName, String inputPermission, Status status)
    {

    }

    //******************************************************************
    public final @NonNull
    String[] getPermissionStrings()
    //******************************************************************
    {
        return mPermissionStrings;
    }

    //******************************************************************
    public boolean isGranted()
    //******************************************************************
    {
        for (String permission : mPermissionStrings)
            if (!AndroidUtil.checkPermission(permission))
                return false;
        return true;
    }

    //******************************************************************
    @Override
    public boolean equals(Object o)
    //******************************************************************
    {
        return o instanceof Permission
                && Arrays.equals(mPermissionStrings, ((Permission)o).getPermissionStrings());
    }

    //******************************************************************
    @Override
    public int hashCode()
    //******************************************************************
    {
        return Arrays.hashCode(mPermissionStrings);
    }

    //******************************************************************
    @Override
    public String toString()
    //******************************************************************
    {
        return Arrays.toString(mPermissionStrings);
    }

    //******************************************************************
    @MainThread
    public void request(@Nullable OnResult onResult)
    //******************************************************************
    {
        for (String permission : mPermissionStrings)
            API.request(permission, onResult);
    }

    //******************************************************************
    public static @NonNull
    Permission fromStrings(@NonNull String... permissions)
    //******************************************************************
    {
        return new Permission(permissions);
    }

    /**
     * Interface for callback when grant permission was received.
     */
    //******************************************************************
    public interface OnResult
            //******************************************************************
    {
        /**
         * Permission result.
         *
         * @param permission Permission that was requested.
         * @param status     Result of the request.
         */
        //******************************************************************
        @MainThread
        void onPermission(@NonNull String permission,
                          @NonNull Status status);
        //******************************************************************
    }


    //******************************************************************
    public interface OnDenied
            //******************************************************************
    {
        //******************************************************************
        @MainThread
        void onPermissionDenied(@NonNull String permission);
        //******************************************************************
    }

    //******************************************************************
    @MainThread
    public static void register(@NonNull OnDenied denied,
                                @NonNull String permission,
                                boolean notifyNow)
    //******************************************************************
    {
        ArrayList<OnDenied> deniedList = sOnDenied.get(permission);
        if (deniedList == null)
            deniedList = new ArrayList<>();
        deniedList.add(denied);
        sOnDenied.put(permission, deniedList);

        if (notifyNow && !check(permission))
            denied.onPermissionDenied(permission);
    }

    //******************************************************************
    @MainThread
    public static void unregister(@NonNull OnDenied listener,
                                  @NonNull String permission)
    //******************************************************************
    {
        ArrayList<OnDenied> deniedList = sOnDenied.get(permission);
        if (deniedList != null)
            deniedList.remove(listener);
    }

    //******************************************************************
    @MainThread
    public static void unregister(@NonNull OnDenied listener,
                                  @NonNull String... permissions)
    //******************************************************************
    {
        for (final String permission : permissions)
            unregister(listener, permission);
    }

    // ******************************************************************
    private static void notifyDenied(@NonNull String permission)
    // ******************************************************************
    {
        ArrayList<OnDenied> onPermissionDenied = sOnDenied.get(permission);
        if (onPermissionDenied != null)
            for (final OnDenied listener : onPermissionDenied)
                listener.onPermissionDenied(permission);
    }


    /**
     * Permission status.
     */
    //******************************************************************
    public enum Status
            //******************************************************************
    {
        /**
         * Granted.
         */
        Granted,

        /**
         * Denied for now but you may ask again.
         */
        DeniedForNow,

        /**
         * Denied permanently. The user has to go to settings to grant the permission.
         */
        DeniedPermanently,
        ;

        /**
         * Check whether the permission was granted.
         *
         * @return Whether the permission was granted.
         */
        //******************************************************************
        public boolean isGranted()
        //******************************************************************
        {
            switch (this)
            {
            case Granted:
                return true;
            }
            return false;
        }

        /**
         * Check whether this status is permanent.
         *
         * @return Whether this status is permanent.
         */
        //******************************************************************
        public boolean isPermanent()
        //******************************************************************
        {
            switch (this)
            {
            case Granted:
            case DeniedPermanently:
                return true;
            }
            return false;
        }
    }


    /**
     * API 19.
     */
    //******************************************************************
    private static class Api21
            //******************************************************************
    {
        /**
         * Handle result of asynchronous request.
         *
         * @param permission Permission that was requested.
         * @param result     Result of the request.
         */
        //******************************************************************
        public void handleResult(@NonNull String permission,
                                 int result)
        //******************************************************************
        {
            Log.e(TAG, "handleResult: Unexpected asynchronous result for permission " + permission);
        }

        /**
         * Request the permission.
         * <p>
         * The callback is called synchronously if the permission was already
         * granted or denied.
         *
         * @param permission String permission to request.
         * @param callback   Callback when the request is handled.
         */
        //******************************************************************
        public void request(@NonNull String permission,
                            @Nullable OnResult callback)
        //******************************************************************
        {
            final Status status = AndroidUtil.checkPermission(permission) ?
                                  Status.Granted : Status.DeniedPermanently;
            if (callback != null)
                callback.onPermission(permission, status);
        }

        //******************************************************************
        public void onRequestPermissionResult(int requestCode,
                                              String[] permissions,
                                              int[] results)
        //******************************************************************
        {
            Log.e(TAG, "Unexpected asynchronous result for permission %s " + Arrays.toString(
                    permissions));
        }

        //******************************************************************
        public boolean shouldShowRationale(String permission, Activity activity)
        //******************************************************************
        {
            return false;
        }
    }


    /**
     * API 23.
     */
    //******************************************************************
    @TargetApi(23)
    private static class Api23
            extends Api21
            //******************************************************************
    {
        // whether we have scheduled the next permission group request
        private static boolean sRequestScheduled = false;

        // list of permission groups to requests in the future
        private static HashSet<String> sPermissionsToRequest = new HashSet<>();

        // used to match permission request results with our permission groups
        private static int REQUEST_CODE = 0;

        private static HashMap<String, ArrayList<OnResult>> sPermissionCallbacks = new HashMap<>();

        private Fragment mFragment;
        private Activity mActivity;

        public Api23(@Nullable Fragment mFragment, @Nullable Activity mActivity)
        {
            this.mFragment = mFragment;
            this.mActivity = mActivity;

        }

        /**
         * {@inheritDoc}
         */
        //******************************************************************
        @Override
        public void handleResult(@NonNull String permission,
                                 int result)
        //******************************************************************
        {
            final Context ctx = AndroidUtil.getContext();
            if (!(ctx instanceof Activity))
            {
                Log.e(TAG, "Can't handle permission result without activity");
                return;
            }
            final Activity activity = (Activity)ctx;
            final Status status = result == PackageManager.PERMISSION_GRANTED
                                  ? Status.Granted
                                  : shouldShowPermissionRationale(permission, activity)
                                    ? Status.DeniedForNow
                                    : Status.DeniedPermanently;

            if (status != Status.Granted)
                Log.w(TAG,
                      "Permission " + (status == Status.DeniedPermanently ? " permanently" : "") + " permanently " + permission);

            invokeCallbacks(permission, status);
            sPermissionCallbacks.remove(permission);
        }

        //******************************************************************
        @Override
        public void onRequestPermissionResult(int requestCode,
                                              @NonNull String[] permissions,
                                              @NonNull int[] results)
        //******************************************************************
        {
            for (int i = 0; i < permissions.length; i++)
                handleResult(permissions[i], results[i]);

            // if more permissions are waiting for request, schedule the next one
            if (sPermissionsToRequest.size() > 0)
                AndroidUtil.handler.post(this::requestPermissions);
            else
                sRequestScheduled = false;
        }

        /**
         * {@inheritDoc}
         */
        //******************************************************************
        @Override
        public void request(@NonNull String permission,
                            @Nullable OnResult callback)
        //******************************************************************
        {
            /* Check always, the permission may have been granted while
             * we were in the background. */
            if (check(permission))
            {
                if (callback != null)
                    callback.onPermission(permission, Status.Granted);
                return;
            }

            ArrayList<OnResult> list = sPermissionCallbacks.get(permission);
            if (list == null)
            {
                list = new ArrayList<>();
                sPermissionCallbacks.put(permission, list);
            }
            if (callback != null)
                list.add(callback);
            sPermissionsToRequest.add(permission);
            /* No permission group request is currently being processed or scheduled,
             * schedule the next one.
             *
             * Small delay is necessary to group requests from onCreate, onStart etc. */
            if (!sRequestScheduled)
            {
                sRequestScheduled = true;
                AndroidUtil.handler.postDelayed(this::requestPermissions, 400);
            }
        }

        /**
         * Request a group of permissions
         */
        //******************************************************************
        public void requestPermissions()
        //******************************************************************
        {
            Context ctx = AndroidUtil.getContext();
          /*  if (!(ctx instanceof Activity))
            {
                Log.w(TAG,
                      "Activity context required to process permission request. Request ignored.");
                return;
            }*/

            sRequestScheduled = false;

//            Activity activity = (Activity)ctx;

            ArrayList<String> denied = new ArrayList<>();
            ArrayList<String> alreadyGranted = new ArrayList<>();

            for (String permission : sPermissionsToRequest)
            {
                if (check(permission))
                    alreadyGranted.add(permission);
                else
                {
                    denied.add(permission);
                }
            }
            sPermissionsToRequest.clear();
            for (final String granted : alreadyGranted)
            {
                invokeCallbacks(granted, Status.Granted);
                sPermissionCallbacks.remove(granted);
            }

            for (final String granted : denied)
            {
                invokeCallbacks(granted, Status.DeniedForNow);
                sPermissionCallbacks.remove(granted);
            }


            if (denied.size() > 0)
            {

                if (mFragment != null)
                {
                    mFragment.requestPermissions(denied.toArray(new String[denied.size()]),
                                                 REQUEST_CODE);
                }
                else
                    ActivityCompat.requestPermissions(mActivity,
                                                      denied.toArray(new String[denied.size()]),
                                                      REQUEST_CODE);
                REQUEST_CODE++;
            }
            else
                sRequestScheduled = false;
        }

        //******************************************************************
        @Override
        public boolean shouldShowRationale(@NonNull String permission,
                                           @NonNull Activity activity)
        //******************************************************************
        {
            return activity.shouldShowRequestPermissionRationale(permission);
        }

        //******************************************************************
        private void invokeCallbacks(@NonNull String permission, @NonNull Status status)
        //******************************************************************
        {
            ArrayList<OnResult> callbacks = sPermissionCallbacks.get(permission);
            if (callbacks != null)
                for (final OnResult callback : callbacks)
                    callback.onPermission(permission, status);
            if (!status.isGranted())
                notifyDenied(permission);
            else if (status.isPermanent())
            {

            }

        }
    }


    /**
     * API-specific methods.
     */
    private static Api21 API;


    /**
     * Request the permission.
     * <p>
     * The callback is called synchronously if the permission was already
     * granted or denied.
     *
     * @param permission permission to request.
     * @param onResult   Callback when the request is handled.
     */
    // ******************************************************************
    @MainThread
    public static void request(@NonNull String permission,
                               @Nullable OnResult onResult)
    // ******************************************************************
    {
        API.request(permission, onResult);
    }

    /**
     * Check a permission status.
     *
     * @return Whether the permission was granted.
     */
    //******************************************************************
    @MainThread
    public static boolean check(@NonNull String permission)
    //******************************************************************
    {
        return AndroidUtil.checkPermission(permission);
    }

    //******************************************************************
    @MainThread
    public static void onRequestPermissionResult(int requestCode,
                                                 @NonNull String[] permissions,
                                                 @NonNull int[] results)
    //******************************************************************
    {
        API.onRequestPermissionResult(requestCode, permissions, results);
    }

    //******************************************************************
    @MainThread
    public static boolean shouldShowPermissionRationale(@NonNull String permission,
                                                        @NonNull Activity activity)
    //******************************************************************
    {
        return API.shouldShowRationale(permission, activity);
    }

    //**********************************************************************
    public static List<String> getManifestPermissions()
    //**********************************************************************
    {
        ArrayList<String> permissionList = new ArrayList<String>();
        try
        {
            Context context = AndroidUtil.getContext();
            PackageInfo info = context.getPackageManager()
                                      .getPackageInfo(context.getPackageName(),
                                                      PackageManager.GET_PERMISSIONS);
            permissionList.addAll(Arrays.asList(info.requestedPermissions));
        }
        catch (PackageManager.NameNotFoundException nnfe)
        {
            Log.e(TAG, "getManifestPermissions: " + nnfe.getLocalizedMessage());
        }

        return permissionList;
    }
}
