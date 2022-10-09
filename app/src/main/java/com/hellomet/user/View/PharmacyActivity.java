package com.hellomet.user.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.stats.CodePackage;
import com.hellomet.user.Adapter.PharmacyAdapter;
import com.hellomet.user.ApiClient;
import com.hellomet.user.Model.Pharmacy;
import com.hellomet.user.R;
import com.hellomet.user.Room.MedicineDatabase;
import com.hellomet.user.Room.MedicineEntityRoom;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hellomet.user.Constants.MAIN_URL;

public class PharmacyActivity extends AppCompatActivity {
    private static final String TAG = "HomeFragment";
    EditText edt_pharmacy_search;
    LatLng latLng;
    String medicine_brand;
    String medicine_features;
    String medicine_id;
    String medicine_image_url;
    String medicine_name;
    String medicine_price;
    ProgressBar progressbar_user_location;
    RecyclerView recy_pharmacy;
    TextView txtv_user_location;
    String prescriptionImageUri;

    ProgressBar progressbar;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_pharmacy);
        prescriptionImageUri = getIntent().getStringExtra("prescriptionImageUri");

        medicine_id = getIntent().getStringExtra("medicine_id");
        medicine_name = getIntent().getStringExtra("medicine_name");
        medicine_image_url = getIntent().getStringExtra("medicine_image_url");
        medicine_features = getIntent().getStringExtra("medicine_features");
        medicine_price = getIntent().getStringExtra("medicine_price");
        medicine_brand = getIntent().getStringExtra("medicine_brand");
        edt_pharmacy_search = (EditText) findViewById(R.id.edt_pharmacy_search);
        txtv_user_location = (TextView) findViewById(R.id.txtv_user_location);
        progressbar_user_location = (ProgressBar) findViewById(R.id.progressbar_user_location);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recy_pharmacy);

        progressbar = findViewById(R.id.progressbar);

        recy_pharmacy = recyclerView;
        recyclerView.setHasFixedSize(true);
        recy_pharmacy.setLayoutManager(new LinearLayoutManager(this));
        SharedPreferences sharedPreferences = getSharedPreferences(CodePackage.LOCATION, 0);
        String lat = sharedPreferences.getString("Lat", "null");
        String lng = sharedPreferences.getString("Lng", "null");
        if (lat.equalsIgnoreCase("null") || lng.equalsIgnoreCase("null")) {
            Log.d(TAG, "onComplete: lastKnownLocationn  is null");
            //Toast.makeText(getApplicationContext(), "lastKnownLocationn  is null", Toast.LENGTH_SHORT).show();
            progressbar_user_location.setVisibility(View.GONE);
            return;
        }
        latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
        Log.d(TAG, "onComplete: Everything of location is ok");
        getAllPharmacyAndSetLocalPharmacy();
    }

    public void getAllPharmacyAndSetLocalPharmacy() {
        Log.d(TAG, "getAllPharmacyAndSetLocalPharmacy: called");
        progressbar.setVisibility(View.VISIBLE);
        ApiClient.getInstance(MAIN_URL).getPharmacies().enqueue(new Callback<List<Pharmacy>>() {
            public void onResponse(Call<List<Pharmacy>> call, Response<List<Pharmacy>> response) {
                progressbar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Log.d(PharmacyActivity.TAG, "onResponse: all pharmacy count: " + response.body().size());
                    Log.d(PharmacyActivity.TAG, "onResponse: Current Location LatLng: " + PharmacyActivity.this.latLng.latitude + "," + PharmacyActivity.this.latLng.longitude);
                    String address = PharmacyActivity.this.getAddressFromLatLng(new LatLng(PharmacyActivity.this.latLng.latitude, PharmacyActivity.this.latLng.longitude));
                    if (address != null) {
                        PharmacyActivity.this.txtv_user_location.setText(address);
                    } else {
                        Toast.makeText(PharmacyActivity.this.getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                    final List<Pharmacy> localPharmacies = PharmacyActivity.this.getLocalPharmacies(new LatLng(PharmacyActivity.this.latLng.latitude, PharmacyActivity.this.latLng.longitude), response.body(), 5.0d);
                    final PharmacyAdapter pharmacyAdapter = new PharmacyAdapter(PharmacyActivity.this.getApplicationContext(), localPharmacies);
                    recy_pharmacy.setAdapter(pharmacyAdapter);
                    progressbar_user_location.setVisibility(View.GONE);
                    pharmacyAdapter.setOnItemClickListener(new PharmacyAdapter.OnItemClickListener() {
                        public void onItemClick(View view, int position) {
                            int i = position;
                           // Toast.makeText(PharmacyActivity.this.getApplicationContext(), ((Pharmacy) localPharmacies.get(i)).getId(), Toast.LENGTH_SHORT).show();

                            if (prescriptionImageUri !=null){
                                PharmacyActivity.this.startActivity(new Intent(PharmacyActivity.this.getApplicationContext(), OrderActivity.class)
                                        .putExtra("prescriptionImageUri", prescriptionImageUri)
                                        .putExtra("pharmacyId", ((Pharmacy) localPharmacies.get(i)).getId())
                                        .putExtra("pharmacyName", ((Pharmacy) localPharmacies.get(i)).getMeta_data().getName())
                                        .putExtra("pharmacyDistance", ((Pharmacy) localPharmacies.get(i)).getMeta_data().getDistance())
                                        .putExtra("pharmacyAddress", ((Pharmacy) localPharmacies.get(i)).getMeta_data().getAddress())
                                        .putExtra("pharmacyImageUrl", ((Pharmacy) localPharmacies.get(i)).getMeta_data().getImage_url())
                                        .putExtra("pharmacy_phone_number", ((Pharmacy) localPharmacies.get(i)).getMeta_data().getPhone_number())
                                        .putExtra("pharmacy_lat", ((Pharmacy) localPharmacies.get(i)).getMeta_data().getLatitude())
                                        .putExtra("pharmacy_lng", ((Pharmacy) localPharmacies.get(i)).getMeta_data().getLongitude())
                                        .putExtra("user_lat", String.valueOf(latLng.latitude))
                                        .putExtra("user_lng", String.valueOf(latLng.longitude)));

                            } else if (PharmacyActivity.this.medicine_name != null && PharmacyActivity.this.medicine_image_url != null
                                    && PharmacyActivity.this.medicine_id != null && PharmacyActivity.this.medicine_features != null &&
                                    PharmacyActivity.this.medicine_price != null) {
                                PharmacyActivity.this.startActivity(new Intent(PharmacyActivity.this.getApplicationContext(),
                                        OrderActivity.class).putExtra("pharmacyId", ((Pharmacy) localPharmacies.get(i)).getId())
                                        .putExtra("pharmacyName", ((Pharmacy) localPharmacies.get(i)).getMeta_data().getName())
                                        .putExtra("pharmacyDistance", ((Pharmacy) localPharmacies.get(i)).getMeta_data().getDistance())
                                        .putExtra("pharmacyAddress", ((Pharmacy) localPharmacies.get(i)).getMeta_data().getAddress())
                                        .putExtra("pharmacyImageUrl", ((Pharmacy) localPharmacies.get(i)).getMeta_data().getImage_url())
                                        .putExtra("pharmacy_phone_number", ((Pharmacy) localPharmacies.get(i)).getMeta_data().getPhone_number())
                                        .putExtra("pharmacy_lat", ((Pharmacy) localPharmacies.get(i)).getMeta_data().getLatitude())
                                        .putExtra("pharmacy_lng", ((Pharmacy) localPharmacies.get(i)).getMeta_data().getLongitude())
                                        .putExtra("medicine_id", PharmacyActivity.this.medicine_id)
                                        .putExtra("medicine_name", PharmacyActivity.this.medicine_name)
                                        .putExtra("medicine_image_url", PharmacyActivity.this.medicine_image_url)
                                        .putExtra("medicine_features", PharmacyActivity.this.medicine_features)
                                        .putExtra("medicine_price", PharmacyActivity.this.medicine_price).
                                                putExtra("medicine_brand", PharmacyActivity.this.medicine_brand).
                                                putExtra("user_lat", String.valueOf(PharmacyActivity.this.latLng.latitude))
                                        .putExtra("user_lng", String.valueOf(PharmacyActivity.this.latLng.longitude)));
                            } else if (PharmacyActivity.this.getAllMedicineFromCartInRoomDB() == null ||
                                    PharmacyActivity.this.getAllMedicineFromCartInRoomDB().size() <= 0) {
                                Toast.makeText(PharmacyActivity.this, "Invalid Medicine", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(PharmacyActivity.this, HomeActivity.class));
                            } else {
                                PharmacyActivity.this.startActivity(new Intent(PharmacyActivity.this.getApplicationContext(),
                                        OrderActivity.class).putExtra("pharmacyId", ((Pharmacy) localPharmacies.get(i)).getId())
                                        .putExtra("pharmacyName", ((Pharmacy) localPharmacies.get(i)).getMeta_data().getName())
                                        .putExtra("pharmacyDistance", ((Pharmacy) localPharmacies.get(i)).getMeta_data().getDistance())
                                        .putExtra("pharmacyAddress", ((Pharmacy) localPharmacies.get(i)).getMeta_data().getAddress())
                                        .putExtra("pharmacyImageUrl", ((Pharmacy) localPharmacies.get(i)).getMeta_data().getImage_url())
                                        .putExtra("pharmacy_phone_number", ((Pharmacy) localPharmacies.get(i)).getMeta_data().getPhone_number())
                                        .putExtra("pharmacy_lat", ((Pharmacy) localPharmacies.get(i)).getMeta_data().getLatitude())
                                        .putExtra("pharmacy_lng", ((Pharmacy) localPharmacies.get(i)).getMeta_data().getLongitude())
                                        .putExtra("user_lat", String.valueOf(latLng.latitude))
                                        .putExtra("user_lng", String.valueOf(latLng.longitude)));
                            }
                        }
                    });
                    PharmacyActivity.this.edt_pharmacy_search.addTextChangedListener(new TextWatcher() {
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            PharmacyActivity.this.edt_pharmacy_search.setCursorVisible(true);
                        }

                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            //Toast.makeText(PharmacyActivity.this, "" + s, Toast.LENGTH_SHORT).show();
                            pharmacyAdapter.getFilter().filter(s);
                        }

                        public void afterTextChanged(Editable s) {
                            PharmacyActivity.this.edt_pharmacy_search.setCursorVisible(false);
                        }
                    });
                    /*if (localPharmacies != null) {
                        Toast.makeText(PharmacyActivity.this.getApplicationContext(), "Local Pharmacy Length: " + localPharmacies.size(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PharmacyActivity.this.getApplicationContext(), "Local Pharmacy is null.", Toast.LENGTH_SHORT).show();
                    }*/
                }
            }

            public void onFailure(Call<List<Pharmacy>> call, Throwable t) {
                progressbar.setVisibility(View.GONE);
                Toast.makeText(PharmacyActivity.this.getApplicationContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getAddressFromLatLng(LatLng latLng2) {
        try {
            List<Address> addresses = new Geocoder(this, Locale.getDefault())
                    .getFromLocation(latLng2.latitude, latLng2.longitude, 1);
            if (addresses.size() > 0) {
                return addresses.get(0).getAddressLine(0);
            }
            Toast.makeText(getApplicationContext(), "Unknown Address", Toast.LENGTH_SHORT).show();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Pharmacy> getLocalPharmacies(LatLng userCurrentPositionLatLng, List<Pharmacy> allPharmacy, double targetedAreaInKM) {
        List<Pharmacy> list = allPharmacy;
        Log.d(TAG, "getLocalPharmacy: called");
        List<Pharmacy> localPharmacies = new ArrayList<>();
        for (int i = 0; i < allPharmacy.size(); i++) {
            double distance = new BigDecimal(getDistanceOfAreaFromOnePlaceToAnother(
                    userCurrentPositionLatLng, new LatLng(Double.parseDouble(list.get(i).getMeta_data().getLatitude()),
                            Double.parseDouble(list.get(i).getMeta_data().getLongitude()))))
                    .setScale(3, RoundingMode.HALF_UP).doubleValue();
            Log.d(TAG, "getLocalPharmacy: Position:" + i + "   Distance: " + distance);
            if (targetedAreaInKM >= distance && list.get(i).getMeta_data().getStatus().equalsIgnoreCase("On")) {
                Pharmacy localPharmacy = list.get(i);
                localPharmacy.getMeta_data().setDistance(distance + " km");
                localPharmacies.add(localPharmacy);
            }
        }
        return localPharmacies;
    }

    public double getDistanceOfAreaFromOnePlaceToAnother(LatLng userLatLng, LatLng pharmecyLatLng) {
        Log.d(TAG, "getDistanceOfAreaFromUserToPharmacy: called");
        if (userLatLng.latitude < pharmecyLatLng.latitude) {
            double disLatKM = (pharmecyLatLng.latitude - userLatLng.latitude) * 111.0d;
            double disLngKM = 111.0d * (pharmecyLatLng.longitude - userLatLng.longitude);
            return Math.sqrt((disLatKM * disLatKM) + (disLngKM * disLngKM));
        }
        double disLatKM2 = (userLatLng.latitude - pharmecyLatLng.latitude) * 111.0d;
        double disLngKM2 = (userLatLng.longitude - pharmecyLatLng.longitude) * 111.0d;
        return Math.sqrt((disLatKM2 * disLatKM2) + (disLngKM2 * disLngKM2));
    }

    /* access modifiers changed from: private */
    public List<MedicineEntityRoom.Medicine> getAllMedicineFromCartInRoomDB() {
        return MedicineDatabase.getInstance(this).medicineDao().getAllMedicine();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("AUTHENTICATION", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("USER_ID","null");
        if (userId.equalsIgnoreCase("null")) {
            startActivity(new Intent(PharmacyActivity.this, SignInActivity.class));
            finish();
        }
    }
}
