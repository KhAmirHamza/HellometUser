package com.hellomet.user.View;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

import com.hellomet.user.R;
import com.hellomet.user.View.Fragment.AccountFragment;
import com.hellomet.user.View.Fragment.CartFragment;
import com.hellomet.user.View.Fragment.MedicineFragment;
import com.hellomet.user.View.Fragment.OrderFragment;

import static com.hellomet.user.Constants.API_KEY;
import static com.hellomet.user.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";

    final Fragment accountFragment = new AccountFragment();
    final Fragment cartFragment = new CartFragment();
    final Fragment medicineFragment = new MedicineFragment();
    final Fragment orderFragment = new OrderFragment();
    Fragment activeFragment = medicineFragment;

    FragmentManager fragmentManager;
    BottomNavigationView home_btm_nav;
    FrameLayout home_content_layout;
    Location lastKnownLocation;
    private LocationCallback locationCallback;
    /* access modifiers changed from: private */
    public boolean locationPermissionGranted;
    Task<Location> locationTask;
    FusedLocationProviderClient mFusedLocationProviderClient;
    private PlacesClient placesClient;
    SwipeRefreshLayout swiperefresh;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_home);
        home_content_layout = (FrameLayout) findViewById(R.id.home_content_layout);
        home_btm_nav = (BottomNavigationView) findViewById(R.id.home_btm_nav);
        fragmentManager = getSupportFragmentManager();
        Places.initialize(this, API_KEY);
        placesClient = Places.createClient(this);







        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient((Activity) this);
        locationCallback = new LocationCallback() {
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    for (Location location : locationResult.getLocations()) {
                        lastKnownLocation = location;
                    }
                }
            }
        };

        home_btm_nav.setOnNavigationItemSelectedListener(getBottomNavigationSelectedListener());
        fragmentManager.beginTransaction().add(R.id.home_content_layout, accountFragment, "4").hide(accountFragment).commit();
        fragmentManager.beginTransaction().add(R.id.home_content_layout, orderFragment, "3").hide(orderFragment).commit();
        fragmentManager.beginTransaction().add(R.id.home_content_layout, cartFragment, "2").hide(cartFragment).commit();
        fragmentManager.beginTransaction().add(R.id.home_content_layout, medicineFragment, "1").commit();
        getLocationPermission();
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swiperefresh = swipeRefreshLayout;
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.detach(activeFragment);
                ft.attach(activeFragment);
                ft.commit();
                swiperefresh.setRefreshing(false);
            }
        });
        String requestFragment = getIntent().getStringExtra("fragment");
        if (requestFragment != null && requestFragment.equalsIgnoreCase("order")) {
            fragmentManager.beginTransaction().hide(activeFragment).show(orderFragment).commit();
            activeFragment = orderFragment;
            home_btm_nav.getMenu().getItem(2).setChecked(true);
            setTitle(home_btm_nav.getMenu().getItem(2).getTitle());
        }
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0) {
            locationPermissionGranted = true;
            //Toast.makeText(this, "True", Toast.LENGTH_SHORT).show();
            createLocationRequest();
            return;
        }
       // Toast.makeText(this, "False", Toast.LENGTH_SHORT).show();
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
       locationPermissionGranted = false;
        if (requestCode == 9002 && grantResults.length > 0 && grantResults[0] == 0) {
            locationPermissionGranted = true;
            //Toast.makeText(this, "True", Toast.LENGTH_SHORT).show();
            createLocationRequest();
        }
    }

    /* access modifiers changed from: protected */
    public void createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(100);
        Task<LocationSettingsResponse> task = LocationServices.getSettingsClient((Activity) this)
                .checkLocationSettings(new LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build());
        task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                locationPermissionGranted = true;
                getDeviceLocation();
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            public void onFailure(Exception e) {
                try {
                    ((ResolvableApiException) e).startResolutionForResult(HomeActivity.this, 1);
                } catch (IntentSender.SendIntentException e2) {
                }
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == -1) {
            //Toast.makeText(this, "Well,All Location settings are satisfied now!", Toast.LENGTH_SHORT).show();
            this.locationPermissionGranted = true;
            updateLocationUI();
        }
        if (requestCode == 1 && resultCode == 0) {
            Toast.makeText(this, "Please turn on Location with High Accuracy", Toast.LENGTH_LONG).show();
            startActivityForResult(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"), 2);
        }
        if (requestCode == 2 && resultCode == -1) {
            if (Build.VERSION.SDK_INT < 23) {
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled("gps") || !locationManager.isProviderEnabled("network")) {
                    Toast.makeText(this, "High Accuracy Location permission required", Toast.LENGTH_LONG).show();
                    createLocationRequest();
                } else {
                    this.locationPermissionGranted = true;
                    //Toast.makeText(this, "Everything is ok", Toast.LENGTH_SHORT).show();
                    updateLocationUI();
                }
            } else if (checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission("android.permission.ACCESS_FINE_LOCATION") == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(this, "Everything is ok", Toast.LENGTH_SHORT).show();
                this.locationPermissionGranted = true;
                updateLocationUI();
            } else {
                Toast.makeText(this, "High Accuracy Location permission required", Toast.LENGTH_SHORT).show();
                createLocationRequest();
            }
        }
        if (requestCode == 2 && resultCode == 0) {
            Toast.makeText(this, "Location permission required", Toast.LENGTH_SHORT).show();
            this.locationPermissionGranted = false;
            createLocationRequest();
        }
    }

    /* access modifiers changed from: private */
    public void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: called");
        try {
            if (this.locationPermissionGranted) {
                Task<Location> lastLocation = this.mFusedLocationProviderClient.getLastLocation();
                this.locationTask = lastLocation;
                lastLocation.addOnCompleteListener((Activity) this, (OnCompleteListener<Location>) new OnCompleteListener<Location>() {
                    public void onComplete(Task<Location> task) {
                        if (!task.isSuccessful() || !task.isComplete()) {
                            Log.d(HomeActivity.TAG, "Current location is null. Using defaults.");
                            Log.e(HomeActivity.TAG, "Exception: %s", task.getException());
                            return;
                        }
                        //Toast.makeText(HomeActivity.this, "lastKnownLocation Changed", Toast.LENGTH_SHORT).show();
                        HomeActivity.this.lastKnownLocation = task.getResult();
                        if (HomeActivity.this.lastKnownLocation != null) {
                            //Toast.makeText(HomeActivity.this, "New Location Detected.", Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor editor = HomeActivity.this.getSharedPreferences("LOCATION", 0).edit();
                            editor.putString("Lat", String.valueOf(HomeActivity.this.lastKnownLocation.getLatitude()));
                            editor.putString("Lng", String.valueOf(HomeActivity.this.lastKnownLocation.getLongitude()));
                            editor.apply();
                            return;
                        }
                        //Toast.makeText(HomeActivity.this, "Using Latest Location.", Toast.LENGTH_SHORT).show();
                    }
                });
                return;
            }
           // Toast.makeText(this, "Location not permitted", Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    private void updateLocationUI() {
        try {
            if (!this.locationPermissionGranted) {
                this.lastKnownLocation = null;
                createLocationRequest();
            } else if (this.lastKnownLocation == null) {
                getDeviceLocation();
            } else {
                SharedPreferences.Editor editor = getSharedPreferences("LOCATION", 0).edit();
                editor.putString("Lat", String.valueOf(this.lastKnownLocation.getLatitude()));
                editor.putString("Lng", String.valueOf(this.lastKnownLocation.getLongitude()));
                editor.apply();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    public void setFragmentToContentViewBottomNav(MenuItem menuItem) {
        Fragment fragment;
        //Toast.makeText(this, menuItem.getTitle().toString(), Toast.LENGTH_SHORT).show();
        switch (menuItem.getItemId()) {
            case R.id.home_account /*2131230939*/:
                fragment = accountFragment;
                break;
            case R.id.home_cart /*2131230941*/:
                fragment = cartFragment;
                break;
            case R.id.home_medicine /*2131230943*/:
                fragment = medicineFragment;
                break;
            case R.id.home_order /*2131230944*/:
                fragment = orderFragment;
                break;
            default:
                fragment = medicineFragment;
                break;
        }
        if (fragment == accountFragment) {
            SharedPreferences sharedPreferences = getSharedPreferences("AUTHENTICATION", 0);
            if (FirebaseAuth.getInstance().getCurrentUser() == null && sharedPreferences.getString("USER_PHONE_NUMBER", "null").equalsIgnoreCase("null")) {
             //   startActivity(new Intent(this, SignInActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                return;
            }
        }
        fragmentManager.beginTransaction().hide(activeFragment).show(fragment).commit();
        //Toast.makeText(this, "Active: " + this.activeFragment.getTag() + "\nSelected: " + fragment.getTag(), Toast.LENGTH_SHORT).show();
        activeFragment = fragment;
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
    }

    public BottomNavigationView.OnNavigationItemSelectedListener getBottomNavigationSelectedListener() {
        return new BottomNavigationView.OnNavigationItemSelectedListener() {
            public boolean onNavigationItemSelected(MenuItem item) {
                setFragmentToContentViewBottomNav(item);
                return true;
            }
        };
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle outState) {
        Location location = this.lastKnownLocation;
        if (location != null) {
            outState.putParcelable(FirebaseAnalytics.Param.LOCATION, location);
        }
        super.onSaveInstanceState(outState);
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        createLocationRequest();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (this.lastKnownLocation != null) {
            startLocationUpdates();
        }
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0 ||
                ActivityCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setInterval(10000);
            locationRequest.setFastestInterval(5000);
            locationRequest.setPriority(100);
            mFusedLocationProviderClient.requestLocationUpdates(locationRequest, this.locationCallback, Looper.getMainLooper());
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        this.mFusedLocationProviderClient.removeLocationUpdates(this.locationCallback);
    }
}
