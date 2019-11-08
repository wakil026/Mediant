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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpdatePasswordActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private Button changePasswordButton;
    private EditText newPassword;
    private EditText confirmPassword;

    private String password;
    private String rePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        getSupportActionBar().setTitle("Update Password");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        changePasswordButton = findViewById(R.id.changePasswordButtonId);
        newPassword = findViewById(R.id.newPasswordId);
        confirmPassword = findViewById(R.id.confirmPasswordId);

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassword();
            }
        });

    }

    private void updatePassword() {
        password = newPassword.getText().toString();
        rePassword = confirmPassword.getText().toString();

        if(password.isEmpty()){
            newPassword.setError("Enter a password");
            newPassword.requestFocus();
            return;
        }

        if(password.length()<6){
            newPassword.setError("Minimum length of a password should be at least 6");
            newPassword.requestFocus();
            return;
        }

        if(!password.equals(rePassword)){
            confirmPassword.setError("Password didn't match");
            confirmPassword.requestFocus();
            return;
        }
        user.updatePassword(password)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Successfully updated your password", Toast.LENGTH_SHORT).show();
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
