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
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ReminderActivity extends AppCompatActivity implements ItemClickListener{

    private static final String TAG = "ReminderActivity";
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
    private Long t1, t2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        createNotificationChannel();
        initialize();
        refreshList();

        addReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                Intent intent = new Intent(ReminderActivity.this, ReminderDetailsActivity.class);
                intent.putExtra("PreferenceId", PREFERENCE);
                intent.putExtra("Position", preferences.getInt("ListSize", 0));
                startActivityForResult(intent, 1);
            }
        });
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int pos1 = viewHolder.getAdapterPosition();
                int pos2 = target.getAdapterPosition();
                Collections.swap(reminderList, pos1, pos2);
                int id1 = preferences.getInt(pos1 + "Id",  0);
                int id2 = preferences.getInt(pos2 + "Id", 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt(pos1 + "Id", id2);
                editor.putInt(pos2 + "Id", id1);
                editor.apply();
                adapter.notifyItemMoved(pos1, pos2);
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                final int pos = viewHolder.getAdapterPosition();
                new AlertDialog.Builder(ReminderActivity.this,R.style.AlertDialogStyle)
                        .setTitle("Warning")
                        .setMessage("Delete this item?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                remove(pos);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                adapter.notifyItemChanged(pos);
                            }
                        }).show();
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(ReminderActivity.this, ReminderDetailsActivity.class);
        intent.putExtra("PreferenceId", PREFERENCE);
        intent.putExtra("Position", position);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            refreshList();
        }
    }


    public void remove(int position) {
        reminderList.remove(position);
        adapter.notifyItemRemoved(position);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reminder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.syncId:

                return true;
            case R.id.deleteId:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void importSettings() {
        t1 = System.currentTimeMillis();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.collection("UserReminders").document(uid).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()) {
                            Map<String, ?> mp = documentSnapshot.getData();
                            removeAll();
                            SharedPreferences.Editor editor = preferences.edit();
                            int size = ((Long)mp.get("ListSize")).intValue();
                            editor.putInt("ListSize", size);
                            for (int i = 0; i < size; ++i) {
                                int id = ((Long) mp.get(i + "Id")).intValue();
                                editor.putInt(i + "Id", id);
                                String name = (String) mp.get(id + "Name");
                                editor.putString(id + "Name", name);
                                String details = (String) mp.get(id + "Details");
                                editor.putString(id + "Details", details);
                                Boolean status = (Boolean) mp.get(id + "Status");
                                editor.putBoolean(id + "Status", status);
                                int times = ((Long) mp.get(id + "Times")).intValue();
                                editor.putInt(id + "Times", times);
                                for (int j = 0; j < times; ++j) {
                                    int time = ((Long) mp.get(id + "Time" + j)).intValue();
                                    editor.putInt(id + "Time" + j, time);
                                    int requestCode = ((Long) mp.get(id + "RequestCode" + j)).intValue();
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
                            Toast.makeText(ReminderActivity.this, "Data Imported!", Toast.LENGTH_SHORT).show();
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
        t1 = System.currentTimeMillis();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        preferences = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        Map<String, ?> mp = preferences.getAll();
        db.collection("UserReminders").document(uid).set(mp)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showTime();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public void showTime() {
        t2 = System.currentTimeMillis();
        Toast.makeText(this, "Success! " + (double) (t2 - t1) / 1000 + " sec", Toast.LENGTH_SHORT).show();
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

    public void removeAll() {
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
        editor.apply();
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
            assert notificationManager != null;
            notificationManager.deleteNotificationChannel(CHANNEL);
        }
    }

}

