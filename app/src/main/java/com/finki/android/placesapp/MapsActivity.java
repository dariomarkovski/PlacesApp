package com.finki.android.placesapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.finki.android.placesapp.model.CustomLocation;
import com.finki.android.placesapp.model.Geometry;
import com.finki.android.placesapp.model.Place;
import com.finki.android.placesapp.persistence.AppDatabase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private AppDatabase base;
    private List<Place> placeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        base = AppDatabase.getAppDatabase(getApplicationContext());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnInfoWindowClickListener(this);

        Intent currentIntent = getIntent();
        placeList = (List<Place>) currentIntent.getSerializableExtra("placeList");
        Double currentLatitude = currentIntent.getDoubleExtra("currentLatitude", 21.42);
        Double currentLongitude = currentIntent.getDoubleExtra("currentLongitude", 41.99);

        LatLng current = new LatLng(currentLatitude, currentLongitude);

        BitmapDescriptor currentMarker = BitmapDescriptorFactory.fromResource(R.drawable.current_location_marker);

        mMap.addMarker(new MarkerOptions().position(current).title("You Are Here").icon(currentMarker));

        for(Place place : placeList){

            MarkerOptions marker = new MarkerOptions().position(new LatLng(place.geometry.location.lat, place.geometry.location.lng)).title(place.name);

            CustomInfoWindowAdapter adapter = new CustomInfoWindowAdapter(MapsActivity.this);
            mMap.setInfoWindowAdapter(adapter);

            mMap.addMarker(marker);
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 15));

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        CustomLocation location = new CustomLocation();
        location.lat = marker.getPosition().latitude;
        location.lng = marker.getPosition().longitude;

        Geometry geo = new Geometry();
        geo.location = location;

        for(Place p : placeList){
            if(p.name.equals(marker.getTitle())){
                base.placeDao().insert(p);
                Toast.makeText(this, marker.getTitle()+" was added to favourites",
                Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }
}
