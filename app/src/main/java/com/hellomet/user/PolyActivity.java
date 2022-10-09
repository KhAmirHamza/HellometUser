package com.hellomet.user;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import java.util.Arrays;
import java.util.List;

public class PolyActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnPolylineClickListener, GoogleMap.OnPolygonClickListener {
    private static final int COLOR_BLUE_ARGB = -415707;
    private static final int COLOR_GREEN_ARGB = -13070788;
    private static final int COLOR_ORANGE_ARGB = -688361;
    private static final int COLOR_PURPLE_ARGB = -8271996;
    private static final PatternItem DOT;
    private static final PatternItem GAP;
    private static final int PATTERN_GAP_LENGTH_PX = 20;
    private static final List<PatternItem> PATTERN_POLYGON_DOTTED;
    private static final List<PatternItem> PATTERN_POLYLINE_DOTTED;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView((int) R.layout.activity_poly);
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
    }

    public void onMapReady(GoogleMap googleMap) {
        Polyline polyline1 = googleMap.addPolyline(new PolylineOptions().clickable(true).add(new LatLng(-35.016d, 143.321d), new LatLng(-34.747d, 145.592d), new LatLng(-34.364d, 147.891d), new LatLng(-33.501d, 150.217d), new LatLng(-32.306d, 149.248d), new LatLng(-32.491d, 147.309d)));
        polyline1.setColor(COLOR_BLUE_ARGB);
        polyline1.setTag("A");
        Polygon polygon1 = googleMap.addPolygon(new PolygonOptions().clickable(true).add(new LatLng(-27.457d, 153.04d), new LatLng(-33.852d, 151.211d), new LatLng(-37.813d, 144.962d), new LatLng(-34.928d, 138.599d)));
        polygon1.setStrokeColor(COLOR_GREEN_ARGB);
        polygon1.setTag("alpha");
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-23.684d, 133.903d), 4.0f));
        googleMap.setOnPolylineClickListener(this);
        googleMap.setOnPolygonClickListener(this);
    }

    static {
        Dot dot = new Dot();
        DOT = dot;
        Gap gap = new Gap(20.0f);
        GAP = gap;
        PATTERN_POLYLINE_DOTTED = Arrays.asList(new PatternItem[]{gap, dot});
        PATTERN_POLYGON_DOTTED = Arrays.asList(new PatternItem[]{new Gap(10.0f), dot});
    }

    public void onPolylineClick(Polyline polyline) {
        if (polyline.getPattern() == null || !polyline.getPattern().contains(DOT)) {
            polyline.setPattern(PATTERN_POLYLINE_DOTTED);
        } else {
            polyline.setPattern((List<PatternItem>) null);
        }
        Toast.makeText(this, "Route type " + polyline.getTag().toString(), Toast.LENGTH_SHORT).show();
    }

    public void onPolygonClick(Polygon polygon) {
        if (polygon.getStrokePattern() == null || !polygon.getStrokePattern().contains(DOT)) {
            polygon.setStrokePattern(PATTERN_POLYGON_DOTTED);
        } else {
            polygon.setStrokePattern((List<PatternItem>) null);
        }
        Toast.makeText(this, "Route type " + polygon.getTag().toString(), Toast.LENGTH_SHORT).show();
    }
}
