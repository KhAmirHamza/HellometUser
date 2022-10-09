package com.hellomet.user.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import com.hellomet.user.R;

public class PrescriptionActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "PrescriptionActivity";
    MaterialButton material_btn_browse_prescription,material_btn_submit_prescription;
    ImageView imgv_prescription;
    ProgressBar progress_prescription;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);

        material_btn_browse_prescription = findViewById(R.id.material_btn_browse_prescription);
        material_btn_submit_prescription = findViewById(R.id.material_btn_submit_prescription);
        imgv_prescription = findViewById(R.id.imgv_prescription);
        progress_prescription = findViewById(R.id.progress_prescription);

        material_btn_browse_prescription.setOnClickListener(this);
        material_btn_submit_prescription.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.material_btn_browse_prescription) {
//            CropImage.startPickImageActivity(PrescriptionActivity.this);
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction("android.intent.action.GET_CONTENT");
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE);

        } else if (v.getId() == R.id.material_btn_submit_prescription) {
            if (uri != null) {


                // Upload image to server then go to Pharmacy Activity...
                //uploadImageFileToServerViaRestAPIusingRetrofit(apiRequests,file);

                startActivity(new Intent(PrescriptionActivity.this, PharmacyActivity.class)
                        .putExtra("prescriptionImageUri", uri.toString()));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
           /* Uri imageUri = CropImage.getPickImageResultUri(PrescriptionActivity.this, data);
            if (CropImage.isReadExternalStoragePermissionsRequired(PrescriptionActivity.this, imageUri)) {
                uri = imageUri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, 0});
                }
            }*/
            Uri imageUri = data.getData();
            startCrop(imageUri);


        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imgv_prescription.setImageURI(result.getUri());
            uri = result.getUri();
            Toast.makeText(this, "Image Crop Successfully!", Toast.LENGTH_SHORT).show();
        }


    }

    private void startCrop(Uri imageUri){
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
    }
}