package com.example.mediant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

public class SearchAmbulanceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    SearchView ambusearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_ambulance);

        spinner = findViewById(R.id.spinner_seach_ambu);
        ambusearch = findViewById(R.id.id_search_ambu);
        adapter = ArrayAdapter.createFromResource(this, R.array.Searching_Ambulance, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();
        if(item.equals("City")){
            ambusearch.setQueryHint("Enter city");

        }

        else if(item.equals("Name")){
            ambusearch.setQueryHint("Enter ambulance name");

        }

        else if(item.equals("Contact Number")){
            ambusearch.setQueryHint("Enter contact number");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

        Toast.makeText(getApplicationContext(),"Please select an search type",Toast.LENGTH_SHORT).show();

    }
}
