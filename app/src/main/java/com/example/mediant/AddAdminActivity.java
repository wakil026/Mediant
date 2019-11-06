package com.example.mediant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddAdminActivity extends AppCompatActivity {
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private EditText newAdminEmail;
    private Button addButton;
    private String KEY_TYPE = "Type";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin);
        getSupportActionBar().setTitle("Add Admin");
        newAdminEmail = (EditText) findViewById(R.id.newAdminEmailId);
        addButton = (Button) findViewById(R.id.addButtonId);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = newAdminEmail.getText().toString().trim();
                addAdmin(email);
            }
        });


    }

    private void addAdmin(final String email) {
        Map<String,Object> user = new HashMap<>();
        user.put(KEY_TYPE,"Admin");
        firebaseFirestore.collection("UserData").document(email).update(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        newAdminEmail.setText("");
                        Toast.makeText(getApplicationContext(), "Successfully Added "+email+" as Admin", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "failed: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
