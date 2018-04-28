package com.finki.android.placesapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.finki.android.placesapp.model.Place;
import com.finki.android.placesapp.persistence.AppDatabase;
import com.finki.android.placesapp.service.GetPlacesListService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FavouritesActivity extends AppCompatActivity {

    private AppDatabase base;
    private RecyclerView recyclerView;
    private PlaceAdapter pAdapter;
    private DeletedPlaceBroadcastReceiver deletedPlaceBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        deletedPlaceBroadcastReceiver = new DeletedPlaceBroadcastReceiver();

        base = AppDatabase.getAppDatabase(getApplicationContext());

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        setAdapterOnRecyclerView();


        /*recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        Place p = (Place) pAdapter.getPlace(position);
                        List<Place> placeList = new ArrayList<Place>();
                        placeList.add(p);

                        Intent currentIntent = getIntent();
                        Double currentLatitude = currentIntent.getDoubleExtra("currentLatitude", 21.42);
                        Double currentLongitude = currentIntent.getDoubleExtra("currentLongitude", 41.99);

                        Intent mapIntent = new Intent(getApplicationContext(), MapsActivity.class);
                        mapIntent.putExtra("currentLatitude", currentLatitude);
                        mapIntent.putExtra("currentLongitude", currentLongitude);
                        mapIntent.putExtra("placeList", (Serializable)placeList);
                        startActivity(mapIntent);
                    }
                })
        );*/

    }

    private void setAdapterOnRecyclerView(){
        Intent currentIntent = getIntent();
        pAdapter = new PlaceAdapter(base.placeDao().getAll(), getApplicationContext(), currentIntent);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(pAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(PlaceAdapter.UPDATE_ADAPTER);
        this.registerReceiver(deletedPlaceBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(deletedPlaceBroadcastReceiver);
    }

    private class DeletedPlaceBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            setAdapterOnRecyclerView();

        }
    }
}
