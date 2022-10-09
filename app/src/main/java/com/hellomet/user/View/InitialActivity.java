package com.hellomet.user.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.google.firebase.messaging.FirebaseMessaging;
import com.hellomet.user.Adapter.SliderAdapter;
import com.hellomet.user.ApiClient;
import com.hellomet.user.ApiRequests;
import com.hellomet.user.Connectivity.ConnectivityDetection;
import com.hellomet.user.FcmNotification.MyFirebaseMessagingService;
import com.hellomet.user.Location.DeviceLocation;
import com.hellomet.user.MainActivity;
import com.hellomet.user.Model.FCM;
import com.hellomet.user.Model.Slider;
import com.hellomet.user.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hellomet.user.Constants.API_KEY;
import static com.hellomet.user.Constants.MAIN_URL;
import static com.hellomet.user.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.hellomet.user.Constants.PERMISSIONS_REQUEST_ENABLE_GPS;
import static com.hellomet.user.Constants.REQUEST_CHECK_SETTINGS;
import static com.hellomet.user.Constants.REQUEST_CHECK__MANUAL_SETTINGS;

public class InitialActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "InitialActivity";

    boolean locationPermissionGranted = false;
    DeviceLocation deviceLocation;

    TextView txtv_user_location;
    ProgressBar progressbar_user_location;
    MaterialToolbar toolbar_initial;

    private LinearLayout dotsLayout;
    int slidePosition = 0;
    ViewPager introViewPager;
    SliderAdapter sliderAdapter; //--
    Timer timer;
    private PlacesClient placesClient;
    ConnectivityDetection connectivityDetection;

    MyFirebaseMessagingService myFirebaseMessagingService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);
