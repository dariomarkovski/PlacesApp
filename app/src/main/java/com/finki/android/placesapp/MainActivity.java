package com.finki.android.placesapp;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.finki.android.placesapp.model.Place;
import com.finki.android.placesapp.service.GetPlacesListService;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    final Context self = this;
    final Activity selfActivity = this;

    LocationManager locationManager;
    PlacesResultBroadcastReceiver placesResultBroadcastReceiver;

    Location currentLocation;

    List<Place> placeList;
    boolean mLocationPermissionGranted = false;
    boolean askedForPermission = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        placesResultBroadcastReceiver = new PlacesResultBroadcastReceiver();

        Spinner spinner = findViewById(R.id.placeTypeSpinner);
        String[] items = new String[]{"Restaurant", "Hospital", "Bar"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);

        SeekBar seekBar = findViewById(R.id.placeDistanceSeekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView textView = findViewById(R.id.placeDistanceTextView);
                DecimalFormat decimalFormat = new DecimalFormat("#0.00");
                String distanceString = "Distance: \n" + decimalFormat.format(progress * 0.1) + "km";
                textView.setText(distanceString);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBar.setProgress(50);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(GetPlacesListService.FETCH_FINISHED);
        this.registerReceiver(placesResultBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(placesResultBroadcastReceiver);
    }

    public void searchClicked(View view) {
        callForService();
    }

    public void favouritesClicked(View view) {
        if (ActivityCompat.checkSelfPermission(self, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location location = currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            location = new Location("HOME");
            location.setLongitude(21.42);
            location.setLatitude(41.99);
            currentLocation = location;
            Intent intent = new Intent(this, FavouritesActivity.class);
            intent.putExtra("currentLatitude", currentLocation.getLatitude());
            intent.putExtra("currentLongitude", currentLocation.getLongitude());
            startActivity(intent);
        } else {
            if (!askedForPermission) {
                askedForPermission = true;
                ActivityCompat.requestPermissions(selfActivity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    callForService();
                }
            }
        }
    }

    private void callForService() {
        EditText editText = findViewById(R.id.placeNameEditText);
        Spinner spinner = findViewById(R.id.placeTypeSpinner);
        SeekBar seekBar = findViewById(R.id.placeDistanceSeekBar);
        if (ActivityCompat.checkSelfPermission(self, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            Location location = currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Intent intent = new Intent(self, GetPlacesListService.class);
            location = new Location("HOME");
            location.setLongitude(21.42);
            location.setLatitude(41.99);
            currentLocation = location;
            intent.putExtra(GetPlacesListService.LOCATION_EXTRA, location);
            intent.putExtra(GetPlacesListService.NAME_EXTRA, editText.getText().toString());
            intent.putExtra(GetPlacesListService.RADIUS_EXTRA, (double) seekBar.getProgress() * 100);
            intent.putExtra(GetPlacesListService.TYPES_EXTRA, (String) spinner.getItemAtPosition(spinner.getSelectedItemPosition()));
            startService(intent);
        } else {
            if (!askedForPermission) {
                askedForPermission = true;
                ActivityCompat.requestPermissions(selfActivity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        }
    }

    private class PlacesResultBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                placeList = (ArrayList<Place>) intent.getSerializableExtra(GetPlacesListService.FETCH_FINISHED_RESULT);
                Intent mapIntent = new Intent(context, MapsActivity.class);
                mapIntent.putExtra("currentLatitude", currentLocation.getLatitude());
                mapIntent.putExtra("currentLongitude", currentLocation.getLongitude());
                mapIntent.putExtra("placeList", (Serializable)placeList);
                startActivity(mapIntent);
            } catch (Exception e) {
                Log.e("ERROR: ", e.getMessage());
            }
        }
    }
}
