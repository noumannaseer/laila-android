package com.aditum.network.firebaseNotification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.aditum.R;
import com.aditum.activities.BrowserActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import lombok.NonNull;

import static com.aditum.utils.Constants.REQUEST_CODE;


//****************************************************************************************************
public class MessagingService
        extends FirebaseMessagingService
//****************************************************************************************************
{

    public static final String NOTIFICATION_CHANNEL_ID = "999";
    public static final String NOTIFICATION_CHANNEL_NAME = "NOTIFICATION_CHANNEL_NAME";


    //****************************************************************************************************
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    //****************************************************************************************************
    {

        if (remoteMessage.getNotification() != null)
            getNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle());
    }


    //****************************************************************
    private void getNotification(@NonNull String body, @NonNull String title)
    //****************************************************************
    {
            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
//            Intent repeatingIntent = new Intent(this, HomeActivity.class);
//            //repeatingIntent.putExtra(DashBoardActivity.IS_NOTIFICATION, true);
//            repeatingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Intent browserIntent = new Intent(this, BrowserActivity.class);
            browserIntent.putExtra(BrowserActivity.SCREEN_URL,getString(R.string.terms_conditions_url));
            browserIntent.putExtra(BrowserActivity.INFO_TITLE,getString(R.string.settings_terms_and_conditions));
            //startActivity(browserIntent);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE, browserIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.mipmap.logo)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(true);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
            {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, importance);

                builder.setChannelId(NOTIFICATION_CHANNEL_ID);
                notificationManager.createNotificationChannel(notificationChannel);
            }

            notificationManager.notify(REQUEST_CODE, builder.build());

    }

}