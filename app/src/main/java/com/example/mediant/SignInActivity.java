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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;



public class SignInActivity extends AppCompatActivity  implements View.OnClickListener {
    private FirebaseAuth mAuth;
    //private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private DocumentReference documentReference;
    private CollectionReference collectionReference;
    private FirebaseAuth.AuthStateListener authStateListener;


    private EditText signInEmail,signInPassword;
    private Button signInButton;
    private TextView signUpTextView;
    private TextView forgotPasswordTextView;
    private ProgressBar signInProgressBar;

    private String KEY_NAME = "name";
    private String KEY_EMAIL = "email";
    private String KEY_TYPE = "type";


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if(currentUser != null) {
            checkUsertype(currentUser.getEmail());
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        this.setTitle("Sign In");

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        collectionReference = firebaseFirestore.collection("UserData");

        signInEmail = findViewById(R.id.signInEmailId);
        signInPassword = findViewById(R.id.signInPasswordId);
        signInButton = findViewById(R.id.signInButtonId);
        signUpTextView = findViewById(R.id.signUpTextViewId);
        forgotPasswordTextView = findViewById(R.id.signUpForgotPasswordId);
        signInProgressBar = findViewById(R.id.signInProgressBarId);


        signUpTextView.setOnClickListener(this);
        signInButton.setOnClickListener(this);
        forgotPasswordTextView.setOnClickListener(this);
        signInProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signInButtonId:
                userLogin();
                break;
            case  R.id.signUpTextViewId:
                finish();
                Intent intent = new Intent(getApplicationContext(),SignUpActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.signUpForgotPasswordId:
                intent = new Intent(getApplicationContext(),ForgotPasswordActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }

    }

    private void userLogin() {
        final String email = signInEmail.getText().toString().trim();
        String password = signInPassword.getText().toString().trim();

        if(email.isEmpty()){
            signInEmail.setError("Enter an Email Address");
            signInEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            signInEmail.setError("Enter a valid email address");
            signInEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            signInPassword.setError("Enter a password");
            signInPassword.requestFocus();
            return;
        }
        signInProgressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    checkUsertype(email);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Error : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void checkUsertype(String email){
        final FirebaseUser user = mAuth.getCurrentUser();
        documentReference = collectionReference.document(email);
        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            String type = documentSnapshot.getString(KEY_TYPE);
                            signInProgressBar.setVisibility(View.GONE);
                            if(type.equals("Admin")){
                                finish();
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);

                            }
                            else{
                                checkIfVerified(user,type);

                            }

                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Something Wrong happened", Toast.LENGTH_SHORT).show();
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

    private void checkIfVerified(FirebaseUser user,String type) {
        if(user.isEmailVerified()) {
            finish();
            Intent intent = new Intent(getApplicationContext(),UserHomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            Toast.makeText(getApplicationContext(), type, Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "Please, Verify your email", Toast.LENGTH_SHORT).show();
        }

    }
}
