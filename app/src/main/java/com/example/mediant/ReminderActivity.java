package com.example.mediant;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ReminderActivity extends AppCompatActivity implements ItemClickListener{

    private static final String CHANNEL_ID = "Mediant";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<ReminderItem> reminderList;
    private FloatingActionButton addReminderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        //SharedPreferences preferences = getSharedPreferences("MyPreference", MODE_PRIVATE);
        //preferences.edit().clear().apply();

        createNotificationChannel();
        initializeRecyclerView();
        refreshList();

        addReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("MyPreference", MODE_PRIVATE);
                Intent intent = new Intent(ReminderActivity.this, ReminderDetailsActivity.class);
                intent.putExtra("position", preferences.getInt("ListSize", 0));
                startActivityForResult(intent, 1);
            }
        });

    }

    public void initializeRecyclerView() {

        reminderList = new ArrayList<>();
        recyclerView = findViewById(R.id.remindersRecyclerId);
        recyclerView.setHasFixedSize(true);
        adapter = new ReminderListAdapter(reminderList, this);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        addReminderButton = findViewById(R.id.addReminderId);
    }

    public void refreshList() {
        reminderList.clear();
        SharedPreferences preferences = getSharedPreferences("MyPreference", MODE_PRIVATE);
        int size = preferences.getInt("ListSize", 0);
        for (int i = 0; i < size; ++i) {
            int id = preferences.getInt(i + "Id", 0);
            String name = preferences.getString(id + "Name", null);
            String description = preferences.getString(id + "Description", null);
            reminderList.add(new ReminderItem(name, description));
        }
        adapter.notifyDataSetChanged();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {

            refreshList();
        }
    }

    @Override
    public void onClick(int position) {
        //Toast.makeText(this, "" + position, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(ReminderActivity.this, ReminderDetailsActivity.class);
        intent.putExtra("position", position);
        startActivityForResult(intent, 1);

    }

    @Override
    public void onLongClick(final int position) {

        // code need to be done

        new AlertDialog.Builder(this)
                .setTitle("Wait")
                .setMessage("Delete Reminder?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        help(position);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();


    }

    public void help(int position) {


        reminderList.remove(position);
        adapter.notifyDataSetChanged();
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        SharedPreferences preferences = getSharedPreferences("MyPreference", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        int id = preferences.getInt(position + "Id", -1);
        editor.remove(id + "Name");
        editor.remove(id + "Description");
        int times = preferences.getInt(id + "Times", 0);
        for (int i = 0; i < times; ++i) {
            int requestCode = preferences.getInt(id + "RequestCode" + i, 0);
            Intent intent = new Intent(ReminderActivity.this, AlertReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(ReminderActivity.this, requestCode, intent, 0);
            alarmManager.cancel(pendingIntent);
            editor.remove(id + "RequestCode" + i);
            editor.remove(id + "Time" + i);
        }
        int listSize = preferences.getInt("ListSize", 0);
        for (int i = position + 1; i < listSize; ++i) {
            editor.putInt(i - 1 + "Id", preferences.getInt(i + "Id", -1));
        }
        editor.remove(listSize - 1 + "Id");
        editor.putInt("ListSize", listSize - 1);
        editor.apply();
        Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();

    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "whatever", NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);
            channel.enableLights(true);
            channel.setLightColor(R.color.colorPrimary);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void deleteNotificationChannel() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.deleteNotificationChannel(CHANNEL_ID);
        }
    }

}

