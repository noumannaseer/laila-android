package com.aditum.network.firebaseNotification;

import com.google.firebase.iid.FirebaseInstanceIdService;

public class GetDeviceTokenService extends FirebaseInstanceIdService
{
    @Override
    public void onTokenRefresh()
    {
        super.onTokenRefresh();
    }
}
