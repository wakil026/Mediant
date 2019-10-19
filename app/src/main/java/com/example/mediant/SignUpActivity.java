package com.example.mediant;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity  implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private DocumentReference documentReference;
    private EditText signUpName, signUpEmail,signUpPassword,signUpReEnterEmail,signUpReEnterPassword;
    private ProgressBar signUpProgressBar;
    private Button signUpButton;
    private TextView signInTextView;

    private String KEY_NAME = "name";
    private String KEY_EMAIL = "email";
    private String KEY_TYPE = "type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.setTitle("Sign Up");

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        signUpButton = findViewById(R.id.signUpButtonId);
        signInTextView = findViewById(R.id.signInTextViewId);
        signUpName = findViewById(R.id.signUpNameId);
        signUpEmail = findViewById(R.id.signUpEmailId);
        signUpPassword = findViewById(R.id.signUpPasswordId);
        signUpProgressBar = findViewById(R.id.signUpProgressBarId);
        signUpReEnterEmail = findViewById(R.id.signUpReEnterEmailId);
        signUpReEnterPassword = findViewById(R.id.signUpReEnterPasswordId);


        signInTextView.setOnClickListener(this);
        signUpButton.setOnClickListener(this);

        signUpProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signUpButtonId:
                userRegister();
                break;
            case  R.id.signInTextViewId:
                finish();
                Intent intent = new Intent(getApplicationContext(),SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

        }

    }

    private void userRegister() {
        final String name = signUpName.getText().toString().trim();
        final String email = signUpEmail.getText().toString().trim();
        final String reEmail = signUpReEnterEmail.getText().toString().trim();
        final String rePassword = signUpReEnterPassword.getText().toString().trim();
        final String password = signUpPassword.getText().toString().trim();

        if(name.isEmpty()){
            signUpName.setError("Username cannot be empty");
            signUpName.requestFocus();
            return;
        }

        if(email.isEmpty()){
            signUpEmail.setError("Enter an Email Address");
            signUpEmail.requestFocus();
            return;
        }

        if(reEmail.isEmpty()){
            signUpReEnterEmail.setError("Re Enter your email");
            signUpReEnterEmail.requestFocus();
            return;
        }
        if(!email.equals(reEmail)){
            signUpReEnterEmail.setError("Your Emails are not same");
            signUpReEnterEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            signUpEmail.setError("Enter a valid email address");
            signUpEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            signUpPassword.setError("Enter a password");
            signUpPassword.requestFocus();
            return;
        }

        if(password.length()<6){
            signUpPassword.setError("Minimum length of a password should be at least 6");
            signUpPassword.requestFocus();
            return;
        }

        if(!password.equals(rePassword)){
            signUpReEnterPassword.setError("Password didn't match");
            signUpReEnterPassword.requestFocus();
            return;
        }

        signUpProgressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                signUpProgressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    saveUserData(name,email);
                    finish();
                    Intent intent = new Intent(getApplicationContext(),UserHomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else if(task.getException() instanceof FirebaseAuthUserCollisionException){
                    Toast.makeText(getApplicationContext(), "This email is already registered", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Error : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    void saveUserData(String name,String email){
        Map<String,Object> user = new HashMap<>();
        user.put(KEY_NAME, name);
        user.put(KEY_EMAIL,email);
        user.put(KEY_TYPE,"User");
        firebaseFirestore.collection("UserData").document(email).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Successfully Signed Up", Toast.LENGTH_SHORT).show();
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
