package com.example.mediant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
            ambusearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    searchaccordingcity(s);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    return false;
                }
            });
        }

        else if(item.equals("Name")){
            ambusearch.setQueryHint("Enter ambulance name");
            ambusearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    searchaccordingname(s);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    return false;
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
