package com.fantechlabs.lailaa.geofence;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.fantechlabs.lailaa.R;
import com.fantechlabs.lailaa.utils.AndroidUtil;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

//***************************************************************
public class GeofenceBroadcastReceiver extends BroadcastReceiver
//***************************************************************
{
    //***************************************************************
    @Override
    public void onReceive(Context context, Intent intent)
    //***************************************************************
    {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if (geofencingEvent.hasError()) {
            return;
        }

        List<Geofence> geofenceList = geofencingEvent.getTriggeringGeofences();
        for (Geofence geofence : geofenceList) {
            geofence.getRequestId();
        }

        int transitionType = geofencingEvent.getGeofenceTransition();

        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                AndroidUtil.toast(false, "GEOFENCE_TRANSITION_ENTER");
                break;
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                AndroidUtil.toast(false, "GEOFENCE_TRANSITION_DWELL");
                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                AndroidUtil.toast(false, "GEOFENCE_TRANSITION_EXIT");
                break;

        }
    }
}