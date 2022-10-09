package com.hellomet.user;

public class MyLocation {
/*    boolean gps_enabled = false;

    *//* renamed from: lm *//*
    LocationManager lm;
    LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location location) {
            MyLocation.this.timer1.cancel();
            MyLocation.this.locationResult.gotLocation(location);
            MyLocation.this.lm.removeUpdates(this);
            MyLocation.this.lm.removeUpdates(MyLocation.this.locationListenerNetwork);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };
    LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            timer1.cancel();
            locationResult.gotLocation(location);
            lm.removeUpdates(this);
            lm.removeUpdates(MyLocation.this.locationListenerGps);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };
    LocationResult locationResult;
    boolean network_enabled = false;
    Timer timer1;

    public static abstract class LocationResult {
        public abstract void gotLocation(Location location);
    }

    public boolean getLocation(Context context, LocationResult result) {
        this.locationResult = result;
        if (lm == null) {
            lm = (LocationManager) context.getSystemService(FirebaseAnalytics.Param.LOCATION);
        }
        try {
            this.gps_enabled = this.f133lm.isProviderEnabled("gps");
        } catch (Exception e) {
        }
        try {
            this.network_enabled = this.f133lm.isProviderEnabled("network");
        } catch (Exception e2) {
        }
        boolean z = this.gps_enabled;
        if (!z && !this.network_enabled) {
            return false;
        }
        if (z) {
            this.f133lm.requestLocationUpdates("gps", 0, 0.0f, this.locationListenerGps);
        }
        if (this.network_enabled) {
            this.f133lm.requestLocationUpdates("network", 0, 0.0f, this.locationListenerNetwork);
        }
        Timer timer = new Timer();
        this.timer1 = timer;
        timer.schedule(new GetLastLocation(), 20000);
        return true;
    }

    class GetLastLocation extends TimerTask {
        GetLastLocation() {
        }

        public void run() {
            MyLocation.this.f133lm.removeUpdates(MyLocation.this.locationListenerGps);
            MyLocation.this.f133lm.removeUpdates(MyLocation.this.locationListenerNetwork);
            Location net_loc = null;
            Location gps_loc = null;
            if (MyLocation.this.gps_enabled) {
                gps_loc = MyLocation.this.f133lm.getLastKnownLocation("gps");
            }
            if (MyLocation.this.network_enabled) {
                net_loc = lm.getLastKnownLocation("network");
            }
            if (gps_loc == null || net_loc == null) {
                if (gps_loc != null) {
                    MyLocation.this.locationResult.gotLocation(gps_loc);
                } else if (net_loc != null) {
                    MyLocation.this.locationResult.gotLocation(net_loc);
                } else {
                    MyLocation.this.locationResult.gotLocation((Location) null);
                }
            } else if (gps_loc.getTime() > net_loc.getTime()) {
                MyLocation.this.locationResult.gotLocation(gps_loc);
            } else {
                MyLocation.this.locationResult.gotLocation(net_loc);
            }
        }
    }*/
}
