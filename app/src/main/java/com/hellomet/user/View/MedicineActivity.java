package com.hellomet.user.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.hellomet.user.Adapter.MedicineAdapter;
import com.hellomet.user.ApiClient;
import com.hellomet.user.Model.Medicine;
import java.util.List;

import com.hellomet.user.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hellomet.user.Constants.MAIN_URL;

public class MedicineActivity extends AppCompatActivity {
    private static final String TAG = "MedicineActivity";

    String pharmacyId;
    String pharmacyName;
    RecyclerView recy_medicine;
    MaterialToolbar toolbar_medicine;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_medicine);
        MaterialToolbar materialToolbar = (MaterialToolbar) findViewById(R.id.toolbar_medicine);
        toolbar_medicine = materialToolbar;
        setSupportActionBar(materialToolbar);
        pharmacyId = getIntent().getStringExtra("pharmacyId");
        String stringExtra = getIntent().getStringExtra("pharmacyName");
        pharmacyName = stringExtra;
        setTitle(stringExtra);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recy_medicine);
        recy_medicine = recyclerView;
        recyclerView.setHasFixedSize(true);
        recy_medicine.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ApiClient.getInstance(MAIN_URL).getMedicines().enqueue(new Callback<List<Medicine>>() {
            public void onResponse(Call<List<Medicine>> call, final Response<List<Medicine>> response) {
                if (response.isSuccessful()) {
                    //Toast.makeText(MedicineActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    MedicineAdapter medicineAdapter = new MedicineAdapter(MedicineActivity.this.getApplicationContext(), response.body());
                    MedicineActivity.this.recy_medicine.setAdapter(medicineAdapter);
                    medicineAdapter.setOnItemClickListener(new MedicineAdapter.OnItemClickListener() {
                        public void onItemClick(View view, int position) {
                            startActivity(new Intent(MedicineActivity.this, MedicineDetailsActivity.class).putExtra("id", ((Medicine) ((List) response.body()).get(position)).getId()).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }

                        public void onItemAddToCartClick(MaterialButton materialButton, MaterialCardView materialCardView, int position) {
                        }
                    });
                    return;
                }
                Log.d(TAG, "onResponse: Response Error: " + response.errorBody().toString());
            }

            public void onFailure(Call<List<Medicine>> call, Throwable t) {
                Log.d(TAG, "onFailure: Error: " + t.getMessage());
            }
        });
    }
}
