package com.example.mediant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UpdateEmailActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;
    private DocumentReference documentReference;

    private EditText updatedEmail;
    private EditText updatedReEmail;

    private Button updateButton;

    private String email;
    private String newEmail;
    private String newReEmail;
    private String username;
    private String type;
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
        setContentView(R.layout.activity_update_email);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        updatedEmail = findViewById(R.id.updatedEmailId);
        updatedReEmail = findViewById(R.id.updatedReEmailId);
        updateButton = findViewById(R.id.changeEmailId);

        email = user.getEmail();
        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("UserData");
        documentReference = collectionReference.document(email);

        getData();

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEmail();
            }
        });

    }

    private void getData() {
        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            username = documentSnapshot.getString(KEY_NAME);
                            age = documentSnapshot.getString(KEY_AGE);
                            type = documentSnapshot.getString(KEY_TYPE);
                            bloodGroup = documentSnapshot.getString(KEY_BLOOD_GROUP);
                            city = documentSnapshot.getString(KEY_CITY);

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

    private void updateEmail() {
        newEmail = updatedEmail.getText().toString().trim();
        newReEmail = updatedReEmail.getText().toString().trim();

        if(newEmail.isEmpty()){
            updatedEmail.setError("Enter an Email Address");
            updatedEmail.requestFocus();
            return;
        }

        if(newReEmail.isEmpty()){
            updatedReEmail.setError("Re Enter your email");
            updatedReEmail.requestFocus();
            return;
        }
        if(!newEmail.equals(newReEmail)){
            updatedReEmail.setError("Your Emails are not same");
            updatedReEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()){
            updatedEmail.setError("Enter a valid email address");
            updatedEmail.requestFocus();
            return;
        }

        user.updateEmail(newEmail)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        updateUser();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });



    }

    private void updateUser() {
        documentReference = collectionReference.document(newEmail);
        Map<String,Object> data = new HashMap<>();

        data.put(KEY_NAME, username);
        data.put(KEY_EMAIL,newEmail);
        data.put(KEY_TYPE,type);
        data.put(KEY_AGE,age);
        data.put(KEY_BLOOD_GROUP,bloodGroup);
        data.put(KEY_CITY,city);

        documentReference.set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Successfully Updated Email", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
