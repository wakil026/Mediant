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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ReminderActivity extends AppCompatActivity implements ItemClickListener{

    private final String TAG = "ReminderActivity";
    private static final String CHANNEL = "Mediant";
    private String PREFERENCE;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<ReminderItem> reminderList;
    private FloatingActionButton addReminderButton;
    private SharedPreferences preferences;
    private AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        //SharedPreferences preferences = getSharedPreferences("MyPreference", MODE_PRIVATE);
        //preferences.edit().clear().apply();
        createNotificationChannel();
        initialize();
        refreshList();

        addReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                Intent intent = new Intent(ReminderActivity.this, ReminderDetailsActivity.class);
                intent.putExtra("PreferenceId", PREFERENCE);
                intent.putExtra("position", preferences.getInt("ListSize", 0));
                startActivityForResult(intent, 1);
            }
        });
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
        Intent intent = new Intent(ReminderActivity.this, ReminderDetailsActivity.class);
        intent.putExtra("PreferenceId", PREFERENCE);
        intent.putExtra("Position", position);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onLongClick(final int position) {
        new AlertDialog.Builder(this)
                .setTitle("Warning")
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

    @Override
    public void onChecked(int position, Boolean status) {
        if (status) {
            int id = preferences.getInt(position + "Id", -1);
            int times = preferences.getInt(id + "Times", 0);
            String name = preferences.getString(id + "Name", "");
            String details = preferences.getString(id + "Details", "");
            for (int i = 0; i < times; ++i) {
                int time = preferences.getInt(id + "Time" + i, -1);
                int requestCode = preferences.getInt(id + "RequestCode" + i, -1);
                Intent intent = new Intent(this, AlertReceiver.class);
                intent.putExtra("NotificationId", requestCode);
                intent.putExtra("Title", name);
                intent.putExtra("Message", details);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
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
        else {
            int id = preferences.getInt(position + "Id", -1);
            int times = preferences.getInt(id + "Times", 0);
            for (int i = 0; i < times; ++i) {
                int requestCode = preferences.getInt(id + "RequestCode" + i, -1);
                Intent intent = new Intent(this, AlertReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
                pendingIntent.cancel();
            }
        }
        int id = preferences.getInt(position + "Id", -1);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(id + "Status", status);
        editor.apply();
    }

    public void help(int position) {
        reminderList.remove(position);
        adapter.notifyDataSetChanged();
        SharedPreferences.Editor editor = preferences.edit();
        int id = preferences.getInt(position + "Id", -1);  // it has to exist so -1 can't come
        editor.remove(id + "Name");
        editor.remove(id + "Details");
        editor.remove(id + "Status");
        int times = preferences.getInt(id + "Times", 0);
        for (int i = 0; i < times; ++i) {
            int requestCode = preferences.getInt(id + "RequestCode" + i, 0);
            Intent intent = new Intent(ReminderActivity.this, AlertReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
            editor.remove(id + "RequestCode" + i);
            editor.remove(id + "Time" + i);
        }
        editor.remove(id + "Times");
        int listSize = preferences.getInt("ListSize", 0);
        for (int i = position + 1; i < listSize; ++i) {
            editor.putInt(i - 1 + "Id", preferences.getInt(i + "Id", -1));
        }
        editor.remove(listSize - 1 + "Id");
        editor.putInt("ListSize", listSize - 1);
        editor.apply();
        Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reminder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.importId:
                new AlertDialog.Builder(this)
                        .setTitle("Warning")
                        .setMessage("Import settings From online account? It'll remove current settings!")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                importSettings();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();

                return true;
            case R.id.uploadId:
                new AlertDialog.Builder(this)
                        .setTitle("Warning")
                        .setMessage("Upload settings into online account? It'll remove current settings!")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                uploadSettings();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void importSettings() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("UserReminders").document(uid).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()) {
                            Toast.makeText(ReminderActivity.this, "Data Imported!", Toast.LENGTH_SHORT).show();
                            Gson gson = new Gson();
                            String json = gson.toJson(documentSnapshot.getData().get("all"));
                            Type type = new TypeToken<HashMap<String, Object>>(){}.getType();
                            HashMap<String, Object> mp = gson.fromJson(json, type);
                            removeAllAlarmsAndClearPreference();
                            SharedPreferences.Editor editor = preferences.edit();
                            int size = ((Double)mp.get("ListSize")).intValue();
                            editor.putInt("ListSize", size);
                            for (int i = 0; i < size; ++i) {
                                int id = ((Double)mp.get(i + "Id")).intValue();
                                editor.putInt(i + "Id", id);
                                String name = (String) mp.get(id + "Name");
                                editor.putString(id + "Name", name);
                                String details = (String) mp.get(id + "Details");
                                editor.putString(id + "Details", details);
                                Boolean status = (Boolean) mp.get(id + "Status");
                                editor.putBoolean(id + "Status", status);
                                int times = ((Double) mp.get(id + "Times")).intValue();
                                editor.putInt(id + "Times", times);
                                for (int j = 0; j < times; ++j) {
                                    int time = ((Double) mp.get(id + "Time" + j)).intValue();
                                    editor.putInt(id + "Time" + j, time);
                                    int requestCode = ((Double)mp.get(id + "RequestCode" + j)).intValue();
                                    editor.putInt(id + "RequestCode" + j, requestCode);
                                    Intent intent = new Intent(ReminderActivity.this, AlertReceiver.class);
                                    intent.putExtra("NotificationId", requestCode);
                                    intent.putExtra("Title", name);
                                    intent.putExtra("Message", details);
                                    PendingIntent pendingIntent = PendingIntent.getBroadcast(ReminderActivity.this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
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
                            editor.apply();
                            refreshList();
                            Toast.makeText(ReminderActivity.this, "Data Imported Successfully!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ReminderActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void uploadSettings() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("UserReminders").document(uid).set(getSharedPreferences(PREFERENCE, MODE_PRIVATE))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ReminderActivity.this, "Data uploaded!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ReminderActivity.this, "Error uploading data!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void initialize() {

        PREFERENCE = FirebaseAuth.getInstance().getCurrentUser().getUid();
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        preferences = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
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
        int size = preferences.getInt("ListSize", 0);
        for (int i = 0; i < size; ++i) {
            int id = preferences.getInt(i + "Id", 0);
            String name = preferences.getString(id + "Name", "");
            String description = preferences.getString(id + "Details", "");
            Boolean status = preferences.getBoolean(id + "Status", false);
            reminderList.add(new ReminderItem(name, description, status));
        }
        adapter.notifyDataSetChanged();
    }

    public void removeAllAlarmsAndClearPreference() {
        SharedPreferences.Editor editor = preferences.edit();
        int size = preferences.getInt("ListSize", 0);
        for (int i = 0; i < size; ++i) {
            int id = preferences.getInt(i + "Id", -1);
            editor.remove(id + "Id");
            editor.remove(id + "Name");
            editor.remove(id + "Details");
            editor.remove(id + "Status");
            int oldtimes = preferences.getInt(id + "Times", 0);
            editor.remove(id + "Times");
            for (int j = 0; j < oldtimes; ++j) {
                int requestCode = preferences.getInt(id + "RequestCode" + j, 0);
                Intent intent = new Intent(ReminderActivity.this, AlertReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(ReminderActivity.this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
                pendingIntent.cancel();
                editor.remove(id + "RequestCode" + i);
                editor.remove(id + "Time" + i);
            }
        }
    }

    public void setAlarms() {

    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL, "whatever", NotificationManager.IMPORTANCE_HIGH);
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
            notificationManager.deleteNotificationChannel(CHANNEL);
        }
    }

}

