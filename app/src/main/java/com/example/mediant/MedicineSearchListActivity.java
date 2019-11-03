package com.example.mediant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MedicineSearchListActivity extends AppCompatActivity {
    List<MedicineInfo> modelList = new ArrayList<>();
    RecyclerView mrecyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseFirestore firebaseFirestore;
    CustomAdapterforMedicine adapter;
    int x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_search_list);
        mrecyclerView = findViewById(R.id.recycler_view);
        mrecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        mrecyclerView.setLayoutManager(layoutManager);
        Bundle bundle = getIntent().getExtras();
        String search_data = bundle.getString("value");
        getSupportActionBar().setTitle(search_data);
        String searchType = bundle.getString("type");
        firebaseFirestore = FirebaseFirestore.getInstance();

        if (searchType.equals("Brand Name")) {
            x=0;
            firebaseFirestore.collection("Medicine Information").whereEqualTo("brandName", search_data.toUpperCase().trim())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            for (DocumentSnapshot doc : task.getResult()) {
                                x=1;
                                MedicineInfo model = new MedicineInfo(doc.getString("brandName"),
                                        doc.getString("genericName"),
                                        doc.getString("type"),
                                        doc.getString("contains"),
                                        doc.getString("companyName"),
                                        doc.getString("indications"),
                                        doc.getString("sideEffect")
                                );
                                modelList.add(model);

                            }
                            if(x==1) {
                                adapter = new CustomAdapterforMedicine(MedicineSearchListActivity.this, modelList);
                                mrecyclerView.setAdapter(adapter);
                            }
                            else Toast.makeText(getApplicationContext(),"Sorry! No information available.",Toast.LENGTH_LONG).show();

                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }
        else{
            x=0;
            firebaseFirestore.collection("Medicine Information").whereEqualTo("genericName", search_data.toLowerCase().trim())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            for (DocumentSnapshot doc : task.getResult()) {
                                x=1;
                                MedicineInfo model = new MedicineInfo(doc.getString("brandName"),
                                        doc.getString("genericName"),
                                        doc.getString("type"),
                                        doc.getString("contains"),
                                        doc.getString("companyName")
                                );
                                modelList.add(model);

                            }
                            if(x==1) {
                                adapter = new CustomAdapterforMedicine(MedicineSearchListActivity.this, modelList);
                                mrecyclerView.setAdapter(adapter);
                            }
                            else Toast.makeText(getApplicationContext(),"Sorry! No information available.",Toast.LENGTH_LONG).show();

                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }
    }
    public void showDetails(final int position){
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Medicine Information").document(modelList.get(position).getBrandName())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        String brandName = modelList.get(position).getBrandName();
                        String genericName = modelList.get(position).getGenericName();
                        String companyName = modelList.get(position).getCompanyName();
                        String type = modelList.get(position).getType();
                        String indication = modelList.get(position).getIndications();
                        String sideEffect = modelList.get(position).getSideEffect();
                        String medicineContain = modelList.get(position).getContains();
                        Bundle bundle = new Bundle();
                        bundle.putString("brandName",brandName);
                        bundle.putString("genericName",genericName);
                        bundle.putString("medicineContain",medicineContain);
                        bundle.putString("companyName",companyName);
                        bundle.putString("type",type);
                        bundle.putString("indication",indication);
                        bundle.putString("sideEffect",sideEffect);
                        Intent intent = new Intent(getApplicationContext(), MedicineDetailsActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);


                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }
}
