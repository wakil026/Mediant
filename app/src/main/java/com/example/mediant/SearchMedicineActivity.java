package com.example.mediant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;

public class SearchMedicineActivity extends AppCompatActivity {

    SearchView searchMedicine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_medicine);
        searchMedicine = findViewById(R.id.id_search_medicine);
        searchMedicine.setQueryHint("Enter medicine name");
        searchMedicine.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchingMedicine(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }
    public void searchingMedicine(String medicineName){
        String inputText = medicineName.trim();
        // Toast.makeText(getApplicationContext(),inputText,Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, MedicineSearchListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("value",inputText);
        //bundle.putString("type","city");
        i.putExtras(bundle);
        startActivity(i);
    }
}
