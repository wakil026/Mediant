package com.example.mediant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RemoveMedicineActivity extends AppCompatActivity {
    EditText medicineName;
    Button medicineNameEnter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_medicine);
        getSupportActionBar().setTitle("Remove Medicine");
        medicineName = findViewById(R.id.editTextMedicineName);
        medicineNameEnter = findViewById(R.id.button_enter_medicine);

        medicineNameEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputText = medicineName.getText().toString().toUpperCase().trim();
                // Toast.makeText(getApplicationContext(),inputText,Toast.LENGTH_LONG).show();
                if(inputText.isEmpty()){
                    medicineNameEnter.setError("Enter the Medicine name");
                    medicineNameEnter.requestFocus();
                    return;
                }
                else {
                    Intent i = new Intent(getApplicationContext(), MedicineRemoveListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("value", inputText);
                    medicineName.setText("");
                    i.putExtras(bundle);
                    startActivity(i);
                }
            }
        });
    }
}
