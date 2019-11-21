
package com.example.mediant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AllAmbulanceActivity extends AppCompatActivity {
    List<Model> modelList = new ArrayList<>();
    RecyclerView mrecyclerView;
    Button nextButton;
    RecyclerView.LayoutManager layoutManager;
    FirebaseFirestore firebaseFirestore;
    CustomAdapterforAllAmbulance adapter;
    CollectionReference collectionReference;
    DocumentSnapshot lastInfo;
    Query query;
    public final int REQUEST_CALL = 1;
    public String phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_ambulance);
        getSupportActionBar().setTitle("All Ambulance");
        mrecyclerView = findViewById(R.id.recycler_view);
        mrecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        nextButton = findViewById(R.id.nextButton2);
        mrecyclerView.setLayoutManager(layoutManager);
        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("Ambulance");
        query = collectionReference.orderBy("Name", Query.Direction.ASCENDING).limit(10);
        query
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        for (DocumentSnapshot doc : task.getResult()) {
                            Model model = new Model(doc.getString("Name"),
                                    doc.getString("Contact Number"),
                                    doc.getString("Service Area")
                            );
                            modelList.add(model);

                        }
                            adapter = new CustomAdapterforAllAmbulance(AllAmbulanceActivity.this, modelList);
                            mrecyclerView.setAdapter(adapter);


                        lastInfo = task.getResult().getDocuments().get(task.getResult().size() - 1);
                        if(task.getResult().size()!=10){
                            nextButton.setVisibility(View.GONE);
                        }

                        nextButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Query nextQuery = collectionReference.orderBy("Name", Query.Direction.ASCENDING).startAfter(lastInfo).limit(10);
                                nextQuery
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {

                                                    if (task.getResult().size() != 0) {
                                                        for (DocumentSnapshot doc : task.getResult()) {
                                                            Model model = new Model(doc.getString("Name"),
                                                                    doc.getString("Contact Number"),
                                                                    doc.getString("Service Area")
                                                            );
                                                            modelList.add(model);

                                                        }
                                                        adapter.notifyDataSetChanged();
                                                        lastInfo = task.getResult().getDocuments().get(task.getResult().size() - 1);
                                                        if (task.getResult().size() != 10) {
                                                            nextButton.setVisibility(View.GONE);
                                                        }
                                                    } else {
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
    public void makeCall(){
        if (ContextCompat.checkSelfPermission(AllAmbulanceActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AllAmbulanceActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        }
        else {
            String dial = "tel:" + phoneNumber;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makeCall();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void  getPhoneNumber(int index) {
        phoneNumber = modelList.get(index).getContactNumber();
    }
}