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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddMedicineActivity extends AppCompatActivity {

    private EditText brandNameEditText;
    private EditText genericEditText;
    private EditText companyEditText;
    private EditText indicationsEditText;
    private EditText sideEffectsEditText;
    private EditText typeEditText;
    private EditText containsEditText;

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
        brandNameEditText = findViewById(R.id.BrandNameEditTextId);
        genericEditText = findViewById(R.id.GenericEditTextId);
        indicationsEditText = findViewById(R.id.indicationsEditTextId);
        companyEditText = findViewById(R.id.companyNameEditTextId);
        containsEditText = findViewById(R.id.containsEditTextId);
        sideEffectsEditText = findViewById(R.id.sideEffectsEditTextId);
        typeEditText = findViewById(R.id.medicineTypeEditTextId);


        saveButton = findViewById(R.id.SaveButtonId);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData(v);
            }
        });

    }

    protected void saveData(View v) {

        String brand = brandNameEditText.getText().toString().toUpperCase();
        String generic = genericEditText.getText().toString().toLowerCase();
        String indications = indicationsEditText.getText().toString();
        String contains = containsEditText.getText().toString();
        String type = typeEditText.getText().toString();
        String sideEffects = sideEffectsEditText.getText().toString();
        String company = companyEditText.getText().toString();

        if(brand.isEmpty()){
            brandNameEditText.setError("This field cannot be empty");
            brandNameEditText.requestFocus();
            return;
        }
        if(generic.isEmpty()){
            genericEditText.setError("This field cannot be empty");
            genericEditText.requestFocus();
            return;
        }
        if(indications.isEmpty()){
            indicationsEditText.setError("This field cannot be empty");
            indicationsEditText.requestFocus();
            return;
        }
        if(type.isEmpty()){
            typeEditText.setError("This field cannot be empty");
            typeEditText.requestFocus();
            return;
        }
        if(company.isEmpty()){
            companyEditText.setError("This field cannot be empty");
            companyEditText.requestFocus();
            return;
        }

        MedicineInfo medicineInfo = new MedicineInfo(brand,generic,contains,type,company,indications,sideEffects);
        String docname = ""+brand+generic+type+contains;
        docname = docname.replaceAll("[^A-Za-z0-9]","").trim();
        collectionReference.document(""+docname).set(medicineInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AddMedicineActivity.this, "Medicine Added", Toast.LENGTH_SHORT).show();
                        clearFields();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddMedicineActivity.this, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }
    private void clearFields(){
        genericEditText.setText("");
        brandNameEditText.setText("");
        containsEditText.setText("");
        indicationsEditText.setText("");
        sideEffectsEditText.setText("");
        typeEditText.setText("");
        companyEditText.setText("");

    }



}
