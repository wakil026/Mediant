package com.example.mediant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class UpdateMedicineActivity extends AppCompatActivity {

    private EditText brandNameEditText;
    private EditText genericEditText;
    private EditText companyEditText;
    private EditText indicationsEditText;
    private EditText sideEffectsEditText;
    private EditText typeEditText;
    private EditText containsEditText;

    private Button updateButton;

    private String docname;
    private String brand;
    private String generic;
    private String indications;
    private String contains;
    private String medtype;
    private String sideEffect;
    private String company;


    FirebaseFirestore firebaseFirestore;
    CollectionReference collectionReference;
    DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_medicine);
        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference =firebaseFirestore.collection("Medicine Information");

        Intent intent = getIntent();
        docname = intent.getStringExtra("docname");

        documentReference = collectionReference.document(docname);

        getData();
        brandNameEditText = findViewById(R.id.updateBrandNameEditTextId);
        genericEditText = findViewById(R.id.updateGenericEditTextId);
        indicationsEditText = findViewById(R.id.updateIndicationsEditTextId);
        companyEditText = findViewById(R.id.updateCompanyNameEditTextId);
        containsEditText = findViewById(R.id.updateContainsEditTextId);
        sideEffectsEditText = findViewById(R.id.updateSideEffectsEditTextId);
        typeEditText = findViewById(R.id.updateMedicineTypeEditTextId);

        updateButton = findViewById(R.id.updateButtonId);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                            brand = documentSnapshot.getString("brandName");
                            generic = documentSnapshot.getString("genericName");
                            contains = documentSnapshot.getString("contains");
                            medtype = documentSnapshot.getString("type");
                            company = documentSnapshot.getString("companyName");
                            indications = documentSnapshot.getString("indications");
                            sideEffect = documentSnapshot.getString("sideEffect");

                            brandNameEditText.setText(brand);
                            genericEditText.setText(generic);
                            indicationsEditText.setText(indications);
                            companyEditText.setText(company);
                            containsEditText.setText(contains);
                            sideEffectsEditText.setText(sideEffect);
                            typeEditText.setText(medtype);
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

    protected void saveData() {
        String brand = brandNameEditText.getText().toString().toUpperCase().trim();
        String generic = genericEditText.getText().toString().toLowerCase().trim();
        String indications = indicationsEditText.getText().toString().trim();
        String contains = containsEditText.getText().toString().trim();
        String type = typeEditText.getText().toString().trim();
        String sideEffects = sideEffectsEditText.getText().toString().trim();
        String company = companyEditText.getText().toString().trim();

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
        docname = docname.replaceAll("[^A-Za-z0-9]","").trim().toLowerCase();
        collectionReference.document(""+docname).set(medicineInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Successfully updated the medicine", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        setResult(RESULT_OK,intent);
                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }
}
