package com.aditum.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.aditum.R;

import lombok.NonNull;
import lombok.val;

//*********************************************************
public class LocationUtils
//*********************************************************
{

    private Activity mActivity;
    private Context mContext;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;

    //*********************************************************
    public LocationUtils(Activity mActivity)
    //*********************************************************
    {
        this.mActivity = mActivity;
    }

    public LocationUtils(Context mContext)
    {
        this.mContext = mContext;
    }

    //*********************************************************
    public static com.aditum.utils.LocationUtils getInstance(Activity activity)
    //*********************************************************
    {
        return new com.aditum.utils.LocationUtils(activity);

    }

    //*********************************************************
    public static com.aditum.utils.LocationUtils getInstance(Context context)
    //*********************************************************
    {
        return new com.aditum.utils.LocationUtils(context);
    }

    //**************************************************************************************
    public com.aditum.utils.LocationUtils getCurrentLocation(boolean isLocationUpdate, int minTime,
                                                             int minDistance,
                                                             CurrentLocationListener currentLocationListener)
    //***************************************************************************************
    {
        if (mActivity != null)
        {
            if (!getGPSStatus())
            {
                Intent intent1 = new Intent(
                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mActivity.startActivity(intent1);
                AndroidUtil.toast(false, AndroidUtil.getString(R.string.unable_gps_location));
                return this;
            }
            if (!getPermission())
                return this;
        }
        val context = mActivity == null ? mContext : mActivity;

        mLocationManager = (LocationManager)context.getSystemService(
                Context.LOCATION_SERVICE);

        mLocationListener = new LocationListener()
        {
            @Override
            public void onLocationChanged(Location location)
            {
                Log.i("LOCATION", location.toString());
                val latitude = String.valueOf(
                        location.getLatitude());
                val longitude = String.valueOf(
                        location.getLongitude());
                Log.d("location",
                      "mUserLatitude -> " + latitude);
                Log.d("location",
                      "mUserLongitude -> " + longitude);
                Log.d("location", "location found");
                currentLocationListener.onLocationUpdate(location.getLatitude(),
                                                         location.getLongitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras)
            {
            }

            @Override
            public void onProviderEnabled(String provider)
            {

            }

            @Override
            public void onProviderDisabled(String provider)
            {

            }

        };
        if (mActivity != null && ContextCompat.checkSelfPermission(mActivity,
                                                                   Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(mActivity,
                                              new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                                              1);
        }
        else
        {

            if (mLocationManager != null)
            {
                val location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (isLocationUpdate || location == null)
                {
                    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                                            minTime,
                                                            minDistance,
                                                            mLocationListener);
                    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                                                            minTime,
                                                            minDistance,
                                                            mLocationListener);

                }
                else if (currentLocationListener != null)
                {
                    currentLocationListener.onLastKnownLocation(location.getLatitude(),
                                                                location.getLongitude());
                }

            }
        }
        return this;
    }

    //***********************************************************
    public void stopListener()
    //***********************************************************
    {
        mLocationManager.removeUpdates(mLocationListener);

    }


    //***********************************************************
    private boolean getPermission()
    //***********************************************************
    {
        if ((ActivityCompat.checkSelfPermission(mActivity,
                                                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mActivity,
                                                      Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        )

        {
            ActivityCompat.requestPermissions(mActivity,
                                              new String[] { Manifest.permission.ACCESS_COARSE_LOCATION
                                                      , Manifest.permission.WRITE_CALENDAR,
                                                      Manifest.permission.WAKE_LOCK
                                                      , Manifest.permission.WRITE_EXTERNAL_STORAGE
                                              },
                                              1);
            return false;
        }
        else
        {
            return true;
        }

    }

    //******************************************************************
    public boolean getGPSStatus()
    //******************************************************************
    {
        val mLocationManager = (LocationManager)mActivity.getSystemService(
                Context.LOCATION_SERVICE);
        return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    //*********************************************************
    public static void getDirection(Activity activity, String la1, String ln1, String lat2, String ln2)
    //*********************************************************
    {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                                   Uri.parse(
                                           "http://maps.google.com/maps?saddr=" + la1 + "," + ln1 + "&daddr=" + lat2 + "," + ln2 + ""));
        activity.startActivity(intent);
    }


    //*********************************************************
    public static double distance(double lat1, double lon1, double lat2, double lon2)
    //*********************************************************
    {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    //*********************************************************
    private static double deg2rad(double deg)
    //*********************************************************
    {
        return (deg * Math.PI / 180.0);
    }

    //*********************************************************
    private static double rad2deg(double rad)
    //*********************************************************
    {
        return (rad * 180.0 / Math.PI);
    }

    //**************************************************************
    public static void showDirection(@NonNull Activity activity, @androidx.annotation.NonNull String Latitude, String longitude)
    //**************************************************************
    {

        String longLat = Latitude + "," + longitude;

        Intent intent =
                new Intent(Intent.ACTION_VIEW,
                           Uri.parse("http://maps.google.com/maps?" +
                                             "&daddr=" + longLat));
        activity.startActivity(intent);
    }


    public interface CurrentLocationListener
    {
        void onLastKnownLocation(double latitude, double longitude);

        void onLocationUpdate(double latitude, double longitude);

        void onFailed();

    }
}
