package com.example.mediant;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;
import java.util.Calendar;

public class ReminderDetailsActivity extends AppCompatActivity {

    private final String TAG = "ReminderDetailsActivity";
    private String PREFERENCE;
    private SharedPreferences preferences;
    private EditText nameEditText;
    private EditText detailsEditText;
    private TextView timeTextView;
    private Button addBtn;
    private Button saveBtn;
    private ListView timeListView;
    private ArrayList<String> timeList = new ArrayList<String>();
    private ArrayList<Integer> timeListInMinute = new ArrayList<>();
    private ArrayAdapter listAdapter;
    private int timeHour;
    private int timeMinute;
    private String timeStr = "Select Time";
    private AlarmManager alarmManager;
    private int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_details);
        initialize();
        loadData();

        timeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePicker = new TimePickerDialog(ReminderDetailsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeHour = hourOfDay;
                        timeMinute = minute;
                        timeTextView.setText(getTimeInAmPmFormat(timeHour * 60 + timeMinute));
                    }
                }, currentHour, currentMinute, false);

                timePicker.show();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!timeTextView.getText().toString().equals("Select Time")) {
                    int timeInMinute = timeHour * 60 + timeMinute;
                    timeListInMinute.add(timeInMinute);
                    timeList.add(getTimeInAmPmFormat(timeInMinute));
                    listAdapter.notifyDataSetChanged();
                    timeTextView.setText("Select Time");
                }

            }
        });

        timeListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                timeList.remove(position);
                listAdapter.notifyDataSetChanged();
                timeListInMinute.remove(position);
                return true;
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String details = detailsEditText.getText().toString();
                if (name.equals("")) {
                    Toast.makeText(ReminderDetailsActivity.this, "Name Can't Be Empty", Toast.LENGTH_SHORT).show();
                }
                else{
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
                    Toast.makeText(ReminderDetailsActivity.this, "Saved", Toast.LENGTH_SHORT).show();


                }

            }
        });

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
        timeTextView = findViewById(R.id.timeTextID);
        addBtn = findViewById(R.id.addTimeButtonID);
        timeListView = findViewById(R.id.listViewID);
        saveBtn = findViewById(R.id.saveButtonID);
        listAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, timeList);
        timeListView.setAdapter(listAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
