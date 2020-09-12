package com.tarp.farmcare.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.tarp.farmcare.R;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);

        Button testButton = findViewById(R.id.test_crop);
        Button reportButton = findViewById(R.id.report);
        Button searchButton = findViewById(R.id.search);
        findViewById(R.id.logout).setOnClickListener(this);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, TestActivity.class);
                startActivity(i);
                Toast.makeText(getApplicationContext(), "Click On Test Crop", Toast.LENGTH_LONG).show();
            }
        });

        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, ReportActivity.class);
                startActivity(i);
                Toast.makeText(getApplicationContext(), "Click On Report", Toast.LENGTH_LONG).show();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Click On Search", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onClick(View view) {
        FirebaseAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }
}
