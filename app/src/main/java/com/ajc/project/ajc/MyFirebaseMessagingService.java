package com.ajc.project.ajc;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static android.R.id.message;

/**
 * Created by pierre on 2017-11-10.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService{
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        try {
            String title = remoteMessage.getData().get("title");
            String body = remoteMessage.getData().get("body");

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
            mBuilder.setContentTitle(title);
            mBuilder.setContentText(body);

            // onclick
            Intent notificationIntent = new Intent(this, MainActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent intent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
            mBuilder.setContentIntent(intent);

            mNotificationManager.notify(0, mBuilder.build());
        }catch(Exception e){
            System.err.println("Notification problem");
        }
    }
}
