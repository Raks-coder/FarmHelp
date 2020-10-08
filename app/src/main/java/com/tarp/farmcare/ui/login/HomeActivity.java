package com.tarp.farmcare.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.tarp.farmcare.R;
import com.tarp.farmcare.data.LocaleHelper;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedEdit;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);

        Button testButton = findViewById(R.id.test_crop);
        Button reportButton = findViewById(R.id.report);
        Button searchButton = findViewById(R.id.search);
        findViewById(R.id.logout).setOnClickListener(this);

        sharedPreferences = getSharedPreferences("language", Context.MODE_PRIVATE);
        sharedEdit = sharedPreferences.edit();

        String language = sharedPreferences.getString("language", "en");
        LocaleHelper.setLocale(HomeActivity.this, language);


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
                Intent i = new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(i);
                Toast.makeText(getApplicationContext(), "Click On Search", Toast.LENGTH_LONG).show();
            }
        });

        String[] arraySpinner = new String[] {
                "Change Language", "English", "ਪੰਜਾਬੀ", "தமிழ்"
        };
        Spinner s = (Spinner) findViewById(R.id.language_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(R.layout.spinner_item_layout);
        s.setAdapter(adapter);

        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Log.e("Check", "IN Select");
                if (position == 1) {

                    sharedEdit.putString("language", "en");
                    sharedEdit.apply();
                    LocaleHelper.setLocale(HomeActivity.this, "en");
                    s.setSelection(0);
                    recreate();
                } else if (position == 2){

                    sharedEdit.putString("language", "pa");
                    sharedEdit.apply();
                    LocaleHelper.setLocale(HomeActivity.this, "pa");
                    s.setSelection(0);
                    recreate();
                } else if (position == 3){

                    sharedEdit.putString("language", "ta");
                    sharedEdit.apply();
                    LocaleHelper.setLocale(HomeActivity.this, "ta");
                    s.setSelection(0);
                    recreate();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
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
