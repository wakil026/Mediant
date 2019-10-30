package com.example.mediant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class UpdateProfileActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;
    private DocumentReference documentReference;

    private Button updateButton;
    private EditText usernameText;
    private EditText ageText;
    private EditText bloodGroupText;
    private EditText cityText;

    private TextView updateEmail;
    private TextView updatePassword;

    private String email;
    private String name;
    private String age;
    private String bloodGroup;
    private String city;

    private String KEY_NAME = "Name";
    private String KEY_BLOOD_GROUP = "Blood Group:";
    private String KEY_AGE = "Age:";
    private String KEY_CITY = "City";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        usernameText = findViewById(R.id.updateUsernameId);
        ageText = findViewById(R.id.updateAgeId);
        bloodGroupText = findViewById(R.id.updateBloodGroupId);
        cityText = findViewById(R.id.updateCityId);
        updateButton = findViewById(R.id.updateButtonId);

        updateEmail = findViewById(R.id.updateEmailTextViewId);
        updatePassword = findViewById(R.id.updatePasswordTextViewId);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        email = user.getEmail();
        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("UserData");
        documentReference = collectionReference.document(email);

        getData();

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });

        updateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),UpdateEmailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),UpdatePasswordActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void getData() {
        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            name = documentSnapshot.getString(KEY_NAME);
                            age = documentSnapshot.getString(KEY_AGE);
                            bloodGroup = documentSnapshot.getString(KEY_BLOOD_GROUP);
                            city = documentSnapshot.getString(KEY_CITY);

                            usernameText.setText(name);
                            ageText.setText(age);
                            bloodGroupText.setText(bloodGroup);
                            cityText.setText(city);

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

    private void updateProfile() {
        Map<String,Object> data = new HashMap<>();
        name = usernameText.getText().toString().trim();
        age = ageText.getText().toString();
        bloodGroup = bloodGroupText.getText().toString().trim();
        city = cityText.getText().toString().trim();

        if(!name.isEmpty()){
            data.put(KEY_NAME,name);
        }
        if(!age.isEmpty()){
            data.put(KEY_AGE,age);
        }
        if(!bloodGroup.isEmpty()){
            data.put(KEY_BLOOD_GROUP,bloodGroup);
        }
        if(!city.isEmpty()){
            data.put(KEY_CITY,city);
        }
        documentReference.update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Successfully updated your profile", Toast.LENGTH_SHORT).show();
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
