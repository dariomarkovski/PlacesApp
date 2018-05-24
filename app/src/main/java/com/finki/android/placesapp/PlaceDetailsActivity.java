package com.finki.android.placesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.finki.android.placesapp.model.Place;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlaceDetailsActivity extends AppCompatActivity {

    Place place;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);
        intent = getIntent();
        place = (Place) intent.getSerializableExtra("place");

        TextView textView = findViewById(R.id.name);
        textView.setText(place.name);

        textView = findViewById(R.id.rating);
        textView.setText("" + place.rating);

        textView = findViewById(R.id.types);
        String typeString = place.types.get(0);
        if (place.types.size() > 1) {
            typeString = typeString + ", " + place.types.get(1);
        }
        textView.setText(typeString);

        TextView name = findViewById(R.id.vicinity);
        name.setText(place.vicinity);

    }

    public void showOnMapButtonClick(View view) {

        List<Place> placeList = new ArrayList<Place>();
        placeList.add(place);

        Double currentLatitude = intent.getDoubleExtra("currentLatitude", 21.42);
        Double currentLongitude = intent.getDoubleExtra("currentLongitude", 41.99);

        Intent mapIntent = new Intent(this, MapsActivity.class);
        mapIntent.putExtra("currentLatitude", currentLatitude);
        mapIntent.putExtra("currentLongitude", currentLongitude);
        mapIntent.putExtra("placeList", (Serializable) placeList);
        startActivity(mapIntent);
    }

    public void shareClick(View view){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Here is your sexy place";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Here is your sexy place");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Check this place out "+place.name + " at "+place.vicinity);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
}
