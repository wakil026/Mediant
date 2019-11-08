package com.example.mediant;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlertReceiver extends BroadcastReceiver {

    private int notificationId;
    private String message;
    private String title;

    @Override
    public void onReceive(Context context, Intent intent) {

        notificationId = intent.getIntExtra("NotificationId", 0);
        title = intent.getStringExtra("Title");
        message = intent.getStringExtra("Message");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Mediant")
                .setSmallIcon(R.drawable.ic_alarm_icon)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat.from(context).notify(notificationId, builder.build());


    }

}
