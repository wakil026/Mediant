package com.example.mediant;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlertReceiver extends BroadcastReceiver {

    private String PREFERENCE;
    private int notificationId;
    private String message;
    private String title;
    private int position;

    @Override
    public void onReceive(Context context, Intent intent) {

        notificationId = intent.getIntExtra("NotificationId", 0);
        position = intent.getIntExtra("Position", 0);
        PREFERENCE = intent.getStringExtra("PreferenceId");
        title = intent.getStringExtra("Title");
        message = intent.getStringExtra("Message");
        Intent intent1 = new Intent(context, ReminderDetailsActivity.class);
        intent1.putExtra("PreferenceId", PREFERENCE);
        intent1.putExtra("Position", position);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notificationId, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Mediant")
                .setSmallIcon(R.drawable.ic_alarm_icon)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat.from(context).notify(notificationId, builder.build());


    }

}
