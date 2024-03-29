package com.example.mediant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RemoveAdminActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private EditText removeAdminEmail;
    private Button removeButton;
    private String KEY_TYPE = "Type";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_admin);
        getSupportActionBar().hide();
        removeAdminEmail = (EditText) findViewById(R.id.removeAdminEmailId);
        removeButton = (Button) findViewById(R.id.removeButtonId);

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = removeAdminEmail.getText().toString().trim();
                if(!email.isEmpty())
                    removeAdmin(email);
                else{
                    removeAdminEmail.setError("Email cannot be empty");
                    removeAdminEmail.requestFocus();
                }
            }
        });


    }

    private void removeAdmin(final String email) {
        Map<String,Object> user = new HashMap<>();
        user.put(KEY_TYPE,"User");
        firebaseFirestore.collection("UserData").document(email).update(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        removeAdminEmail.setText("");
                        Toast.makeText(getApplicationContext(), "Successfully removed "+email+" from Admin", Toast.LENGTH_SHORT).show();
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
