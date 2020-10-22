package com.tarp.farmcare.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tarp.farmcare.R;

public class DescriptionActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        Intent intent = getIntent();

        int id = intent.getIntExtra("id",0);
        String d_name = intent.getStringExtra("name");
        String d_description = intent.getStringExtra("description");

        TextView diseaseName = findViewById(R.id.diseaseName);
        diseaseName.setText(d_name);


        TextView diseaseDesc = findViewById(R.id.diseaseDescription);
        diseaseDesc.setText(d_description);
        diseaseDesc.setMovementMethod(new ScrollingMovementMethod());
        Log.d("ID", ""+ id);
    }
}
