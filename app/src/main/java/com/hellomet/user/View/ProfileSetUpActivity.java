package com.hellomet.user.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import com.hellomet.user.ApiClient;
import com.hellomet.user.ApiRequests;
import com.hellomet.user.Model.Profile;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.hellomet.user.R;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.hellomet.user.Constants.MAIN_URL;

public class ProfileSetUpActivity extends AppCompatActivity {
    public static final int PICK_IMAGE = 1;
    private static final String TAG = "ProfileSetUpActivity";
    MaterialButton btn_choose_image;
    MaterialButton btn_submit_profile;
    String confirm_pass;
    TextInputLayout edt_profile_confirm_pass;
    TextInputLayout edt_profile_email;
    TextInputLayout edt_profile_name;
    TextInputLayout edt_profile_password;
    TextInputLayout edt_profile_phone_number;
    String email;
    ImageView iv_profile_image;
    String name;
    String password;
    String phone_number;
    MaterialToolbar profile_toolbar;
    ProgressBar progressbar;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_set_up);
        MaterialToolbar materialToolbar = (MaterialToolbar) findViewById(R.id.profile_toolbar);
        profile_toolbar = materialToolbar;
        setSupportActionBar(materialToolbar);
        progressbar = findViewById(R.id.progressbar);
        btn_choose_image = (MaterialButton) findViewById(R.id.btn_choose_image);
        btn_submit_profile = (MaterialButton) findViewById(R.id.btn_submit_profile);
        iv_profile_image = (ImageView) findViewById(R.id.iv_profile_image);
        edt_profile_name = (TextInputLayout) findViewById(R.id.edt_profile_name);
        edt_profile_email = (TextInputLayout) findViewById(R.id.edt_profile_email);
        edt_profile_password = (TextInputLayout) findViewById(R.id.edt_profile_password);
        edt_profile_confirm_pass = (TextInputLayout) findViewById(R.id.edt_profile_confirm_pass);
        TextInputLayout textInputLayout = (TextInputLayout) findViewById(R.id.edt_profile_phone_number);
        edt_profile_phone_number = textInputLayout;
        textInputLayout.getEditText().setText(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().substring(1));
        edt_profile_phone_number.setFocusable(false);
        btn_choose_image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction("android.intent.action.GET_CONTENT");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == -1 && data != null) {
            Uri imageUri = data.getData();
            this.iv_profile_image.setImageURI(imageUri);
            try {
                Bitmap selectedImageBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                ApiRequests apiRequests =new Retrofit.Builder().baseUrl(MAIN_URL)
                //ApiRequests apiRequests =new Retrofit.Builder().baseUrl("https://hellomet-health-98.herokuapp.com/")
                        .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                        .client(
                                new OkHttpClient.Builder()
                                        .readTimeout(600, TimeUnit.SECONDS)
                                        .writeTimeout(600, TimeUnit.SECONDS)
                                        .connectTimeout(5, TimeUnit.MINUTES).build()
                        ).build().create(ApiRequests.class);

                btn_submit_profile.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (progressbar.getVisibility()==View.VISIBLE){
                            Toast.makeText(ProfileSetUpActivity.this, "A task is in progress..!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        name = edt_profile_name.getEditText().getText().toString();
                        email = edt_profile_email.getEditText().getText().toString();
                        password = edt_profile_password.getEditText().getText().toString();
                        confirm_pass = edt_profile_confirm_pass.getEditText().getText().toString();
                        phone_number = edt_profile_phone_number.getEditText().getText().toString().trim();

                        if (name.isEmpty()) {
                            edt_profile_name.setError("Name (required)!");
                            edt_profile_name.requestFocus();
                        } else if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            edt_profile_email.setError("Valid emil address (required)!");
                            edt_profile_email.requestFocus();
                        } else if (password.isEmpty() || password.length() < 6) {
                            edt_profile_password.setError("6 Digits Password (required)!");
                            edt_profile_password.requestFocus();
                        } else if (confirm_pass.isEmpty() || !password.equals(confirm_pass)) {
                            edt_profile_confirm_pass.setError("Password does not match! (required).");
                            edt_profile_confirm_pass.requestFocus();
                        } else {
                            createProfile(selectedImageBitmap, apiRequests);
                        }
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                FirebaseCrashlytics.getInstance().recordException(e);
            }
        }
    }

    /* access modifiers changed from: private */
    public void createProfile(Bitmap selectedImageBitmap, ApiRequests apiRequests) {
        File file = new File(getApplicationContext().getFilesDir(), "Image_" + System.currentTimeMillis() + ".jpg");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        selectedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bitmapdata = byteArrayOutputStream.toByteArray();
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            progressbar.setVisibility(View.VISIBLE);
            apiRequests.uploadImageToGenarateUrl(
                    MultipartBody.Part.createFormData("uploadImage", file.getName(),
                            RequestBody.create(MediaType.parse("image/*"), file)))
                    .enqueue(new Callback<JsonObject>() {
                public void onResponse(Call<JsonObject> call, final Response<JsonObject> response) {
                    progressbar.setVisibility(View.GONE);
                    if (response.code() == 200) {
                        try {
                            progressbar.setVisibility(View.VISIBLE);
                            String imageUrl = new JSONObject(response.body().toString()).getString("url");
                            Profile.MetaData metaData = new Profile.MetaData(name, imageUrl, phone_number, email);
                            Profile.Auth auth = new Profile.Auth(phone_number, password);
                            Profile profile = new Profile(metaData, auth);

                            //vaiya eitate problem, onnanno sob kichu thik ache
                            Log.d(TAG, "onResponse: " + profile.getAuth().getPassword());
                            //ApiClient.getInstance("http://192.168.0.105:3000/"+"user/")
                            ApiClient.getInstance(MAIN_URL)
                                    .setUpProfile(profile)
                                    .enqueue(new Callback<Profile>() {
                                public void onResponse(Call<Profile> call, Response<Profile> response2) {

                                    progressbar.setVisibility(View.GONE);
                                    if (response2.isSuccessful()) {
                                        String id = response2.body().getId();
                                        String name = response2.body().getMetaData().getName();
                                        String phone_number = response2.body().getMetaData().getPhone_number();
                                        if (id != null && name != null && phone_number != null) {
                                            SharedPreferences sharedPreferences = getSharedPreferences("AUTHENTICATION", 0);
                                            sharedPreferences.edit().putString("USER_PHONE_NUMBER", phone_number).apply();
                                            sharedPreferences.edit().putString("USER_ID", id).apply();
                                            sharedPreferences.edit().putString("USER_NAME", name).apply();
                                            startActivity(new Intent(ProfileSetUpActivity.this, HomeActivity.class));
                                            finish();

                                        }else {
                                            FirebaseCrashlytics.getInstance().log("Nothing Found!");
                                            Log.d(TAG, "onResponse: Nothing Found!");
                                        }

                                    }else {
                                        Log.d(TAG, "onResponse: Error: "+response.errorBody().toString());
                                        FirebaseCrashlytics.getInstance().log("onResponse: Error: "+response.errorBody().toString());
                                    }

                                }

                                public void onFailure(Call<Profile> call, Throwable t) {
                                    FirebaseCrashlytics.getInstance().log("onFailure: MetaData: " + t.getMessage());
                                    Log.d(ProfileSetUpActivity.TAG, "onFailure: MetaData: " + t.getMessage());
                                    Log.d(ProfileSetUpActivity.TAG, "onFailure: MetaData: " + t.getCause().toString());
                                    Log.d(ProfileSetUpActivity.TAG, "onFailure: MetaData: " + t.getLocalizedMessage());
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                            FirebaseCrashlytics.getInstance().recordException(e);
                        }
                    } else {
                        try {
                            String message = new JSONObject(response.errorBody().toString()).getString("message");
                            Log.d(TAG, "onResponse: "+message);
                            FirebaseCrashlytics.getInstance().log("onResponse: "+message);
                            //Toast.makeText(ProfileSetUpActivity.this, message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e2) {
                            e2.printStackTrace();
                            FirebaseCrashlytics.getInstance().recordException(e2);
                        }
                    }
                }

                public void onFailure(Call<JsonObject> call, Throwable t) {
                    progressbar.setVisibility(View.GONE);
                    Log.d(TAG, "onFailure: Error: " + t.getCause());
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        } catch (IOException e2) {
            e2.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e2);
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        FirebaseAuth.getInstance().signOut();
        finish();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        FirebaseAuth.getInstance().signOut();
    }
}
