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

public class AmbulanceRemoveListActivity extends AppCompatActivity {
    List<Model> modelList = new ArrayList<>();
    RecyclerView mrecyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseFirestore firebaseFirestore;
    CustomAdapterforRemoveAmbulance adapter;
    int x=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance_remove_list);
        mrecyclerView = findViewById(R.id.recycler_view);
        mrecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        mrecyclerView.setLayoutManager(layoutManager);
        firebaseFirestore = FirebaseFirestore.getInstance();
        Bundle bundle = getIntent().getExtras();
        String search_data = bundle.getString("value");
        getSupportActionBar().setTitle(search_data);
        firebaseFirestore.collection("Ambulance").whereEqualTo("Name", search_data.toUpperCase().trim())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        for (DocumentSnapshot doc : task.getResult()) {
                            x=1;
                            Model model = new Model(doc.getString("Name"),
                                    doc.getString("Contact Number"),
                                    doc.getString("Service Area")
                            );
                            modelList.add(model);

                        }
                        if(x==1) {
                            adapter = new CustomAdapterforRemoveAmbulance(AmbulanceRemoveListActivity.this, modelList);
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
    public void deleteAmbulanceData(int index){
        firebaseFirestore = FirebaseFirestore.getInstance();
        String ambulanceName = modelList.get(index).getName();
        String serviceArea = modelList.get(index).getServiceArea();
        String docname = ""+ambulanceName+serviceArea;
        docname = docname.replaceAll("[^A-Za-z0-9]","").trim().toLowerCase();
        firebaseFirestore.collection("Ambulance").document(docname)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(),"Ambulance information deleted successfully.",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(),RemoveAmbulanceActivity.class);
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
