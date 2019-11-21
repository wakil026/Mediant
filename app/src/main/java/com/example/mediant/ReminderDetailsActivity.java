package com.example.mediant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class ReminderDetailsActivity extends AppCompatActivity implements ItemClickListener{

    private final String TAG = "ReminderDetailsActivity";
    private String PREFERENCE;
    private SharedPreferences preferences;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EditText nameEditText;
    private EditText detailsEditText;
    private Button addTimeBtn;
    private Button saveBtn;
    private RecyclerView listView;
    private RecyclerView.Adapter listAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<String> timeList;
    private ArrayList<Integer> timeListInMinute;
    private AlarmManager alarmManager;
    private int position;
    private Long t1, t2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_details);

        initialize();
        loadData();

        addTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePicker = new TimePickerDialog(ReminderDetailsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        int timeInMinute = hourOfDay * 60 + minute;
                        timeListInMinute.add(timeInMinute);
                        timeList.add(getTimeInAmPmFormat(timeInMinute));
                        listAdapter.notifyDataSetChanged();
                    }
                }, currentHour, currentMinute, false);
                timePicker.show();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();

            }
        });
    }

    public void save() {
        String name = nameEditText.getText().toString();
        String details = detailsEditText.getText().toString();
        if (name.equals("")) {
            Toast.makeText(ReminderDetailsActivity.this, "Name Can't Be Empty", Toast.LENGTH_SHORT).show();
        }
        else {
            SharedPreferences.Editor editor = preferences.edit();
            int id = preferences.getInt(position + "Id", -1);
            int listSize = preferences.getInt("ListSize", 0);
            if (id == -1) {
                int maxId = 0;
                for (int i = 0; i < listSize; ++i) {
                    int curr = preferences.getInt(i + "Id", -1);
                    if (curr > maxId) {
                        maxId = curr;
                    }
                }
                id = maxId + 1;
                editor.putInt(position + "Id", id);
                editor.putInt("ListSize", listSize + 1);
            }
            editor.putString(id + "Name", name);
            editor.putString(id + "Details", details);

            int oldtimes = preferences.getInt(id + "Times", 0);
            for (int i = 0; i < oldtimes; ++i) {
                int requestCode = preferences.getInt(id + "RequestCode" + i, 0);
                Intent intent = new Intent(ReminderDetailsActivity.this, AlertReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(ReminderDetailsActivity.this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
                pendingIntent.cancel();
                editor.remove(id + "RequestCode" + i);
                editor.remove(id + "Time" + i);
            }

            int newTimes = timeListInMinute.size();
            editor.putInt(id + "Times", newTimes);
            for (int i = 0; i < newTimes; ++i) {
                int requestCode = id * 1000 + i;
                editor.putInt(id + "RequestCode" + i, requestCode);
                int time = timeListInMinute.get(i);
                editor.putInt(id + "Time" + i, time);
                Intent intent = new Intent(ReminderDetailsActivity.this, AlertReceiver.class);
                intent.putExtra("NotificationId", requestCode);
                intent.putExtra("Title", name);
                intent.putExtra("Message", details);
                intent.putExtra("PreferenceId", PREFERENCE);
                intent.putExtra("Position", position);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(ReminderDetailsActivity.this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                Calendar c = Calendar.getInstance();
                c.set(Calendar.HOUR_OF_DAY, time / 60);
                c.set(Calendar.MINUTE, time % 60);
                c.set(Calendar.SECOND, 0);
                if (c.before(Calendar.getInstance())) {
                    c.add(Calendar.DATE, 1);
                }
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                //alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);                                             // for testing purposes
            }
            editor.putBoolean(id + "Status", true);
            editor.apply();
            Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
            t1 = System.currentTimeMillis();
            preferences = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
            Map<String, ?> mp = preferences.getAll();
            db.collection("UserReminders").document(PREFERENCE).set(mp)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }
    }

    public String getTimeInAmPmFormat(int timeInMinute) {
        int hour = timeInMinute / 60;
        int minute = timeInMinute % 60;
        String sMinute;
        if (minute < 10) sMinute = "0" + minute;
        else sMinute = "" + minute;
        if (hour == 0) return 12 + ":" + sMinute + " AM";
        else if (hour < 12) return hour + ":" + sMinute + " AM";
        else if (hour == 12) return 12 + ":" + sMinute + " PM";
        else return hour - 12 + ":" + sMinute + " PM";
    }

    public void loadData() {
        if (position == preferences.getInt("ListSize", 0)) {
            return;
        }
        else{
            int id = preferences.getInt(position + "Id", 0);
            nameEditText.setText(preferences.getString(id + "Name", ""));
            detailsEditText.setText(preferences.getString(id + "Details", ""));
            int times = preferences.getInt(id + "Times", 0);
            for (int i = 0; i < times; ++i) {
                int minutes = preferences.getInt(id + "Time" + i, 0);
                timeListInMinute.add(minutes);
                timeList.add(getTimeInAmPmFormat(minutes));
            }
            listAdapter.notifyDataSetChanged();
        }
    }

    public void initialize() {
        PREFERENCE = getIntent().getStringExtra("PreferenceId");
        preferences = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
        position = getIntent().getIntExtra("Position", 0);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        nameEditText = findViewById(R.id.nameId);
        detailsEditText = findViewById(R.id.detailsId);
        addTimeBtn = findViewById(R.id.addTimeButtonId);
        saveBtn = findViewById(R.id.saveButtonId);
        timeList = new ArrayList<>();
        timeListInMinute = new ArrayList<>();
        listView = findViewById(R.id.timeListId);
        listView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(layoutManager);
        listAdapter = new TimeListAdapter(timeList, this);
        listView.setAdapter(listAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(int position) {
        timeList.remove(position);
        timeListInMinute.remove(position);
        listAdapter.notifyItemRemoved(position);
    }

    @Override
    public void onChecked(int position, Boolean status) {

    }
}
