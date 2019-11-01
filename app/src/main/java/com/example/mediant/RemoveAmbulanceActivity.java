package com.example.mediant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RemoveAmbulanceActivity extends AppCompatActivity {

    EditText ambulanceName;
    Button ambulanceNameEnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_ambulance);
        ambulanceName = findViewById(R.id.editTextAmbulanceName);
        ambulanceNameEnter = findViewById(R.id.button_enter_ambulance);

        ambulanceNameEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputText = ambulanceName.getText().toString().toLowerCase().trim();
                // Toast.makeText(getApplicationContext(),inputText,Toast.LENGTH_LONG).show();
                if(inputText.isEmpty()){
                    ambulanceName.setError("Enter the Ambulance name");
                    ambulanceName.requestFocus();
                    return;
                }
                else {
                    Intent i = new Intent(getApplicationContext(), AmbulanceRemoveListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("value", inputText);
                    ambulanceName.setText("");
                    i.putExtras(bundle);
                    startActivity(i);
                }
            }
        });
    }
}
