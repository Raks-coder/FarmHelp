package com.tarp.farmcare.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tarp.farmcare.R;
import com.tarp.farmcare.data.adapter.diseaseAdapter;
import com.tarp.farmcare.data.model.Diseases;

import java.util.ArrayList;
import java.util.Map;

public class SearchActivity extends AppCompatActivity implements diseaseAdapter.DiseaseItemClickListener{

    private RecyclerView recyclerView;
    diseaseAdapter adapter; // Create Object of the Adapter class
    DatabaseReference mbase; // Create object of the
    // Firebase Realtime Database
    ProgressBar loadingProgressBar;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        loadingProgressBar = findViewById(R.id.loading);
//        loadingProgressBar.setVisibility(View.VISIBLE);

        // Create a instance of the database and get
        // its reference
        sharedPreferences = getSharedPreferences("language", Context.MODE_PRIVATE);
        mbase = FirebaseDatabase.getInstance().getReference("Disease");

        recyclerView = findViewById(R.id.recycler1);

        // To display the Recycler view linearly
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        FirebaseRecyclerOptions<Diseases> options = new FirebaseRecyclerOptions.Builder<Diseases>()
                .setQuery(mbase, Diseases.class)
                .build();


        adapter = new diseaseAdapter(options, this) {
            @Override
            public void onLoaded() {
                loadingProgressBar.setVisibility(View.GONE);
            }
        };

        recyclerView.setAdapter(adapter);
    }

    // Function to tell the app to start getting
    // data from database on starting of the activity
    @Override protected void onStart()
    {
        super.onStart();
        adapter.startListening();
    }

    // Function to tell the app to stop getting
    // data from database on stoping of the activity
    @Override protected void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onDiseaseItemClick(int position, ArrayList<Integer> id, ArrayList<String> name, ArrayList<String> description, ArrayList<String> descriptionPn, ArrayList<String> descriptionTn) {
        Intent i = new Intent(SearchActivity.this, DescriptionActivity.class);
        System.out.println(id);
        String language = sharedPreferences.getString("language", "en");
        if (language.equals("pa")) {
            i.putExtra("description",descriptionPn.get(position));
        } else if(language.equals("ta")) {
            i.putExtra("description",descriptionTn.get(position));
        } else {
            i.putExtra("description",description.get(position));
        }
        i.putExtra("id",id.get(position));
        i.putExtra("name",name.get(position));
        startActivity(i);

    }
}
