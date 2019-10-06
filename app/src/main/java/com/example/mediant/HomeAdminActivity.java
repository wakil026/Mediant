package com.example.mediant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeAdminActivity extends AppCompatActivity {

    Button addMedicine, removeMedicine, addAmbulance, removeAmbulance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);

        addMedicine = (Button) findViewById(R.id.addmedicine_button);
        removeMedicine = (Button) findViewById(R.id.removemedicine_button);
        addAmbulance = (Button) findViewById(R.id.Addambulance_button);
        removeAmbulance = (Button) findViewById(R.id.removeambulance_button);

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

    }

    public void addingmedicine() {
        Intent intent = new Intent(this, AddMedicineActivity.class);
        startActivity(intent);
    }

    public void removingmedicine(){

    }

    public void addingambulance(){
        Intent intent = new Intent(this, AddAmbulanceActivity.class);
        startActivity(intent);

    }

    public void removingambulance(){

    }
}