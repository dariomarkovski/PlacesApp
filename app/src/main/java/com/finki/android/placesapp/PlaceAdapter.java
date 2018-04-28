package com.finki.android.placesapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.finki.android.placesapp.model.Place;

import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.MyViewHolder> {

    private List<Place> placeList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, rating;

        public MyViewHolder(View view){
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            rating = (TextView) view.findViewById(R.id.rating);
        }
    }

    public PlaceAdapter(List<Place> placeList) { this.placeList = placeList; }

    @NonNull
    @Override
    public PlaceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_view, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceAdapter.MyViewHolder holder, int position) {
        Place place = placeList.get(position);
        holder.name.setText(place.name);
        holder.rating.setText("Rating: "+Double.toString(place.rating));
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    public Place getPlace(int position) { return placeList.get(position); }
}
