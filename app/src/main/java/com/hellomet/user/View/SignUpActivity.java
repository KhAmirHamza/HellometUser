package com.hellomet.user.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import com.hbb20.CountryCodePicker;
import com.hellomet.user.ApiClient;
import com.hellomet.user.Model.Profile;
import com.hellomet.user.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hellomet.user.Constants.MAIN_URL;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SignUpActivity";
    MaterialButton btn_goto_sign_in;
    MaterialButton btn_sign_up;

    ProgressBar progressbar;
    TextInputLayout textinputlayout_phone_number;
    CountryCodePicker countryCodePicker;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_sign_up);

        progressbar = findViewById(R.id.progressbar);
        textinputlayout_phone_number = findViewById(R.id.textinputlayout_phone_number);
        btn_sign_up = (MaterialButton) findViewById(R.id.btn_sign_up);
        btn_goto_sign_in = (MaterialButton) findViewById(R.id.btn_goto_sign_in);
        textinputlayout_phone_number.setOnClickListener(this);
        btn_sign_up.setOnClickListener(this);
        btn_goto_sign_in.setOnClickListener(this);
        countryCodePicker = findViewById(R.id.ccp);
        Log.d(TAG, "onClick: phoneNumber: " + textinputlayout_phone_number.getEditText().getText().toString().trim());
    }

    public void onClick(View v) {
        if (v.getId() == textinputlayout_phone_number.getId()) {
            textinputlayout_phone_number.getEditText().setCursorVisible(true);
        } else if (v.getId() == this.btn_sign_up.getId()) {

            String phoneNumber = countryCodePicker.getSelectedCountryCode()+textinputlayout_phone_number.getEditText().getText().toString();
            if (textinputlayout_phone_number.getEditText().getText().toString().isEmpty()) {
                Toast.makeText(this, "Please insert valid phone number", Toast.LENGTH_SHORT).show();
                return;
            }
            //for testing directly
            //startActivity(new Intent(SignUpActivity.this, VerificationActivity.class).putExtra("phoneNumber", phoneNumber));
            progressbar.setVisibility(View.VISIBLE);
            ApiClient.getInstance(MAIN_URL).getUserProfileUsingPhoneNumber(phoneNumber).enqueue(new Callback<Profile>() {
                public void onResponse(Call<Profile> call, Response<Profile> response) {
                    progressbar.setVisibility(View.GONE);
                    if (!response.isSuccessful()) {
                        Log.d(TAG, "onResponse: Res" +
                                "ponseError:" + response.errorBody().toString());
                        //Toast.makeText(SignUpActivity.this.getApplicationContext(), "ResponseError:" + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                    } else if (response.body() != null) {
                        Toast.makeText(SignUpActivity.this, "An Account is already created with this phone number,Try another.", Toast.LENGTH_SHORT).show();
                    } else {
                        startActivity(new Intent(SignUpActivity.this, VerificationActivity.class).putExtra("phoneNumber", phoneNumber));
                    }
                }

                public void onFailure(Call<Profile> call, Throwable t) {
                    progressbar.setVisibility(View.GONE);
                    Log.d(TAG, "onFailure: "+t.getMessage());
                    Toast.makeText(SignUpActivity.this.getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else if (v.getId() == R.id.btn_goto_sign_in) {
            startActivity(new Intent(this, SignInActivity.class));
        }
    }
}
