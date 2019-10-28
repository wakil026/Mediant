package com.example.mediant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class UserHomeActivity extends AppCompatActivity {
    Button allmedicine,mymedicine,findambu;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        allmedicine = findViewById(R.id.button_allmedicine);
        mymedicine = findViewById(R.id.button_mymedicine);
        findambu = findViewById(R.id.button_findambu);

        allmedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_allmedicine();
            }
        });

        mymedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_mymedicine();
            }
        });

        findambu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_ambulance();
            }
        });


    }
    public void show_allmedicine() {
        Intent intent = new Intent(this, AllMedicineActivity.class);
        startActivity(intent);
    }

    public void show_mymedicine(){
        Intent intent = new Intent(this, MyMedicineActivity.class);
        startActivity(intent);
    }

    public void show_ambulance(){
        Intent intent = new Intent(this, AllAmbulanceActivity.class);
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu1) {
        getMenuInflater().inflate(R.menu.user_menu_layout, menu1);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about_us:
                // do your code
                return true;
            case R.id.profileId:
                Intent intent = new Intent(this, ViewProfileActivity.class);
                startActivity(intent);
                return true;
            case R.id.sign_out:
                FirebaseAuth.getInstance().signOut();
                finish();
                intent = new Intent(getApplicationContext(),SignInActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
