package com.hellomet.user.View;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import com.hellomet.user.Adapter.OrderMedicineAdapter;
import com.hellomet.user.ApiClient;
import com.hellomet.user.ApiRequests;
import com.hellomet.user.Model.Order;
import com.hellomet.user.R;
import com.hellomet.user.Room.MedicineDatabase;
import com.hellomet.user.Room.MedicineEntityRoom;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.hellomet.user.Constants.MAIN_URL;

public class OrderActivity extends AppCompatActivity {
    private static final String TAG = "OrderActivity";
    MaterialButton btn_order_apply;
    EditText edt_order_requirement;
    EditText edt_user_address;
    ImageView imgv_pharmacy_image;
    int[] medicineItemQuantity;
    String medicine_brand;
    String medicine_features;
    String medicine_id;
    String medicine_image_url;
    String medicine_name;
    String medicine_price;
    String pharmacyAddress;
    String pharmacyDistance;
    String pharmacyId;
    String pharmacyImageUrl;
    String pharmacyName;
    String pharmacy_lat;
    String pharmacy_lng;
    String pharmacy_phone_number;
    RecyclerView recy_order_medicine;
    double totalPrice = 0.0d;
    TextView txtv_pharmacy_address;
    TextView txtv_pharmacy_distance;
    TextView txtv_pharmacy_name;
    TextView txtv_total_price;
    String user_lat;
    String user_lng;
    String prescriptionImageUri;
    MaterialCardView cardview_medicine;
    ImageView imgv_prescription;