//startForegroundService()
        connectivityDetection = new ConnectivityDetection();
        connectivityDetection.setConnectionBroadcast(new ConnectivityDetection.ConnectionBroadcast() {
            @Override
            public void onConnectionReset(boolean connectionStatus) {

            }
        });
        toolbar_initial = findViewById(R.id.toolbar_initial);
        setSupportActionBar(toolbar_initial);

        txtv_user_location = findViewById(R.id.txtv_user_location);
        progressbar_user_location = findViewById(R.id.progressbar_user_location);

        setUpSlider();

        Places.initialize(InitialActivity.this, API_KEY);
        placesClient = Places.createClient(InitialActivity.this);
        requestDeviceLocation();

        setUpFirebaseMessagingInstanceAndNotificationChannel();
    }
    
    public void requestDeviceLocation(){
        deviceLocation = new DeviceLocation(InitialActivity.this, REQUEST_CHECK_SETTINGS,
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION, API_KEY);

        DeviceLocation.RequestCurrentDeviceLocation requestCurrentDeviceLocation =
                new DeviceLocation.RequestCurrentDeviceLocation() {
                    @Override
                    public void onSuccess(Location location) {
                        progressbar_user_location.setVisibility(View.GONE);
                        String latitude = String.valueOf(location.getLatitude());
                        String longitude = String.valueOf(location.getLongitude());

                        SharedPreferences sharedPreferences = getSharedPreferences("LOCATION", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("Lat", latitude);
                        editor.putString("Lng", longitude);
                        editor.apply();
                    }

                    @Override
                    public void currentDeviceAddress(String address) {
                        //Toast.makeText(getApplicationContext(), address, Toast.LENGTH_SHORT).show();
                        txtv_user_location.setText(address);
                        progressbar_user_location.setVisibility(View.GONE);
                        startActivity(new Intent(InitialActivity.this, HomeActivity.class));
                        finish();
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        progressbar_user_location.setVisibility(View.GONE);
                    }

                    @Override
                    public void locationRequestError(Exception e) {
                        progressbar_user_location.setVisibility(View.GONE);
                    }


                };

        deviceLocation.requestDeviceLocation(requestCurrentDeviceLocation);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //progressbar_user_location.setVisibility(View.GONE);
        if (requestCode == PERMISSIONS_REQUEST_ENABLE_GPS) {
            if (locationPermissionGranted) {
                //   checkLocationAndGPS();
            } else {
                //  checkLocationAndGPS();
            }
        }

        if (requestCode == REQUEST_CHECK_SETTINGS && resultCode == RESULT_OK) {
            //Toast.makeText(this, "Well,All Location settings are satisfied now!", Toast.LENGTH_SHORT).show();
            locationPermissionGranted = true;
            requestDeviceLocation();
        }
        if (requestCode == REQUEST_CHECK_SETTINGS && resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "Please turn on Location with High Accuracy", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, REQUEST_CHECK__MANUAL_SETTINGS);
        }

        if (requestCode == REQUEST_CHECK__MANUAL_SETTINGS && resultCode == RESULT_OK) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(this, "Everything is ok", Toast.LENGTH_SHORT).show();
                    locationPermissionGranted = true;
                    requestDeviceLocation();
                } else {
                    Toast.makeText(this, "High Accuracy Location permission required", Toast.LENGTH_LONG).show();
                    requestDeviceLocation();
                }
            } else {
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                        && locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    locationPermissionGranted = true;
                    //Toast.makeText(this, "Everything is ok", Toast.LENGTH_SHORT).show();
                    requestDeviceLocation();
                } else {
                    Toast.makeText(this, "High Accuracy Location permission required", Toast.LENGTH_LONG).show();
                    requestDeviceLocation();
                }
            }
        }
        if (requestCode == REQUEST_CHECK__MANUAL_SETTINGS && resultCode == RESULT_CANCELED) {
            requestDeviceLocation();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                    //Toast.makeText(this, "True", Toast.LENGTH_SHORT).show();
                    requestDeviceLocation();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.initial, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(InitialActivity.this, WebviewActivity.class)
        .putExtra("url", "https://www.hellomet.com/help-center/"));
        return super.onOptionsItemSelected(item);
    }

    private void setUpSlider(){
        timer = new Timer();

        //--viewpager with dot indicator...
        dotsLayout = findViewById(R.id.dotsContainer);
        introViewPager = findViewById(R.id.introViewPager);

        ApiClient.getInstance(MAIN_URL).getSlider().enqueue(new Callback<List<Slider>>() {
            @Override
            public void onResponse(Call<List<Slider>> call, Response<List<Slider>> response) {
                if (response.isSuccessful()){
                    sliderAdapter = new SliderAdapter(InitialActivity.this,response.body());
                    introViewPager.setAdapter(sliderAdapter);
                    prepareDots(response.body().size());
                    introViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        }
                        @Override
                        public void onPageSelected(int position) {
                            Log.d(TAG, "onPageSelected: "+position);
                            slidePosition = position;
                            prepareDots(response.body().size());
                        }
                        @Override
                        public void onPageScrollStateChanged(int state) {
                        }
                    });
                    createImageSlider();
                }else{
                    Log.d(TAG, "onResponse: "+response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<List<Slider>> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
    }
    private void createImageSlider() {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {

                if (slidePosition == Integer.MAX_VALUE) {
                    slidePosition = 0;
                }
                Log.d(TAG, "onPageSelected: run: "+slidePosition);
                introViewPager.setCurrentItem(slidePosition++, true);
            }
        };

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        }, 3000, 5000);

    }

    private void prepareDots(int count) {

        if (dotsLayout.getChildCount() > 0){
            dotsLayout.removeAllViews();}
        ImageView dots[] = new ImageView[count];

        for (int i = 0; i < count; i++) {
            dots[i] = new ImageView(InitialActivity.this);
            //if (customPosition==dataList.size()){ customPosition = 0;}//---->check dot position...
            if (i == slidePosition%count) {
                dots[i].setImageDrawable(ContextCompat.getDrawable(InitialActivity.this, R.drawable.active_dot));
            } else {
                dots[i].setImageDrawable(ContextCompat.getDrawable(InitialActivity.this, R.drawable.inactive_dots));
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(4, 0, 4, 0);
            dotsLayout.addView(dots[i], layoutParams);
        }
    }

    @Override
    public void onClick(View v) {

        /*if (v.getId() == R.id.liner_super_shop) {
            startActivity(new Intent(InitialActivity.this, WebviewActivity.class)
            .putExtra("url","https://www.hellomet.com/product-category/super-shop/"));
        }*/
    }

    private void sendRegistrationTokenToServer(String token, String id){
        Log.d(TAG, "onComplete: Current Token: " + token);
        Log.d(TAG, "onComplete: id: "+id);
        if (!id.equalsIgnoreCase("null")) {
            if (token == null) {
                Log.d(TAG, "onComplete: token is null!");
            } else {
                ApiClient.getInstance(MAIN_URL).getFcmToken(id).enqueue(new Callback<FCM>() {
                    @Override
                    public void onResponse(Call<FCM> call, Response<FCM> response) {
                        if (response.isSuccessful()){
                            if (response.body() == null) {
                                addToken(id, token);
                            }else{
                                updateToken(id, token);
                            }
                        }else{
                            Log.d(TAG, "onResponse: "+response.errorBody().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<FCM> call, Throwable t) {
                        Log.d(TAG, "onFailure: "+t.getMessage());
                    }
                });
            }
        }
    }

    private void addToken(String id, String token) {
        Log.d(TAG, "addToken: called");
        FCM fcm = new FCM(id, token);
        ApiRequests apiRequests = ApiClient.getInstance(MAIN_URL);
        apiRequests.createFcmToken(fcm).enqueue(new Callback<FCM>() {
            @Override
            public void onResponse(Call<FCM> call, Response<FCM> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: token: " + response.body().getToken());
                } else {
                    Log.d(TAG, "onResponse: token: " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<FCM> call, Throwable t) {
                Log.d(TAG, "onFailure: fcm : " + t.getMessage());
            }
        });
    }

    private void updateToken(String id, String token) {
        Log.d(TAG, "updateToken: called");
        FCM fcm = new FCM(id, token);
        ApiRequests apiRequests = ApiClient.getInstance(MAIN_URL);
        apiRequests.updateFcmToken(id, fcm).enqueue(new Callback<FCM>() {
            @Override
            public void onResponse(Call<FCM> call, Response<FCM> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: " + response.body().getToken());
                } else {
                    Log.d(TAG, "onResponse: " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<FCM> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }


    private void setUpFirebaseMessagingInstanceAndNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_HIGH));
        }

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        // String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, token);
                        //Toast.makeText(InitialActivity.this, token, Toast.LENGTH_SHORT).show();

                        SharedPreferences sharedPreferences = getSharedPreferences("AUTHENTICATION", Context.MODE_PRIVATE);
                        String id = sharedPreferences.getString("USER_ID", "null");
                        sendRegistrationTokenToServer(token, id);
                    }
                });
        myFirebaseMessagingService = new MyFirebaseMessagingService();
        Intent mServiceIntent = new Intent(this, myFirebaseMessagingService.getClass());
        startService(mServiceIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectivityDetection, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(connectivityDetection);
    }
}