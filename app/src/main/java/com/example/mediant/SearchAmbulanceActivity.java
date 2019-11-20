package com.example.mediant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SearchAmbulanceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    EditText ambusearch;
    Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_ambulance);
        getSupportActionBar().setTitle("Search Ambulance");

        spinner = findViewById(R.id.spinner_seach_ambu);
        ambusearch = findViewById(R.id.id_search_ambu);
        adapter = ArrayAdapter.createFromResource(this, R.array.Searching_Ambulance, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        searchButton = findViewById(R.id.search_ambu_button);


    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();
        if(item.equals("City")){
            ambusearch.setHint("Enter city");
            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String s = ambusearch.getText().toString();
                    if(s.isEmpty()){
                        ambusearch.setError("This field cannot be empty");
                        ambusearch.requestFocus();
                        return;
                    }
                    else searchaccordingcity(s);
                }
            });

        }

        else if(item.equals("Name")){
            ambusearch.setHint("Enter ambulance name");
            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String s = ambusearch.getText().toString();
                    if(s.isEmpty()){
                        ambusearch.setError("This field cannot be empty");
                        ambusearch.requestFocus();
                        return;
                    }
                    else searchaccordingname(s);
                }
            });
        }

    }

    private void searchaccordingname(String s) {
        String inputText = s.toLowerCase().trim();
        // Toast.makeText(getApplicationContext(),inputText,Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, AmbulanceSearchListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("value",inputText);
        bundle.putString("type","name");
        i.putExtras(bundle);
        startActivity(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

        Toast.makeText(getApplicationContext(),"Please select an search type",Toast.LENGTH_LONG).show();

    }

    public void searchaccordingcity(String s){
        String inputText = s.trim();
        // Toast.makeText(getApplicationContext(),inputText,Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, AmbulanceSearchListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("value",inputText);
        bundle.putString("type","city");
        i.putExtras(bundle);
        startActivity(i);
    }


}