    ProgressBar progressbar;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_order);
        prescriptionImageUri = getIntent().getStringExtra("prescriptionImageUri");

        medicine_id = getIntent().getStringExtra("medicine_id");
        medicine_name = getIntent().getStringExtra("medicine_name");
        medicine_image_url = getIntent().getStringExtra("medicine_image_url");
        medicine_features = getIntent().getStringExtra("medicine_features");
        medicine_price = getIntent().getStringExtra("medicine_price");
        medicine_brand = getIntent().getStringExtra("medicine_brand");
        pharmacyId = getIntent().getStringExtra("pharmacyId");
        pharmacyName = getIntent().getStringExtra("pharmacyName");
        pharmacyDistance = getIntent().getStringExtra("pharmacyDistance");
        pharmacyAddress = getIntent().getStringExtra("pharmacyAddress");
        pharmacyImageUrl = getIntent().getStringExtra("pharmacyImageUrl");
        pharmacyImageUrl = getIntent().getStringExtra("pharmacyImageUrl");
        pharmacy_phone_number = getIntent().getStringExtra("pharmacy_phone_number");
        pharmacy_lat = getIntent().getStringExtra("pharmacy_lat");
        pharmacy_lng = getIntent().getStringExtra("pharmacy_lng");
        user_lat = getIntent().getStringExtra("user_lat");
        user_lng = getIntent().getStringExtra("user_lng");
        imgv_pharmacy_image = (ImageView) findViewById(R.id.imgv_pharmacy_image);
        txtv_pharmacy_name = (TextView) findViewById(R.id.txtv_pharmacy_name);
        txtv_pharmacy_distance = (TextView) findViewById(R.id.txtv_pharmacy_distance);
        txtv_pharmacy_address = (TextView) findViewById(R.id.txtv_pharmacy_address);
        txtv_total_price = (TextView) findViewById(R.id.txtv_total_price);
        edt_user_address = (EditText) findViewById(R.id.edt_user_address);
        edt_order_requirement = (EditText) findViewById(R.id.edt_order_requirement);
        btn_order_apply = (MaterialButton) findViewById(R.id.btn_order_apply);

        progressbar = findViewById(R.id.progressbar);
        cardview_medicine = findViewById(R.id.cardview_medicine);
        imgv_prescription = findViewById(R.id.imgv_prescription);

        Picasso.with(getApplicationContext()).load(pharmacyImageUrl).into(imgv_pharmacy_image);
        txtv_pharmacy_name.setText(pharmacyName);
        txtv_pharmacy_distance.setText(pharmacyDistance);
        txtv_pharmacy_address.setText(pharmacyAddress);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recy_order_medicine);
        recy_order_medicine = recyclerView;
        recyclerView.setHasFixedSize(true);
        recy_order_medicine.setLayoutManager(new LinearLayoutManager(this));
        Log.d(TAG, "onCreate: medicine_name: " + medicine_name);
        Log.d(TAG, "onCreate: medicine_image_url: " + medicine_image_url);
        Log.d(TAG, "onCreate: medicine_features: " + medicine_features);
        Log.d(TAG, "onCreate: medicine_price: " + medicine_price);
        Log.d(TAG, "onCreate: medicine_id: " + medicine_id);


        if (prescriptionImageUri!=null){
            //if order with prescription...
            cardview_medicine.setVisibility(View.GONE);
            imgv_prescription.setVisibility(View.VISIBLE);
            Picasso.with(OrderActivity.this).load(prescriptionImageUri).placeholder(R.drawable.progress_animation)
                    .into(imgv_prescription);

        }else if (
            //if order with specific medicine...
                medicine_name == null || medicine_image_url == null || medicine_id == null ||
                medicine_features == null || medicine_price == null) {
            imgv_prescription.setVisibility(View.GONE);
            cardview_medicine.setVisibility(View.VISIBLE);

            final List<MedicineEntityRoom.Medicine> medicineList = getAllMedicineFromCartInRoomDB();
            medicineItemQuantity = new int[medicineList.size()];
            OrderMedicineAdapter orderMedicineAdapter = new OrderMedicineAdapter(this, medicineList);
            recy_order_medicine.setAdapter(orderMedicineAdapter);
            totalPrice = 0.0d;
            for (int i = 0; i < getAllMedicineFromCartInRoomDB().size(); i++) {
                this.totalPrice += Double.parseDouble(medicineList.get(i).getMetaData().getPrice());
                this.medicineItemQuantity[i] = 1;
                if (i == medicineList.size() - 1) {
                    this.txtv_total_price.setText("Total Price: " + this.totalPrice + " TK");
                }
            }
            orderMedicineAdapter.setOnItemClickListener(new OrderMedicineAdapter.OnItemClickListener() {
                public void onItemClick(View v, int[] medicineSelectedCount, int position) {
                    OrderActivity.this.medicineItemQuantity = medicineSelectedCount;
                    //Toast.makeText(OrderActivity.this, "Count position: " + position + "   Count: " + medicineSelectedCount[position], Toast.LENGTH_SHORT).show();
                    totalPrice = 0.0d;
                    for (int i = 0; i < OrderActivity.this.getAllMedicineFromCartInRoomDB().size(); i++) {
                        OrderActivity.this.totalPrice += ((double) medicineSelectedCount[i]) * Double.parseDouble(((MedicineEntityRoom.Medicine) medicineList.get(i)).getMetaData().getPrice());
                        if (i == medicineList.size() - 1) {
                            OrderActivity.this.txtv_total_price.setText("Total Price: " + OrderActivity.this.totalPrice + " TK");
                        }
                    }
                }
            });
        } else {
            //For Cart Medicine which contained ROOM Database...
            MedicineEntityRoom.Medicine medicine = new MedicineEntityRoom.Medicine(medicine_id,
                    new MedicineEntityRoom.MetaData(medicine_name, medicine_image_url, medicine_features,
                            "brand", "indication", medicine_price, "description"));
            final List<MedicineEntityRoom.Medicine> medicineListForOne = new ArrayList<>();
            medicineListForOne.add(medicine);
            medicineItemQuantity = new int[medicineListForOne.size()];
            OrderMedicineAdapter orderMedicineAdapter2 = new OrderMedicineAdapter(this, medicineListForOne);
            recy_order_medicine.setAdapter(orderMedicineAdapter2);
            totalPrice = 0.0d;
            for (int i2 = 0; i2 < medicineListForOne.size(); i2++) {
                totalPrice += Double.parseDouble(medicineListForOne.get(i2).getMetaData().getPrice());
                medicineItemQuantity[i2] = 1;
                if (i2 == medicineListForOne.size() - 1) {
                    txtv_total_price.setText("Total Price: " + totalPrice + " TK");
                }
            }
            orderMedicineAdapter2.setOnItemClickListener(new OrderMedicineAdapter.OnItemClickListener() {
                public void onItemClick(View v, int[] medicineSelectedCount, int position) {
                    medicineItemQuantity = medicineSelectedCount;
                    //Toast.makeText(OrderActivity.this, "Count position: " + position + "   Count: " + medicineSelectedCount[position], Toast.LENGTH_SHORT).show();
                    double totalPrice = 0.0d;
                    for (int i = 0; i < medicineListForOne.size(); i++) {
                        totalPrice += ((double) medicineSelectedCount[i]) * Double.parseDouble(((MedicineEntityRoom.Medicine) medicineListForOne.get(i)).getMetaData().getPrice());
                        if (i == medicineListForOne.size() - 1) {
                            txtv_total_price.setText("Total Price: " + totalPrice + " TK");
                        }
                    }
                }
            });
        }

        btn_order_apply.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (progressbar.getVisibility() == View.VISIBLE) {
                    Toast.makeText(OrderActivity.this, "A Task is in Progress..!", Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences sharedPreferences = getSharedPreferences("AUTHENTICATION", Context.MODE_PRIVATE);
                String user_phone_number = sharedPreferences.getString("USER_PHONE_NUMBER", "null");
                if (user_phone_number.contains("+")){
                    user_phone_number = user_phone_number.substring(1);
                }
                String user_id = sharedPreferences.getString("USER_ID", "null");
                String user_name = sharedPreferences.getString("USER_NAME", "null");
                String address = edt_user_address.getText().toString();
                if (address.isEmpty()) address = "...";
                String requirement = edt_order_requirement.getText().toString();
                if (requirement.isEmpty()) requirement = "...";

                Order.MetaData orderMetaData = new Order.MetaData("now",
                        "CashOnDelivery", "UnPaid", requirement, "Pending", String.valueOf(totalPrice), pharmacyAddress, pharmacyId, pharmacy_lat, pharmacy_lng,
                        pharmacyName, pharmacy_phone_number, user_id, user_lat, user_lng, user_name, address, user_phone_number, "...", "...", "...");
                if (user_phone_number.equalsIgnoreCase("null")) {
                    Toast.makeText(OrderActivity.this, "Please Sign In.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(OrderActivity.this, SignInActivity.class));
                    return;
                }

                Order order = new Order(orderMetaData);

                if (prescriptionImageUri != null) {
                    Log.d(TAG, "onClick: Prescription Url Available");
                    File file = generateFileFromUri(Uri.parse(prescriptionImageUri));

                    ApiRequests apiRequests =new Retrofit.Builder().baseUrl(MAIN_URL)
                            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                            .client(
                                    new OkHttpClient.Builder()
                                            .readTimeout(600, TimeUnit.SECONDS)
                                            .writeTimeout(600, TimeUnit.SECONDS)
                                            .connectTimeout(5, TimeUnit.MINUTES).build()
                            ).build().create(ApiRequests.class);

                    uploadImageFileToServerViaRestAPIusingRetrofit(apiRequests, file, order);




                }else if (
                        medicine_name == null || medicine_image_url == null ||
                        medicine_id == null ||
                        medicine_features == null || medicine_price == null) {
                    Log.d(TAG, "onClick: Medicine Items Available From Local DB");
                    List<MedicineEntityRoom.Medicine> medicineList = getAllMedicineFromCartInRoomDB();
                    List<Order.Item> items = new ArrayList<>();
                    for (int i = 0; i < medicineList.size(); i++) {
                        items.add(
                                new Order.Item(
                                        medicineList.get(i).getMedicineId(), medicineList.get(i).getMetaData().getName(),
                                        medicineList.get(i).getMetaData().getPrice(),
                                        String.valueOf(OrderActivity.this.medicineItemQuantity[i]),
                                        medicineList.get(i).getMetaData().getBrand(),
                                        medicineList.get(i).getMetaData().getFeatures(),
                                        String.valueOf(
                                                ((double) medicineItemQuantity[i]) *
                                                        Double.parseDouble(medicineList.get(i).getMetaData().getPrice()))));
                    }
//                    Log.d(TAG, "Item Size: "+order.getItems().size());
//                    Log.d(TAG, "Item Medicine Name: "+order.getItems().get(0).getName());
//                    Log.d(TAG, "Item Medicine Brand: "+order.getItems().get(0).getBrand());
                    order.setItems(items);
                    applyOrder(order);

                }else {
                    Log.d(TAG, "onClick: Medicine Items available");
                    List<Order.Item> items = new ArrayList<>();
                    items.add(
                            new Order.Item(
                                    medicine_id, medicine_name, medicine_price, String.valueOf(medicineItemQuantity[0]),
                            medicine_brand,medicine_features,
                            String.valueOf(((double) medicineItemQuantity[0]) * Double.parseDouble(medicine_price))));
                    order.setItems(items);
                    Log.d(TAG, "order: Item Size: "+order.getItems().size());
                    Log.d(TAG, "items: medicine_name: "+items.get(0).getName());
                    Log.d(TAG, "order: Items:  Medicine Name: "+order.getItems().get(0).getName());
                    Log.d(TAG, "order: Item Medicine Brand: "+order.getItems().get(0).getBrand());
                    applyOrder(order);
                }

            }
        });
    }

    /* access modifiers changed from: private */
    public void applyOrder(Order order) {
        AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this)
                .setCancelable(false)
                .setTitle("Make Sure...")
                .setMessage("Do you want to Apply Order?")
                .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(this, "Now Time to Order: " + this.totalPrice + " Tk of Item: " + order.getItems().size(), Toast.LENGTH_SHORT).show();
                        progressbar.setVisibility(View.VISIBLE);
                        //Log.d(TAG, "onClick: Item Size: "+order.getItems().size());
                        //Log.d(TAG, "onClick: Item Medicine Name: "+order.getItems().get(0).getName());
                        //Log.d(TAG, "onClick: Item Medicine Brand: "+order.getItems().get(0).getBrand());
                        ApiClient.getInstance(MAIN_URL).applyOrder(order).enqueue(new Callback<Order>() {
                            public void onResponse(Call<Order> call, Response<Order> response) {
                                progressbar.setVisibility(View.GONE);
                                if (response.isSuccessful()) {
                                    if (response.body().getMetaData() == null) {
                                        Log.d(TAG, "onResponse: Order Data is null");
                                        return;
                                    }
                                    Log.d(OrderActivity.TAG, "onResponse: Successful");
                                    Toast.makeText(OrderActivity.this, "Order Place Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(OrderActivity.this, HomeActivity.class)
                                    .putExtra("fragment","order"));
                                }else {
                                    //Toast.makeText(OrderActivity.this, "ResponseError: " + response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                                    Log.d(OrderActivity.TAG, "onResponse: ResponseError: " + response.errorBody().toString());
                                }

                            }

                            public void onFailure(Call<Order> call, Throwable t) {
                                progressbar.setVisibility(View.GONE);
                               // Toast.makeText(OrderActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d(OrderActivity.TAG, "onFailure: " + t.getLocalizedMessage());
                                Log.d(OrderActivity.TAG, "onFailure: " + t.getMessage());
                                //Log.d(OrderActivity.TAG, "onFailure: " + t.getCause().toString());
                            }
                        });
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }

    private File generateFileFromUri(Uri imageUri){
        try {
            final Bitmap selectedImageBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
            File file = new File(getApplicationContext().getFilesDir(), "Image_" + System.currentTimeMillis() + ".jpg");
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            selectedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] bitmapdata = byteArrayOutputStream.toByteArray();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            return file;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void uploadImageFileToServerViaRestAPIusingRetrofit(ApiRequests apiRequests, File imageFile, Order order){
        progressbar.setVisibility(View.VISIBLE);
        MultipartBody.Part imageMultipartBody = MultipartBody.Part.createFormData(
                "uploadImage", imageFile.getName(),
                RequestBody.create(MediaType.parse("image/*"), imageFile));



        apiRequests.uploadImageToGenarateUrl(imageMultipartBody).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressbar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.code() == 200){
                    if (response.body()==null){
                        Toast.makeText(OrderActivity.this, "Nothing Found, Try Again.", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onResponse: UploadImage Response is null.");
                    }else {
                        try {
                            final String imageUrl = new JSONObject(response.body().toString()).getString("url");
                            List<Order.PrescriptionImageURL> prescriptionImageUrls = new ArrayList<>();
                            Order.PrescriptionImageURL prescriptionImageURL = new Order.PrescriptionImageURL(imageUrl);
                            prescriptionImageUrls.add(prescriptionImageURL);
                            order.setPrescriptionImageUrls(prescriptionImageUrls);
                            //Toast.makeText(OrderActivity.this, order.getPrescriptionImageUrls().get(0), Toast.LENGTH_SHORT).show();
                            applyOrder(order);
                            //todo...

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressbar.setVisibility(View.GONE);
            }
        });
    }


    /* access modifiers changed from: private */
    public List<MedicineEntityRoom.Medicine> getAllMedicineFromCartInRoomDB() {
        return MedicineDatabase.getInstance(this).medicineDao().getAllMedicine();
    }
}
