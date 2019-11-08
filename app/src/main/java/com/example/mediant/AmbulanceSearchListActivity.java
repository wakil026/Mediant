package com.example.mediant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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

public class AmbulanceSearchListActivity extends AppCompatActivity {
    List<Model> modelList = new ArrayList<>();
    RecyclerView mrecyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseFirestore firebaseFirestore;
    CustomAdapter adapter;
    public final int REQUEST_CALL = 1;
    public String phoneNumber;
    int x;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance_search_list);

        mrecyclerView = findViewById(R.id.recycler_view);
        mrecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        mrecyclerView.setLayoutManager(layoutManager);
        Bundle bundle = getIntent().getExtras();
        String search_data = bundle.getString("value");
        getSupportActionBar().setTitle(search_data);
        String searchType = bundle.getString("type");
        firebaseFirestore = FirebaseFirestore.getInstance();

        if(searchType.equals("city")) {
            x=0;
            firebaseFirestore.collection("Ambulance").whereEqualTo("Service Area", search_data.toUpperCase().trim())
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
                                adapter = new CustomAdapter(AmbulanceSearchListActivity.this, modelList);
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
                                adapter = new CustomAdapter(AmbulanceSearchListActivity.this, modelList);
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
    public void makeCall(){
        if (ContextCompat.checkSelfPermission(AmbulanceSearchListActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AmbulanceSearchListActivity.this,
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