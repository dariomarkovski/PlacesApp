package com.finki.android.placesapp;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Activity context;

    public CustomInfoWindowAdapter(Activity context){
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = context.getLayoutInflater().inflate(R.layout.customwindow, null);

        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(marker.getTitle());

        if(marker.getTitle().equals("You Are Here")) {
            TextView clickMeText = (TextView) view.findViewById(R.id.clickMeText);
            clickMeText.setText("");
        }

        return view;
    }
}
