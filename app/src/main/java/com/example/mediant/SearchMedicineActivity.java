package com.example.mediant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SearchMedicineActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    SearchView searchMedicine;
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_medicine);
        getSupportActionBar().setTitle("Find Medicines");

        spinner = findViewById(R.id.spinner_seach_medicine);
        searchMedicine = findViewById(R.id.id_search_medicine);
        adapter = ArrayAdapter.createFromResource(this, R.array.SearchingMedicine, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener( this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();
        if(item.equals("Brand Name")){
            searchMedicine.setQueryHint("Enter brand name");
            searchMedicine.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    searchaccordingBrandName(s);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    return false;
                }
            });


        }

        else if(item.equals("Generic Name")){
            searchMedicine.setQueryHint("Enter generic name");
            searchMedicine.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    searchaccordingGenericName(s);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    return false;
                }
            });
        }

    }

    private void searchaccordingGenericName(String s) {
        String inputText = s.toLowerCase().trim();
        // Toast.makeText(getApplicationContext(),inputText,Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, MedicineSearchListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("value",inputText);
        bundle.putString("type","Generic name");
        i.putExtras(bundle);
        startActivity(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

        Toast.makeText(getApplicationContext(),"Please select an search type",Toast.LENGTH_LONG).show();

    }

    public void searchaccordingBrandName(String s){
        String inputText = s.trim();
        // Toast.makeText(getApplicationContext(),inputText,Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, MedicineSearchListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("value",inputText);
        bundle.putString("type","Brand Name");
        i.putExtras(bundle);
        startActivity(i);
    }


}
