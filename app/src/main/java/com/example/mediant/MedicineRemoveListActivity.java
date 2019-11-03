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

public class MedicineRemoveListActivity extends AppCompatActivity {
    List<MedicineInfo> modelList = new ArrayList<>();
    RecyclerView mrecyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseFirestore firebaseFirestore;
    CustomAdapterforRemoveMedicine adapter;
    int x=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_remove_list);
        mrecyclerView = findViewById(R.id.recycler_view);
        mrecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        mrecyclerView.setLayoutManager(layoutManager);
        firebaseFirestore = FirebaseFirestore.getInstance();
        Bundle bundle = getIntent().getExtras();
        String search_data = bundle.getString("value");
        getSupportActionBar().setTitle(search_data);
        firebaseFirestore.collection("Medicine Information").whereEqualTo("brandName", search_data.toUpperCase().trim())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        for (DocumentSnapshot doc : task.getResult()) {
                            x=1;
                            MedicineInfo model = new MedicineInfo(doc.getString("brandName"),
                                    doc.getString("genericName"),
                                    doc.getString("contains"),
                                    doc.getString("type"),
                                    doc.getString("companyName")
                            );
                            modelList.add(model);

                        }
                        if(x==1) {
                            adapter = new CustomAdapterforRemoveMedicine(MedicineRemoveListActivity.this, modelList);
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
    public void deleteMedicineData(int index){
        firebaseFirestore = FirebaseFirestore.getInstance();
        String brandName = modelList.get(index).getBrandName();
        String genericName = modelList.get(index).getGenericName();
        String type = modelList.get(index).getType();
        String contains = modelList.get(index).getContains();
        String docname = ""+brandName+genericName+type+contains;
        docname = docname.replaceAll("[^A-Za-z0-9]","").trim().toLowerCase();
        //Toast.makeText(getApplicationContext(),docname,Toast.LENGTH_LONG).show();
        firebaseFirestore.collection("Medicine Information").document(docname)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(),"Medicine information deleted successfully.",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(),RemoveMedicineActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
