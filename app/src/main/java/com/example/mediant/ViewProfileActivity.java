package com.example.mediant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ViewProfileActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private TextView profieEmailTextView;
    private TextView profieUsernameTextView;
    private TextView profieAgeTextView;
    private TextView profieBloodGroupTextView;

    private String email;
    private String username;
    private String age;
    private String bloodGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        profieEmailTextView = findViewById(R.id.emailId);
        profieUsernameTextView = findViewById(R.id.usernameId);
        profieAgeTextView = findViewById(R.id.ageId);
        profieBloodGroupTextView = findViewById(R.id.bloodGroupId);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
    }
}
