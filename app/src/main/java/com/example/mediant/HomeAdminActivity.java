package com.example.mediant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeAdminActivity extends AppCompatActivity {

    Button addMedicine, removeMedicine;
    Button searchMedicine, searchAmbulance;
    Button addAmbulance, removeAmbulance;

    private Button addAdmin;
    private Button removeAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);
        getSupportActionBar().setTitle("Admin Home");
        addMedicine = (Button) findViewById(R.id.addmedicine_button);
        removeMedicine = (Button) findViewById(R.id.removemedicine_button);
        addAmbulance = (Button) findViewById(R.id.Addambulance_button);
        removeAmbulance = (Button) findViewById(R.id.removeambulance_button);
        addAdmin = (Button)findViewById(R.id.addAdminButtonId);
        removeAdmin = (Button)findViewById(R.id.removeAdminButtonId);


        addMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addingmedicine();
            }
        });

        removeMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removingmedicine();
            }
        });

        addAmbulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addingambulance();
            }
        });

        removeAmbulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removingambulance();
            }
        });

        addAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 addingAdmin();
            }
        });

        removeAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removingadmin();
            }
        });




    }


    private void addingAdmin() {
        Intent intent = new Intent(getApplicationContext(),AddAdminActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void removingadmin(){
        Intent intent = new Intent(getApplicationContext(),RemoveAdminActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void addingmedicine() {
        Intent intent = new Intent(getApplicationContext(),AddMedicineActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void removingmedicine(){
        Intent intent = new Intent(this, RemoveMedicineActivity.class);
        startActivity(intent);

    }
    
    public void addingambulance(){
        Intent intent = new Intent(this, AddAmbulanceActivity.class);
        startActivity(intent);

    }

    public void removingambulance(){
        Intent intent = new Intent(this, RemoveAmbulanceActivity.class);
        startActivity(intent);
    }
}
