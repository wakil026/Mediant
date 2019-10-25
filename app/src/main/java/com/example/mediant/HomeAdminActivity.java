package com.example.mediant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeAdminActivity extends AppCompatActivity {

    Button addMedicine, removeMedicine, searchMedicine, searchAmbulance, addAmbulance, removeAmbulance;

    private Button addAdmin;
    private Button removeAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);

        addMedicine = (Button) findViewById(R.id.addmedicine_button);
        removeMedicine = (Button) findViewById(R.id.removemedicine_button);
        addAmbulance = (Button) findViewById(R.id.Addambulance_button);
        removeAmbulance = (Button) findViewById(R.id.removeambulance_button);
        searchAmbulance = (Button) findViewById(R.id.button_search_ambu);
        searchMedicine = (Button) findViewById(R.id.button_search_medicine);

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


        searchMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchingMedicine();
            }
        });


        searchAmbulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchingAmbulance();
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

    }

    public void removingmedicine(){

    }

    public void searchingMedicine(){
        Intent intent = new Intent(this, SearchMedicineActivity.class);
        startActivity(intent);
    }

    public void searchingAmbulance(){
        Intent intent = new Intent(this, SearchAmbulanceActivity.class);
        startActivity(intent);
    }

    public void addingambulance(){
        Intent intent = new Intent(this, AddAmbulanceActivity.class);
        startActivity(intent);

    }

    public void removingambulance(){

    }
}
