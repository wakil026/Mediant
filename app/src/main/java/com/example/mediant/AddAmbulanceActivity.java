package com.example.mediant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddAmbulanceActivity extends AppCompatActivity {

    Button enterAmbulanceinfo;
    EditText ambulanceName, contactNumber, city;
    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    private String key_ambulance_name = "Name";
    private String key_ambulance_number = "Contact Number";
    private String key_ambulance_area = "Service Area";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ambulance);


        enterAmbulanceinfo = findViewById(R.id.button_enter);
        ambulanceName = findViewById(R.id.editText_ambuName);
        contactNumber = findViewById(R.id.editText_ambuNumber);
        city = findViewById(R.id.editText_city);

        enterAmbulanceinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveinfo();
            }
        });
    }
    public void saveinfo(){
        String name = ambulanceName.getText().toString().trim().toUpperCase();
        String number = contactNumber.getText().toString().trim();
        String service_city = city.getText().toString().trim().toUpperCase();
        Map<String, Object> ambulance_data = new HashMap<>();
        ambulance_data.put(key_ambulance_name,name);
        ambulance_data.put(key_ambulance_number,number);
        ambulance_data.put(key_ambulance_area,service_city);
        String docname = ""+name+service_city;
        docname = docname.replaceAll("[^A-Za-z0-9]","").trim().toLowerCase();

        firebaseFirestore.collection("Ambulance").document(docname).set(ambulance_data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Ambulance information is added",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Adding ambulance information is failed",Toast.LENGTH_LONG).show();
                    }
                });

    }
}