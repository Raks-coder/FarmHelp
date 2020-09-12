package com.tarp.farmcare.ui.login;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.tarp.farmcare.R;
import com.tarp.farmcare.data.model.User;

public class ReportActivity extends AppCompatActivity {

    LocationManager locationManager;

    public String latitude,longitude;
    private static final int REQUEST_LOCATION = 1;
    ProgressBar loadingProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_calamity);

        Button fireButton = findViewById(R.id.fire);
        Button floodButton = findViewById(R.id.flood);
        Button droughtButton = findViewById(R.id.drought);
        Button pestButton = findViewById(R.id.pests);
        loadingProgressBar = findViewById(R.id.loading);
//        findViewById(R.id.logout).setOnClickListener(this);

        fireButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callGetLocation();
                updateFirebase("Fire");
//                Toast.makeText(ReportActivity.this, "Current Location: " + latitude + ", " + longitude, Toast.LENGTH_LONG).show();
//                Toast.makeText(getApplicationContext(), "Click On Test Crop", Toast.LENGTH_LONG).show();
            }
        });

        floodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callGetLocation();
                updateFirebase("Flood");
//                Toast.makeText(getApplicationContext(), "Click On Report", Toast.LENGTH_LONG).show();
            }
        });

        droughtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callGetLocation();
                updateFirebase("Drought");
//                Toast.makeText(getApplicationContext(), "Click On Search", Toast.LENGTH_LONG).show();
            }
        });

        pestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callGetLocation();
                updateFirebase("Pests");
//                Toast.makeText(getApplicationContext(), "Click On Search", Toast.LENGTH_LONG).show();
            }
        });

    }


    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void callGetLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            getLocation();
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                ReportActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                ReportActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);
                Toast.makeText(this, "Current Location: " + latitude + ", " + longitude, Toast.LENGTH_LONG).show();
//                showLocation.setText("Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude);
            } else {
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateFirebase(String calamity) {
        loadingProgressBar.setVisibility(View.VISIBLE);
        User user = new User(latitude, longitude, calamity);

        FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Report")
                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    loadingProgressBar.setVisibility(View.INVISIBLE);
//                                                Toast.makeText(SignUpActivity.this, "Current Location: " + latitude + ", " + longitude, Toast.LENGTH_LONG).show();
                    Toast.makeText(ReportActivity.this, "Successfully Update", Toast.LENGTH_LONG).show();
                } else {
                    loadingProgressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



}
