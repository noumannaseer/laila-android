package com.fantechlabs.lailaa.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


//**********************************************************************
public class NetworkChangeReceiver
        extends BroadcastReceiver
//**********************************************************************
{
    ConnectionChangeCallback mConnectionChangeCallback;

    //**********************************************************************
    @Override
    public void onReceive(Context context, Intent intent)
    //**********************************************************************
    {
        if (mConnectionChangeCallback != null)
        {
            mConnectionChangeCallback.onConnectionChanged(AndroidUtil.isNetworkStatusAvailable());
        }
    }

    //**********************************************************************
    public void setConnectionChangeCallback(ConnectionChangeCallback callback)
    //**********************************************************************
    {
        mConnectionChangeCallback = callback;
    }
}
