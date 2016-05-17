package com.example.niels.android;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapEvenement extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    double lat = 0.0;
    double lon = 0.0;
    ArrayList<String> paramEve = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_evenement);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle b = getIntent().getExtras();
       // paramEve = (ArrayList<String>)b.get("array");

        TextView nom = (TextView) findViewById(R.id.textView);
        TextView desc = (TextView) findViewById(R.id.textView2);
        TextView date = (TextView) findViewById(R.id.textView3);

        nom.setText((String) b.get("nom"));
        String de = (String) b.get("desc");
        String da = (String) b.get("date");
        desc.setText(getString(R.string.description) + " : "+de);
        date.setText(getString(R.string.date) + " : "+da);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Bundle b = getIntent().getExtras();
        paramEve = (ArrayList<String>)b.get("array");
        lat = Double.parseDouble((String)b.get("lat"));
        lon = Double.parseDouble((String)b.get("lon"));
        Log.e("LAT LON : ",lat+" "+lon);

        // Add a marker in Sydney and move the camera
        LatLng ici = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(ici).title(getString(R.string.location_event)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ici));
    }
}
