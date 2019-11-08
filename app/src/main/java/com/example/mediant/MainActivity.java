package com.example.mediant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    //private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private DocumentReference documentReference;
    private CollectionReference collectionReference;

    private String KEY_TYPE = "Type";

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
        else{
            finish();
            Intent intent = new Intent(getApplicationContext(),SignInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();

        mAuth = FirebaseAuth.getInstance();
        collectionReference = firebaseFirestore.collection("UserData");

    }
    private void checkUsertype(String email){
        final FirebaseUser user = mAuth.getCurrentUser();
        documentReference = collectionReference.document(email);
        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            SignInActivity.type = documentSnapshot.getString(KEY_TYPE);
                            if(SignInActivity.type.equals("Admin")){
                                finish();
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                            else{
                                checkIfVerified(user,SignInActivity.type);
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
            Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            Toast.makeText(getApplicationContext(), type, Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "Please, Verify your email", Toast.LENGTH_SHORT).show();
        }

    }

}
