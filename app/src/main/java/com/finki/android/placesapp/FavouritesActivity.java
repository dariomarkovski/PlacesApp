package com.finki.android.placesapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.finki.android.placesapp.persistence.AppDatabase;

public class FavouritesActivity extends AppCompatActivity {

    private AppDatabase base;
    private RecyclerView recyclerView;
    private DeletedPlaceBroadcastReceiver deletedPlaceBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        deletedPlaceBroadcastReceiver = new DeletedPlaceBroadcastReceiver();
        base = AppDatabase.getAppDatabase(getApplicationContext());
        recyclerView = findViewById(R.id.recycler_view);
        setAdapterOnRecyclerView();

    }

    private void setAdapterOnRecyclerView() {
        Intent currentIntent = getIntent();
        PlaceAdapter pAdapter = new PlaceAdapter(base.placeDao().getAll(), getApplicationContext(), currentIntent);
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

    private class DeletedPlaceBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            setAdapterOnRecyclerView();
        }
    }
}
