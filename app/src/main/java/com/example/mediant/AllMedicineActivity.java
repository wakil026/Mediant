package com.example.mediant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AllMedicineActivity extends AppCompatActivity {

    List<MedicineInfo> modelList = new ArrayList<>();
    Button nextButton;
    RecyclerView mrecyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseFirestore firebaseFirestore;
    CollectionReference collectionReference;
    DocumentSnapshot lastInfo;
    Query query;
    CustomAdapterforAllMedicine adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_medicine);
        getSupportActionBar().setTitle("All Medicines");
        mrecyclerView = findViewById(R.id.recycler_view);
        mrecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        mrecyclerView.setLayoutManager(layoutManager);
        nextButton = findViewById(R.id.nextButton);
        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("Medicine Information");
        query = collectionReference.orderBy("brandName", Query.Direction.ASCENDING).limit(10);

        query
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        for (DocumentSnapshot doc : task.getResult()) {
                            MedicineInfo model = new MedicineInfo(doc.getString("brandName"),
                                    doc.getString("genericName"),
                                    doc.getString("contains"),
                                    doc.getString("type"),
                                    doc.getString("companyName"),
                                    doc.getString("indications"),
                                    doc.getString("sideEffect")
                            );
                            modelList.add(model);

                        }
                        adapter = new CustomAdapterforAllMedicine(AllMedicineActivity.this, modelList);
                        mrecyclerView.setAdapter(adapter);

                        lastInfo = task.getResult().getDocuments().get(task.getResult().size() - 1);
                        if(task.getResult().size()!=10){
                            nextButton.setVisibility(View.GONE);
                        }

                        nextButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Query nextQuery = collectionReference.orderBy("brandName", Query.Direction.ASCENDING).startAfter(lastInfo).limit(10);
                                nextQuery
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if(task.isSuccessful()) {
                                                    if (task.getResult().size() != 0) {
                                                        for (DocumentSnapshot doc : task.getResult()) {
                                                            MedicineInfo model = new MedicineInfo(doc.getString("brandName"),
                                                                    doc.getString("genericName"),
                                                                    doc.getString("contains"),
                                                                    doc.getString("type"),
                                                                    doc.getString("companyName"),
                                                                    doc.getString("indications"),
                                                                    doc.getString("sideEffect")
                                                            );
                                                            modelList.add(model);
                                                        }
                                                        adapter.notifyDataSetChanged();
                                                        lastInfo = task.getResult().getDocuments().get(task.getResult().size() - 1);
                                                        if(task.getResult().size()!=10){
                                                            nextButton.setVisibility(View.GONE);
                                                        }
                                                    }
                                                    else{
                                                        nextButton.setVisibility(View.GONE);
                                                    }
                                                }
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });
                            }
                        });



                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
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