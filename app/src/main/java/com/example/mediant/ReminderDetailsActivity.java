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

    private static final String CHANNEL_ID = "channel1";
    private EditText nameEditText;
    private EditText descriptionEditText;
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
                        timeTextView.setText(getTimeInAmPmFormat());
                    }
                }, currentHour, currentMinute, false);

                timePicker.show();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!timeTextView.getText().toString().equals("Select Time")) {
                    timeListInMinute.add(timeHour * 60 + timeMinute);
                    timeList.add(getTimeInAmPmFormat());
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
                String description = descriptionEditText.getText().toString();
                if (name.equals("")) {
                    Toast.makeText(ReminderDetailsActivity.this, "Name Can't Be Empty", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (timeListInMinute.size() == 0) {
                        Toast.makeText(ReminderDetailsActivity.this, "Select at least 1 time", Toast.LENGTH_SHORT).show();
                    }
                    else{

                        SharedPreferences preferences = getSharedPreferences("MyPreference", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        int id = preferences.getInt(position + "Id", -1);
                        if (id == -1) {
                            int maxId = 0;
                            int listSize = preferences.getInt("ListSize", 0);
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
                        editor.putString(id + "Description", description);
                        int oldtimes = preferences.getInt(id + "Times", 0);
                        for (int i = 0; i < oldtimes; ++i) {
                            int requestCode = preferences.getInt(id + "RequestCode" + i, 0);
                            Intent intent = new Intent(ReminderDetailsActivity.this, AlertReceiver.class);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(ReminderDetailsActivity.this, requestCode, intent, 0);
                            alarmManager.cancel(pendingIntent);
                            editor.remove(id + "RequestCode" + i);
                            editor.remove(id + "Time" + i);
                        }
                        editor.apply();
                        int newTimes = timeListInMinute.size();
                        editor.putInt(id + "Times", newTimes);
                        for (int i = 0; i < newTimes; ++i) {
                            int requestCode = id * 1000 + i;
                            editor.putInt(id + "RequestCode" + i, requestCode);
                            int time = timeListInMinute.get(i);
                            editor.putInt(id + "Time" + i, time);
                            Intent intent = new Intent(ReminderDetailsActivity.this, AlertReceiver.class);
                            intent.putExtra("NotificationID", requestCode);
                            intent.putExtra("title", "Mediant");
                            intent.putExtra("message", name);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(ReminderDetailsActivity.this, requestCode, intent, 0);
                            Calendar c = Calendar.getInstance();
                            c.set(Calendar.HOUR_OF_DAY, time / 60);
                            c.set(Calendar.MINUTE, time % 60);
                            c.set(Calendar.SECOND, 0);
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                        }
                        editor.apply();
                        Toast.makeText(ReminderDetailsActivity.this, "Saved", Toast.LENGTH_SHORT).show();

                    }
                }

            }
        });

    }

    public String getTimeInAmPmFormat() {
        if (timeHour == 0) return 12 + ":" + timeMinute + "AM";
        else if (timeHour < 12) return timeHour + ":" + timeMinute + " AM";
        else if (timeHour == 12) return 12 + ":" + timeMinute + "PM";
        else return timeHour - 12 + ":" + timeMinute + "PM";
    }

    public void loadData() {
        SharedPreferences preferences = getSharedPreferences("MyPreference", MODE_PRIVATE);
        if (position == preferences.getInt("ListSize", 0)) {
            return;
        }
        else{
            int id = preferences.getInt(position + "Id", 0);
            nameEditText.setText(preferences.getString(id + "Name", ""));
            descriptionEditText.setText(preferences.getString(id + "Description", ""));
            int times = preferences.getInt(id + "Times", 0);
            for (int i = 0; i < times; ++i) {
                int minutes = preferences.getInt(id + "Time" + i, 0);
                int hour = minutes / 60;
                int minute = minutes % 60;
                if (hour < 12) timeList.add(hour + ":" + minute + " AM");
                else timeList.add(hour + ":" + minute + "PM");
            }
            listAdapter.notifyDataSetChanged();
        }
    }

    public void initialize() {

        position = getIntent().getIntExtra("position", 0);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        nameEditText = findViewById(R.id.nameId);
        descriptionEditText = findViewById(R.id.descriptionId);
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
