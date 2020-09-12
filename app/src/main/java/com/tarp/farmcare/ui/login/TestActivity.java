package com.tarp.farmcare.ui.login;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.camera2.CameraAccessException;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.tarp.farmcare.R;
import com.tarp.farmcare.data.model.Classifier;
import com.tarp.farmcare.data.model.Recognition;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

public class TestActivity extends AppCompatActivity {

    Button btnCapture;
    Button btnImport;
    Button btnDetect;
    ImageView mPhotoImageView;
    TextView mResultTextView;

    Classifier mClassifier;
    Bitmap mBitmap;

    int mInputSize = 224;
    String mModelPath = "plant_disease_model.tflite";
    String mLabelPath = "plant_labels.txt";
    String mSamplePath = "soybean.JPG";

    private int mCameraRequestCode = 0;
    private int mGalleryRequestCode = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.capture_crop);

        btnCapture = (Button) findViewById(R.id.capture);
        btnImport = (Button) findViewById(R.id.importGallery);
        btnDetect = (Button) findViewById(R.id.detect);
        mPhotoImageView = (ImageView) findViewById(R.id.mPhotoImageView);
        mResultTextView = (TextView) findViewById(R.id.mResultTextView);

        AssetManager assetmanager = getAssets();
        InputStream is = null;
        try {
            is = assetmanager.open(mSamplePath);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            mClassifier = new Classifier(assetmanager, mModelPath, mLabelPath, mInputSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mBitmap = BitmapFactory.decodeStream(is);
        mBitmap = Bitmap.createScaledBitmap(mBitmap, mInputSize, mInputSize, true);
        mPhotoImageView.setImageBitmap(mBitmap);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(TestActivity.this,new String[]{Manifest.permission.CAMERA},101);

        }


        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(callCameraIntent, mCameraRequestCode);

            }
        });

        btnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callGalleryIntent = new Intent(Intent.ACTION_PICK);
                callGalleryIntent.setType("image/*");
                startActivityForResult(callGalleryIntent, mGalleryRequestCode);
            }
        });

        btnDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List <Recognition> results =  mClassifier.recognizeImage(mBitmap);
                Recognition rec;

                if (!results.isEmpty()) {
                    rec = results.get(0);
                    Log.e("Rec", " "+rec);
                    mResultTextView.setText(rec.getTitle() + "\n Confidence:" + rec.getConfidence());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == mCameraRequestCode) {
            if (resultCode == Activity.RESULT_OK && data != null) {

                mBitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                mBitmap = scaleImage(mBitmap);

//            val toast = Toast.makeText(this, ("Image crop to: w= ${mBitmap.width} h= ${mBitmap.height}"), Toast.LENGTH_LONG)
//            toast.setGravity(Gravity.BOTTOM, 0, 20)
//            toast.show()
                mPhotoImageView.setImageBitmap(mBitmap);
                mResultTextView.setText("Your photo image set now.");
            } else {
                Toast.makeText(this, "Camera cancel..", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == mGalleryRequestCode) {
            // check if the request code is same as what is passed  here it is 2
            if (data != null) {
                Uri uri = data.getData();

                try {
                    mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

//            println("Success!!!")
//                textureView.setVisibility(View.GONE);
//                mPhotoImageView.setVisibility(View.VISIBLE);
                mBitmap = scaleImage(mBitmap);
                mPhotoImageView.setImageBitmap(mBitmap);

            }
        }
    }

    private Bitmap scaleImage(Bitmap bitmap) {
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        float scaleWidth = (float)mInputSize / originalWidth;
        float scaleHeight = (float)mInputSize / originalHeight;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, originalWidth, originalHeight, matrix, true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 101)
        {
            if(grantResults[0] == PackageManager.PERMISSION_DENIED)
            {
                Toast.makeText(getApplicationContext(),"Sorry, camera permission is necessary",Toast.LENGTH_LONG).show();
                //   finish();

            }
        }
    }



}
