package com.example.mediant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeActivity extends AppCompatActivity {

    private String CURRENTUSER = "MediantUserId";

    Button allmedicine,searchMedicine;
    Button mymedicine;
    Button findAmbulance,allAmbulance;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setTitle("Home");
        allmedicine = findViewById(R.id.button_allmedicine);
        mymedicine = findViewById(R.id.button_mymedicine);
        findAmbulance = findViewById(R.id.button_findambu);
        allAmbulance = findViewById(R.id.button_allAmbulance);
        searchMedicine = findViewById(R.id.button_searchMedicine);


        allmedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_allmedicine();
            }
        });

        searchMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_medicine();
            }
        });

        mymedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_mymedicine();
            }
        });

        findAmbulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_ambulance();
            }
        });

        allAmbulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                all_ambulance();
            }
        });

    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this,R.style.AlertDialogStyle);
        builder.setTitle("Warning");
        builder.setMessage("Do you want to quit Mediant?");
        builder.setCancelable(true);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("Quit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void show_allmedicine() {
        Intent intent = new Intent(this, AllMedicineActivity.class);
        startActivity(intent);
    }

    public void show_mymedicine(){
        Intent intent = new Intent(this, ReminderActivity.class);
        startActivity(intent);
    }

    public void search_medicine(){
        Intent intent = new Intent(this, SearchMedicineActivity.class);
        startActivity(intent);
    }

    public void search_ambulance(){
        Intent intent = new Intent(this, SearchAmbulanceActivity.class);
        startActivity(intent);
    }

    public void all_ambulance(){
        Intent intent = new Intent(this, AllAmbulanceActivity.class);
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu1) {
        //Toast.makeText(this, ""+SignInActivity.type, Toast.LENGTH_SHORT).show();
        String type = SignInActivity.type;
        if(type.equals("Admin")) {
            getMenuInflater().inflate(R.menu.menu_layout, menu1);
            return true;
        }
        else{
            getMenuInflater().inflate(R.menu.user_menu_layout, menu1);
            return true;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about_us:
                // do your code
                return true;
            case R.id.profileId:
                intent = new Intent(this,ViewProfileActivity.class);
                startActivity(intent);
                return true;
            case R.id.adminShiftMode:
                // do your code
                intent = new Intent(this, HomeAdminActivity.class);
                startActivity(intent);
                return true;
            case R.id.sign_out:
               FirebaseAuth.getInstance().signOut();
               // unsetting reminders
                removeReminders();

               finish();
               intent = new Intent(getApplicationContext(),SignInActivity.class);
               startActivity(intent);
               return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void removeReminders() {
        String uid = getSharedPreferences(CURRENTUSER, MODE_PRIVATE).getString("Uid", "");
        if (uid == "") return;
        SharedPreferences preferences = getSharedPreferences(uid, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
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
                Intent intent = new Intent(HomeActivity.this, AlertReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(HomeActivity.this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
                pendingIntent.cancel();
                editor.remove(id + "RequestCode" + i);
                editor.remove(id + "Time" + i);
            }
        }
        editor.apply();
    }

}
