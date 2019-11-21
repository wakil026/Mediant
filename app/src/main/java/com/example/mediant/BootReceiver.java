package com.example.mediant;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import java.util.Calendar;

public class BootReceiver extends BroadcastReceiver {

    private String CURRENTUSER = "MediantUserId";
    private String CHANNEL = "Mediant";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED") || intent.getAction().equals("android.intent.action.QUICKBOOT_POWERON"))  {

            // Set the alarm here when device boots

            createNotificationChannel(context);
            String uid = context.getSharedPreferences(CURRENTUSER, Context.MODE_PRIVATE).getString("Uid", "");
            if (uid == "") return;
            SharedPreferences preferences = context.getSharedPreferences(uid, Context.MODE_PRIVATE);
            int size = preferences.getInt("ListSize", 0);
            for (int i = 0; i < size; ++i) {
                int id = preferences.getInt(i + "Id", -1);
                Boolean status = preferences.getBoolean(id + "Status", false);
                if (!status) continue;
                String name = preferences.getString(id + "Name", "");
                String description = preferences.getString(id + "Details", "");
                int times = preferences.getInt(id + "Times", 0);
                for (int j = 0; j < times; ++j) {
                    int time = preferences.getInt(id + "Time" + j, 0);
                    int requestCode = preferences.getInt(id + "RequestCode" + j, 0);
                    Intent intent1 = new Intent(context, AlertReceiver.class);
                    intent1.putExtra("PreferenceId", uid);
                    intent1.putExtra("Position", i);
                    intent1.putExtra("NotificationId", requestCode);
                    intent1.putExtra("Title", name);
                    intent1.putExtra("Message", description);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.HOUR_OF_DAY, time / 60);
                    c.set(Calendar.MINUTE, time % 60);
                    c.set(Calendar.SECOND, 0);
                    if (c.before(Calendar.getInstance())) {
                        c.add(Calendar.DATE, 1);
                    }
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                }

            }


        }

    }

    public void createNotificationChannel(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL, "whatever", NotificationManager.IMPORTANCE_HIGH);
            channel.setSound(null, null);
            channel.enableVibration(true);
            channel.enableLights(true);
            channel.setLightColor(R.color.colorPrimary);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }

    }


}


