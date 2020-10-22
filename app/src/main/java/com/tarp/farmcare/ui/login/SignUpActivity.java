package com.tarp.farmcare.ui.login;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.tarp.farmcare.R;
import com.tarp.farmcare.data.model.User;
import com.tarp.farmcare.ui.activity.HomeActivity;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText firstNameEditText, lastNameEditText, emailEditText, passwordEditText;
    ProgressBar loadingProgressBar;

    private static final int REQUEST_LOCATION = 1;

    public String latitude,longitude;
    private FirebaseAuth mAuth;
    LocationManager locationManager;
    private SignUpViewModel signUpViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signUpViewModel = ViewModelProviders.of(this, new SignUpViewModelFactory())
                .get(SignUpViewModel.class);

        firstNameEditText = findViewById(R.id.first_name);
        lastNameEditText = findViewById(R.id.last_name);
        emailEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        final Button signUpButton = findViewById(R.id.signup);

        loadingProgressBar = findViewById(R.id.loading);
        findViewById(R.id.already).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            getLocation();
        }

//        findViewById(R.id.button_register).setOnClickListener(this);

        signUpViewModel.getSignUpFormState().observe(this, new Observer<SignUpFormState>() {
            @Override
            public void onChanged(@Nullable SignUpFormState signUpFormState) {
                if (signUpFormState == null) {
                    return;
                }
                signUpButton.setEnabled(signUpFormState.isDataValid());
                if (signUpFormState.getUsernameError() != null) {
                    emailEditText.setError(getString(signUpFormState.getUsernameError()));
                }
                if (signUpFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(signUpFormState.getPasswordError()));
                }
                if (signUpFormState.getFirstNameError() != null) {
                    emailEditText.setError(getString(signUpFormState.getFirstNameError()));
                }
                if (signUpFormState.getLastNameError() != null) {
                    passwordEditText.setError(getString(signUpFormState.getLastNameError()));
                }
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                signUpViewModel.signUpDataChanged(firstNameEditText.getText().toString(), lastNameEditText.getText().toString(),
                        emailEditText.getText().toString(), passwordEditText.getText().toString());
            }
        };

        emailEditText.addTextChangedListener(afterTextChangedListener);
        firstNameEditText.addTextChangedListener(afterTextChangedListener);
        lastNameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    signUpViewModel.signUp(firstNameEditText.getText().toString(), lastNameEditText.getText().toString(),
                            emailEditText.getText().toString(), passwordEditText.getText().toString());
                }
                return false;
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                signUpViewModel.signUp(firstNameEditText.getText().toString(), lastNameEditText.getText().toString(),
                        emailEditText.getText().toString(), passwordEditText.getText().toString());

                mAuth.createUserWithEmailAndPassword(emailEditText.getText().toString(),
                        passwordEditText.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
//                                    loadingProgressBar.setVisibility(View.INVISIBLE);

                                    User user = new User(firstNameEditText.getText().toString() + " " + lastNameEditText.getText().toString(),
                                            emailEditText.getText().toString(), latitude, longitude, "None");
//                                    user.setInfo(firstNameEditText.getText().toString() + " " + lastNameEditText.getText().toString(),
//                                            emailEditText.getText().toString());

                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                loadingProgressBar.setVisibility(View.INVISIBLE);
//                                                Toast.makeText(SignUpActivity.this, "Current Location: " + latitude + ", " + longitude, Toast.LENGTH_LONG).show();
                                                Toast.makeText(SignUpActivity.this, "Successfully Registered", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                            } else {
                                                loadingProgressBar.setVisibility(View.INVISIBLE);
                                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                } else {
                                    loadingProgressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });

            }
        });

    }


//    void getLocation() {
//        try {
//            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//            if (locationManager != null) {
//                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
//            }
//        }
//        catch(SecurityException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    @Override
//    public void onClick(View view) {
//        finish();
//        startActivity(new Intent(this, LoginActivity.class));
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//        Toast.makeText(this, "Current Location: " + location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_LONG).show();
//        latitude = location.getLatitude();
//        longitude = location.getLongitude();
//
////        User user = new User();
////        user.setLocation(location.getLatitude(), location.getLongitude());
//    }
//
//    @Override
//    public void onStatusChanged(String s, int i, Bundle bundle) {
//
//    }
//
//    @Override
//    public void onProviderEnabled(String s) {
//
//    }
//
//    @Override
//    public void onProviderDisabled(String s) {
//        Toast.makeText(this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
//
//    }

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
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                SignUpActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                SignUpActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);
//                Toast.makeText(this, "Current Location: " + latitude + ", " + longitude, Toast.LENGTH_LONG).show();
//                showLocation.setText("Your Location: " + "\n" + "Latitude: " + latitude + "\n" + "Longitude: " + longitude);
            } else {
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View view) {
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }
}
