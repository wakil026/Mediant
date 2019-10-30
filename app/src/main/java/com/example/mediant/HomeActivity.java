package com.example.mediant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

    Button allmedicine,searchMedicine;
    Button mymedicine;
    Button findAmbulance,allAmbulance;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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
    public void show_allmedicine() {
        Intent intent = new Intent(this, AllMedicineActivity.class);
        startActivity(intent);
    }

    public void show_mymedicine(){
        Intent intent = new Intent(this, MyMedicineActivity.class);
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
            case R.id.adminAboutUs:
                // do your code
                return true;
            case R.id.adminProfileId:
                intent = new Intent(this,ViewProfileActivity.class);
                startActivity(intent);
                return true;
            case R.id.adminShiftMode:
                // do your code
                intent = new Intent(this, HomeAdminActivity.class);
                startActivity(intent);
                return true;
            case R.id.adminSignOut:
               FirebaseAuth.getInstance().signOut();
               finish();
               intent = new Intent(getApplicationContext(),SignInActivity.class);
               startActivity(intent);
               return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
