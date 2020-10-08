package com.tarp.farmcare.ui.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tarp.farmcare.R;

public class ResultActivity extends AppCompatActivity {

    DatabaseReference mbase;
    ProgressBar loadingProgressBar;
    SharedPreferences sharedPreferences;

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        loadingProgressBar = findViewById(R.id.loading);
        loadingProgressBar.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        int int_id = 0;
        if (id != null) {
            int_id = Integer.parseInt(id)+1;
        }
        String d_name = intent.getStringExtra("d_name");
        float d_confidence = intent.getFloatExtra("confidence",0.0f);

        mbase = FirebaseDatabase.getInstance().getReference("Disease");

        TextView diseaseDesc = findViewById(R.id.diseaseDescription);
        TextView confidence = findViewById(R.id.confidenceDisease);
        TextView name = findViewById(R.id.detectedDisease);

        TextView textDisease = findViewById(R.id.textDetected);
        TextView textHealthy = findViewById(R.id.healthyDescription);
        TextView textAbout = findViewById(R.id.aboutDisease);
//        TextView textConfd = findViewById(R.id.textConfidence);


        confidence.setText(Float.toString(d_confidence));
        name.setText(d_name);


        sharedPreferences = getSharedPreferences("language", Context.MODE_PRIVATE);
        String language = sharedPreferences.getString("language", "en");
        if (language.equals("pa")) {
            language = "pn";
        } else if(language.equals("ta")) {
            language = "tn";
        } else {
            language = "en";
        }

        if (d_name != null && d_name.contains("healthy")) {
            textDisease.setText("Status:");
            textAbout.setText("About Status:");
            loadingProgressBar.setVisibility(View.GONE);
            textHealthy.setVisibility(View.VISIBLE);
        } else {
            mbase.child(String.valueOf(int_id)).child("description_"+language).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    try {
                        if (snapshot.getValue() != null) {
                            try {
                                String d_description = snapshot.getValue().toString();
                                loadingProgressBar.setVisibility(View.GONE);
                                diseaseDesc.setText(d_description);
                                diseaseDesc.setVisibility(View.VISIBLE);
                                diseaseDesc.setMovementMethod(new ScrollingMovementMethod());
                                Log.e("TAG", "" + snapshot.getValue());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.e("TAG", " it's null.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("onCancelled", " cancelled");
                }

            });
        }






        Log.d("ID", ""+ id);
    }
}
