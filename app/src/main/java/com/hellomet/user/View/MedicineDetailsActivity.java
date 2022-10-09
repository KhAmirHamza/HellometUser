package com.hellomet.user.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import com.hellomet.user.ApiClient;
import com.hellomet.user.Model.Medicine;
import com.hellomet.user.R;
import com.hellomet.user.Room.MedicineDatabase;
import com.hellomet.user.Room.MedicineEntityRoom;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hellomet.user.Constants.MAIN_URL;

public class MedicineDetailsActivity extends AppCompatActivity {
    private static final String TAG = "MedicineDetailsActivity";

    MaterialButton btn_add_to_cart;
    MaterialButton btn_order_now;
    ImageView imgv_medicine;
    MaterialToolbar toolbar_medicine_details;
    TextView txtv_medicine_brand;
    TextView txtv_medicine_description;
    TextView txtv_medicine_features;
    TextView txtv_medicine_indication;
    TextView txtv_medicine_name;
    TextView txtv_medicine_price;
    ProgressBar progressbar;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_medicine_details);
        MaterialToolbar materialToolbar = (MaterialToolbar) findViewById(R.id.toolbar_medicine_details);
        toolbar_medicine_details = materialToolbar;
        setSupportActionBar(materialToolbar);

        progressbar = findViewById(R.id.progressbar);

        imgv_medicine = (ImageView) findViewById(R.id.imgv_medicine);
        txtv_medicine_name = (TextView) findViewById(R.id.txtv_medicine_name);
        txtv_medicine_price = (TextView) findViewById(R.id.txtv_medicine_price);
        txtv_medicine_brand = (TextView) findViewById(R.id.txtv_medicine_brand);
        txtv_medicine_features = (TextView) findViewById(R.id.txtv_medicine_features);
        txtv_medicine_indication = (TextView) findViewById(R.id.txtv_medicine_indication);
        txtv_medicine_description = (TextView) findViewById(R.id.txtv_medicine_description);
        btn_order_now = (MaterialButton) findViewById(R.id.btn_order_now);
        btn_add_to_cart = (MaterialButton) findViewById(R.id.btn_add_to_cart);

        progressbar.setVisibility(View.VISIBLE);
        ApiClient.getInstance(MAIN_URL)
                .getMedicine(getIntent().getStringExtra("id")).enqueue(new Callback<Medicine>() {
            public void onResponse(Call<Medicine> call, final Response<Medicine> response) {
                progressbar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Picasso.with(MedicineDetailsActivity.this.getApplicationContext())
                            .load(response.body().getMeta_data().getImage_url())
                            .into(MedicineDetailsActivity.this.imgv_medicine);
                    txtv_medicine_name.setText(" " + response.body().getMeta_data().getName());
                    txtv_medicine_price.setText("৳" + response.body().getMeta_data().getPrice() + " Tk");
                    txtv_medicine_brand.setText(" " + response.body().getMeta_data().getBrand());
                    txtv_medicine_features.setText(response.body().getMeta_data().getFeatures());
                    txtv_medicine_indication.setText(response.body().getMeta_data().getIndication());
                    txtv_medicine_description.setText(response.body().getMeta_data().getDescription());
                    if (MedicineDetailsActivity.this.getAllMedicineFromCartInRoomDB() != null) {
                        for (int i = 0; i < MedicineDetailsActivity.this.getAllMedicineFromCartInRoomDB().size(); i++) {
                            if (response.body().getId().equalsIgnoreCase(((MedicineEntityRoom.Medicine)
                                    getAllMedicineFromCartInRoomDB().get(i)).getMedicineId())) {
                               btn_add_to_cart.setText("Remove");
                            }
                        }
                    }
                    MedicineDetailsActivity.this.btn_order_now.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            //Toast.makeText(MedicineDetailsActivity.this, "Order Clicked.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MedicineDetailsActivity.this, PharmacyActivity.class)
                                    .putExtra("medicine_id", ((Medicine) response.body()).getId())
                                    .putExtra("medicine_name", ((Medicine) response.body()).getMeta_data().getName())
                                    .putExtra("medicine_image_url", ((Medicine) response.body()).getMeta_data().getImage_url())
                                    .putExtra("medicine_features", ((Medicine) response.body()).getMeta_data().getFeatures())
                                    .putExtra("medicine_price", ((Medicine) response.body()).getMeta_data().getPrice())
                                    .putExtra("medicine_brand", ((Medicine) response.body()).getMeta_data().getBrand()));
                        }
                    });
                    MedicineDetailsActivity.this.btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            if (btn_add_to_cart.getText().toString().equalsIgnoreCase("Add to Cart")) {
                                btn_add_to_cart.setText("Remove");
                                insertMedicineToCartInRoomDB((Medicine) response.body());
                                return;
                            }
                            btn_add_to_cart.setText("Add to Cart");
                            deleteMedicineFromCartInRoomDB(((Medicine) response.body()).getId());
                        }
                    });
                    return;
                }
                Log.d(TAG, "onResponse: ResponseError: " + response.errorBody());
            }

            public void onFailure(Call<Medicine> call, Throwable t) {
                progressbar.setVisibility(View.GONE);
                Log.d(TAG, "onFailure: Error: " + t.getMessage());
            }
        });
    }

    /* access modifiers changed from: private */
    public void insertMedicineToCartInRoomDB(Medicine medicine) {
        Medicine.MetaData selectedMedicineMetaData = medicine.getMeta_data();
        MedicineDatabase.getInstance(getApplicationContext()).medicineDao()
                .insertMedicine(new MedicineEntityRoom.Medicine(medicine.getId(),
                        new MedicineEntityRoom.MetaData(
                                selectedMedicineMetaData.getName(),
                                selectedMedicineMetaData.getImage_url(),
                                selectedMedicineMetaData.getFeatures(),
                                selectedMedicineMetaData.getBrand(),
                                selectedMedicineMetaData.getIndication(),
                                selectedMedicineMetaData.getPrice(),
                                selectedMedicineMetaData.getDescription())));
    }

    /* access modifiers changed from: private */
    public void deleteMedicineFromCartInRoomDB(String medicineId) {
        MedicineDatabase.getInstance(getApplicationContext()).medicineDao().deleteMedicine(medicineId);
    }

    /* access modifiers changed from: private */
    public List<MedicineEntityRoom.Medicine> getAllMedicineFromCartInRoomDB() {
        return MedicineDatabase.getInstance(getApplicationContext()).medicineDao().getAllMedicine();
    }
}
