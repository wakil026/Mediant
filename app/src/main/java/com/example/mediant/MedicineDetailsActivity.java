package com.example.mediant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MedicineDetailsActivity extends AppCompatActivity {
    TextView brandName;
    TextView genericName;
    TextView contains;
    TextView medicineType;
    TextView companyName;
    TextView indications;
    TextView sideEffect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_details);
        Bundle bundle = getIntent().getExtras();
        brandName = findViewById(R.id.medicineBrandName);
        genericName = findViewById(R.id.medicineGenericName);
        companyName = findViewById(R.id.companyName);
        contains = findViewById(R.id.medicineContains);
        medicineType = findViewById(R.id.medicineType);
        indications = findViewById(R.id.medicineIndications);
        sideEffect = findViewById(R.id.medicineSideEffects);
        genericName.setText(bundle.getString("genericName"));
        companyName.setText(bundle.getString("companyName"));
        contains.setText(bundle.getString("medicineContain"));
        medicineType.setText(bundle.getString("type"));
        brandName.setText(bundle.getString("brandName"));
        indications.setText(bundle.getString("indication"));
        sideEffect.setText(bundle.getString("sideEffect"));
        getSupportActionBar().setTitle(bundle.getString("brandName"));
    }
}
