package com.hellomet.user;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import static com.hellomet.user.Constants.API_KEY;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int DEFAULT_ZOOM = 15;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    public static final int REQUEST_CHECK_LOCATION_SETTINGS = 1;
    public static final int REQUEST_CHECK__MANUAL_LOCATION_SETTINGS = 2;
    private static final String TAG = "MainActivity";
    /* access modifiers changed from: private */
    public final LatLng defaultLocation = new LatLng(-33.8523341d, 151.2106085d);
    Location lastKnownLocation;
    boolean locationPermissionGranted;
    LocationRequest locationRequest;
    FusedLocationProviderClient mFusedLocationProviderClient;
    /* access modifiers changed from: private */
    public GoogleMap map;
    PlacesClient placesClient;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_main);
        Places.initialize(getApplicationContext(), API_KEY);
        this.placesClient = Places.createClient(this);
        this.mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient((Activity) this);
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
    }

    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        createLocationRequest();
        getDeviceLocation();
        updateLocationUI();
    }

    /* access modifiers changed from: protected */
    public void createLocationRequest() {
        LocationRequest create = LocationRequest.create();
        this.locationRequest = create;
        create.setInterval(10000);
        this.locationRequest.setFastestInterval(5000);
        this.locationRequest.setPriority(100);
        getCurrentLocationSettings(this.locationRequest);
    }

    private void promptUserToChangeLocationSettings(Task task) {
        task.addOnSuccessListener((Activity) this, new OnSuccessListener() {
            public void onSuccess(Object o) {
                Toast.makeText(MainActivity.this, "All Location settings are satisfied", Toast.LENGTH_SHORT).show();
                MainActivity.this.locationPermissionGranted = true;
                MainActivity.this.getDeviceLocation();
                MainActivity.this.updateLocationUI();
            }
        });
        task.addOnFailureListener((Activity) this, (OnFailureListener) new OnFailureListener() {
            public void onFailure(Exception e) {
                if (e instanceof ResolvableApiException) {
                    try {
                        ((ResolvableApiException) e).startResolutionForResult(MainActivity.this, 1);
                    } catch (IntentSender.SendIntentException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    private void getCurrentLocationSettings(LocationRequest locationRequest2) {
        promptUserToChangeLocationSettings(LocationServices.getSettingsClient((Activity) this).checkLocationSettings(new LocationSettingsRequest.Builder().addLocationRequest(locationRequest2).build()));
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == -1) {
            Toast.makeText(this, "Well,All Location settings are satisfied now!", Toast.LENGTH_SHORT).show();
            this.locationPermissionGranted = true;
            getDeviceLocation();
            updateLocationUI();
        }
        if (requestCode == 1 && resultCode == 0) {
            Toast.makeText(this, "Please turn on Location with High Accuracy", Toast.LENGTH_SHORT).show();
            startActivityForResult(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"), 2);
        }
        if (requestCode == 2 && resultCode == -1) {
            Toast.makeText(this, "Thanks,All Location settings are satisfied now!", Toast.LENGTH_SHORT).show();
            this.locationPermissionGranted = true;
            getDeviceLocation();
            updateLocationUI();
        }
        if (requestCode == 2 && resultCode == 0) {
            getCurrentLocationSettings(this.locationRequest);
        }
    }

    /* access modifiers changed from: private */
    public void updateLocationUI() {
        GoogleMap googleMap = this.map;
        if (googleMap != null) {
            try {
                if (this.locationPermissionGranted) {
                    googleMap.setMyLocationEnabled(true);
                    this.map.getUiSettings().setMyLocationButtonEnabled(true);
                    return;
                }
                googleMap.setMyLocationEnabled(false);
                this.map.getUiSettings().setMyLocationButtonEnabled(false);
                this.lastKnownLocation = null;
                createLocationRequest();
            } catch (SecurityException e) {
                Log.e("Exception: %s", e.getMessage());
            }
        }
    }

    /* access modifiers changed from: private */
    public void getDeviceLocation() {
        try {
            if (this.locationPermissionGranted) {
                this.mFusedLocationProviderClient.getLastLocation().addOnCompleteListener((Activity) this, new OnCompleteListener<Location>() {
                    public void onComplete(Task<Location> task) {
                        if (task.isSuccessful()) {
                            MainActivity.this.lastKnownLocation = task.getResult();
                            Toast.makeText(MainActivity.this, "lastKnownLocation Changed.", Toast.LENGTH_SHORT).show();
                            if (MainActivity.this.lastKnownLocation != null) {
                                MainActivity.this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(MainActivity.this.lastKnownLocation.getLatitude(), MainActivity.this.lastKnownLocation.getLongitude()), 15.0f));
                                return;
                            }
                            return;
                        }
                        Log.d(MainActivity.TAG, "Current location is null. Using defaults.");
                        Log.e(MainActivity.TAG, "Exception: %s", task.getException());
                        MainActivity.this.map.moveCamera(CameraUpdateFactory.newLatLngZoom(MainActivity.this.defaultLocation, 15.0f));
                        MainActivity.this.map.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }
}
