package com.example.mediant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SearchMedicineActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    EditText searchMedicine;
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    Button searchMedicineButton;


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
        searchMedicineButton = findViewById(R.id.search_medicine_button);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();
        if(item.equals("Brand Name")){
            searchMedicine.setHint("Enter brand name");
            searchMedicineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String s = searchMedicine.getText().toString();
                    if(s.isEmpty()){
                        searchMedicine.setError("This field cannot be empty");
                        searchMedicine.requestFocus();
                        return;
                    }
                    else searchaccordingBrandName(s);
                }
            });


        }

        else if(item.equals("Generic Name")){
            searchMedicine.setHint("Enter generic name");
            searchMedicineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String s = searchMedicine.getText().toString();
                    if(s.isEmpty()){
                        searchMedicine.setError("This field cannot be empty");
                        searchMedicine.requestFocus();
                        return;
                    }
                    else searchaccordingGenericName(s);
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
