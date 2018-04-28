package com.finki.android.placesapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.finki.android.placesapp.model.Place;
import com.finki.android.placesapp.persistence.AppDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.MyViewHolder> {

    public static String UPDATE_ADAPTER = "com.finki.android.placesapp.placeadapter.UPDATE_ADAPTER";
    private List<Place> placeList;
    private Context context;
    private Intent intent;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, rating;
        public Button viewOnMapButton, deleteButton;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            rating = (TextView) view.findViewById(R.id.rating);
            viewOnMapButton = view.findViewById(R.id.viewOnMapButton);
            deleteButton = view.findViewById(R.id.deleteButton);

        }
    }

    public PlaceAdapter(List<Place> placeList, Context context, Intent intent) {
        this.placeList = placeList;
        this.context = context;
        this.intent = intent;
    }

    @NonNull
    @Override
    public PlaceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_view, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceAdapter.MyViewHolder holder, int position) {
        final Place place = placeList.get(position);
        holder.name.setText(place.name);
        holder.rating.setText("Rating: " + Double.toString(place.rating));
        holder.viewOnMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Place> placeList = new ArrayList<Place>();
                placeList.add(place);

                Double currentLatitude = intent.getDoubleExtra("currentLatitude", 21.42);
                Double currentLongitude = intent.getDoubleExtra("currentLongitude", 41.99);

                Intent mapIntent = new Intent(context, MapsActivity.class);
                mapIntent.putExtra("currentLatitude", currentLatitude);
                mapIntent.putExtra("currentLongitude", currentLongitude);
                mapIntent.putExtra("placeList", (Serializable) placeList);
                context.startActivity(mapIntent);
            }
        });
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDatabase appDatabase = AppDatabase.getAppDatabase(context);
                appDatabase.placeDao().delete(place);
                Intent broadcastIntent = new Intent(UPDATE_ADAPTER);
                context.sendBroadcast(broadcastIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    public Place getPlace(int position) {
        return placeList.get(position);
    }
}
