package com.example.mediant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewProfileActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;
    private DocumentReference documentReference;

    private TextView profieEmailTextView;
    private TextView profieUsernameTextView;
    private TextView profieAgeTextView;
    private TextView profieBloodGroupTextView;
    private TextView profileCityTextView;

    private String email;
    private String username;
    private String age;
    private String bloodGroup;
    private String city;

    private String KEY_NAME = "Name";
    private String KEY_EMAIL = "Email";
    private String KEY_TYPE = "Type";
    private String KEY_BLOOD_GROUP = "Blood Group:";
    private String KEY_AGE = "Age:";
    private String KEY_CITY = "City";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        profieEmailTextView = findViewById(R.id.emailId);
        profieUsernameTextView = findViewById(R.id.usernameId);
        profieAgeTextView = findViewById(R.id.ageId);
        profieBloodGroupTextView = findViewById(R.id.bloodGroupId);
        profileCityTextView = findViewById(R.id.cityId);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        collectionReference = firebaseFirestore.collection("UserData");
        viewProfile();
    }

    private void viewProfile() {
        email = user.getEmail();
        documentReference = collectionReference.document(email);
        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            username = documentSnapshot.getString(KEY_NAME);
                            age = documentSnapshot.getString(KEY_AGE);
                            bloodGroup = documentSnapshot.getString(KEY_BLOOD_GROUP);
                            city = documentSnapshot.getString(KEY_CITY);

                            profieEmailTextView.setText(email);
                            profieUsernameTextView.setText(username);
                            profieAgeTextView.setText(age);
                            profieBloodGroupTextView.setText(bloodGroup);
                            profileCityTextView.setText(city);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
