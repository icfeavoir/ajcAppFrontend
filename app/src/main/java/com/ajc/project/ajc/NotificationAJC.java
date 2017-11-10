package com.ajc.project.ajc;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

/**
 * Created by pierre on 2017-11-10.
 */

public class NotificationAJC extends Activity {
    NotificationAJC(Context context, String notifContext, String title, String body) {
        try {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
            mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
            mBuilder.setContentTitle(title);
            mBuilder.setContentText(body);
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(notifContext);
            mNotificationManager.notify(0, mBuilder.build());
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
