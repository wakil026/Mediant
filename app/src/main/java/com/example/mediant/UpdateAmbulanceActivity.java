package com.example.mediant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UpdateAmbulanceActivity extends AppCompatActivity {
    private EditText ambulanceNameEditText;
    private EditText contactNumberEditText;
    private EditText serviceAreaEditText;

    private Button updateButton;

    private String docname;
    private String ambulanceName;
    private String contactNumber;
    private String serviceArea;

    private String key_ambulance_name = "Name";
    private String key_ambulance_number = "Contact Number";
    private String key_ambulance_area = "Service Area";

    FirebaseFirestore firebaseFirestore;
    CollectionReference collectionReference;
    DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_ambulance);
        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference =firebaseFirestore.collection("Ambulance");

        Intent intent = getIntent();
        docname = intent.getStringExtra("docname");

        documentReference = collectionReference.document(docname);

        getData();

        ambulanceNameEditText = findViewById(R.id.updateAmbulanceNameId);
        contactNumberEditText = findViewById(R.id.updateContactNumberId);
        serviceAreaEditText = findViewById(R.id.updateServiceAreaId);
        updateButton = findViewById(R.id.ambulanceUpdateButtondId);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });


    }

    private void getData() {
        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            ambulanceName = documentSnapshot.getString(key_ambulance_name);
                            contactNumber = documentSnapshot.getString(key_ambulance_number);
                            serviceArea = documentSnapshot.getString(key_ambulance_area);

                            ambulanceNameEditText.setText(ambulanceName);
                            contactNumberEditText.setText(contactNumber);
                            serviceAreaEditText.setText(serviceArea);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void saveData(){
        ambulanceName = ambulanceNameEditText.getText().toString().trim().toUpperCase();
        contactNumber = contactNumberEditText.getText().toString().trim();
        serviceArea = serviceAreaEditText.getText().toString().trim().toUpperCase();

        if(ambulanceName.isEmpty()){
            ambulanceNameEditText.setError("This field cannot be empty");
            ambulanceNameEditText.requestFocus();
            return;
        }
        if(contactNumber.isEmpty()){
            contactNumberEditText.setError("This field cannot be empty");
            contactNumberEditText.requestFocus();
            return;
        }
        if(serviceArea.isEmpty()){
            serviceAreaEditText.setError("This field cannot be empty");
            serviceAreaEditText.requestFocus();
            return;
        }


        Map<String, Object> ambulance_data = new HashMap<>();
        ambulance_data.put(key_ambulance_name,ambulanceName);
        ambulance_data.put(key_ambulance_number,contactNumber);
        ambulance_data.put(key_ambulance_area,serviceArea);
        String docname = ""+ambulanceName+serviceArea;
        docname = docname.replaceAll("[^A-Za-z0-9]","").trim().toLowerCase();
        final String fileId = docname;
        collectionReference.document(docname).set(ambulance_data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Ambulance information updated",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        intent.setData(Uri.parse(fileId));
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Updating ambulance information failed",Toast.LENGTH_LONG).show();
                    }
                });

    }
}
