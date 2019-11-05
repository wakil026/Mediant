package com.example.mediant;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlertReceiver extends BroadcastReceiver {

    private int NOTIFICATION_ID;
    private String message;
    private String title;

    @Override
    public void onReceive(Context context, Intent intent) {

        NOTIFICATION_ID = intent.getIntExtra("notificationID", 0);
        message = intent.getStringExtra("message");
        title = intent.getStringExtra("title");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Mediant")
                .setSmallIcon(R.drawable.ic_alarm_icon)
                .setContentTitle(title)
                .setContentText(message);

        NotificationManagerCompat.from(context).notify(1, builder.build());


    }

}
