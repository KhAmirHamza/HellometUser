package com.hellomet.user.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import com.hbb20.CountryCodePicker;
import com.hellomet.user.ApiClient;
import com.hellomet.user.Model.Profile;
import com.hellomet.user.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hellomet.user.Constants.MAIN_URL;

public class SignInActivity extends AppCompatActivity {
    private static final String TAG = "SignInActivity";
    MaterialButton btn_goto_sign_up;
    MaterialButton btn_sign_in;
    TextInputLayout textInputLayout_countryCode;
    TextInputLayout textInputLayout_password;
    TextInputLayout textInputLayout_phone_number;
    ProgressBar progressbar;
    CountryCodePicker countryCodePicker;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        textInputLayout_phone_number = (TextInputLayout) findViewById(R.id.textInputLayout_phone_number);
        textInputLayout_password = (TextInputLayout) findViewById(R.id.textInputLayout_password);
        btn_sign_in = (MaterialButton) findViewById(R.id.btn_sign_in);
        btn_goto_sign_up = (MaterialButton) findViewById(R.id.btn_goto_sign_up);
        progressbar = findViewById(R.id.progressbar);
        countryCodePicker =findViewById(R.id.ccp);

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String phoneNumber = countryCodePicker.getSelectedCountryCode() +textInputLayout_phone_number.getEditText().getText().toString();
                String password = textInputLayout_password.getEditText().getText().toString();
                if (textInputLayout_phone_number.getEditText().getText().toString().isEmpty()) {
                    SignInActivity.this.textInputLayout_phone_number.setError("Required.");
                    SignInActivity.this.textInputLayout_phone_number.requestFocus();
                    return;
                } else if (password.isEmpty()) {
                    SignInActivity.this.textInputLayout_password.setError("Required.");
                    SignInActivity.this.textInputLayout_password.requestFocus();
                    return;
                } else {
                    progressbar.setVisibility(View.VISIBLE);
                    ApiClient.getInstance(MAIN_URL +"user/").signInUser(phoneNumber, password).enqueue(new Callback<Profile>() {
                        public void onResponse(Call<Profile> call, Response<Profile> response) {
                            progressbar.setVisibility(View.GONE);
                            if (!response.isSuccessful() ) {
                                Log.d(TAG, "onResponse: signInUser: Error: "+response.errorBody());
                                Toast.makeText(SignInActivity.this, "Something Went wrong, Try Again.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (response.body() == null) {
                                Log.d(TAG, "onResponse: Nothing Found");
                                Toast.makeText(SignInActivity.this, "Nothing Found.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            String id = response.body().getId();
                            String name = response.body().getMetaData().getName();
                            String phone_number = response.body().getMetaData().getPhone_number();
                            if (id == null || name == null || phone_number == null) {
                                Toast.makeText(SignInActivity.this, "Phone Number or Password does not matched.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            SharedPreferences sharedPreferences = getSharedPreferences("AUTHENTICATION", Context.MODE_PRIVATE);
                            sharedPreferences.edit().putString("USER_PHONE_NUMBER", phone_number).apply();
                            sharedPreferences.edit().putString("USER_ID", id).apply();
                            sharedPreferences.edit().putString("USER_NAME", name).apply();
                            SignInActivity.this.startActivity(new Intent(SignInActivity.this, HomeActivity.class));
                            SignInActivity.this.finish();
                        }

                        public void onFailure(Call<Profile> call, Throwable t) {
                            progressbar.setVisibility(View.GONE);
                            Toast.makeText(SignInActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        this.btn_goto_sign_up.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SignInActivity.this.startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("AUTHENTICATION", 0);
        FirebaseApp.initializeApp(SignInActivity.this);
        if ((FirebaseAuth.getInstance().getCurrentUser() == null ||
                !sharedPreferences.getString("USER_PHONE_NUMBER", "null").equalsIgnoreCase("null"))
                && sharedPreferences.getString("USER_PHONE_NUMBER", "null") != null) {
            //Toast.makeText(this, "user not sign in", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(new Intent(this, ProfileSetUpActivity.class));
        finish();
        //Toast.makeText(this, "User Sign in", Toast.LENGTH_SHORT).show();
    }
}
