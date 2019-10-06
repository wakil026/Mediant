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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AddMedicineActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText genericEditText;
    private EditText brandEditText;
    private EditText descriptionEditText;

    private String KEY_BRAND = "Brand Name";
    private String KEY_GENERIC = "Generic Name";
    private String KEY_DESCRIPTION = "Description";

    private CollectionReference collectionReference;

    private Button saveButton;

    private FirebaseFirestore database = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);
        setTitle("Medicine Information");

        collectionReference = database.collection("Medicine Information");
        nameEditText = findViewById(R.id.BrandNameEditTextId);
        genericEditText = findViewById(R.id.GenericEditTextId);
        descriptionEditText = findViewById(R.id.DescriptionEditTextId);
        saveButton = findViewById(R.id.SaveButtonId);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData(v);
            }
        });

    }

    protected void saveData(View v) {

        String brand = nameEditText.getText().toString().trim();
        String generic = genericEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        MedicineInfo medicineInfo = new MedicineInfo(brand, generic, description);

        collectionReference.document().set(medicineInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AddMedicineActivity.this, "Medicine Added", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddMedicineActivity.this, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });




    }



}