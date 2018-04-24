package com.finki.android.placesapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.Nullable;
import android.util.Log;

import com.finki.android.placesapp.model.GoogleApiResult;
import com.finki.android.placesapp.model.Place;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class GetPlacesListService extends IntentService {

    public static final String FETCH_FINISHED = "com.finki.android.placesapp.FETCH_FINISHED";
    public static final String FETCH_FINISHED_RESULT = "com.finki.android.placesapp.FETCH_FINISHED_RESULT";
    public static final String LOCATION_EXTRA = "com.finki.android.placesapp.LOCATION_EXTRA";
    public static final String RADIUS_EXTRA = "com.finki.android.placesapp.RADIUS_EXTRA";
    public static final String NAME_EXTRA = "com.finki.android.placesapp.NAME_EXTRA";
    public static final String TYPES_EXTRA = "com.finki.android.placesapp.TYPES_EXTRA";

    Retrofit retrofit;
    GooglePlacesApiService googlePlacesApiService;

    public GetPlacesListService() {
        super("GetPlacesListService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        googlePlacesApiService = retrofit.create(GooglePlacesApiService.class);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            Location location = intent.getParcelableExtra(LOCATION_EXTRA);
            String loc = location.getLatitude() + "," + location.getLongitude();
            double radius = intent.getDoubleExtra(RADIUS_EXTRA, 5000);
            String name = intent.getStringExtra(NAME_EXTRA);
            String types = intent.getStringExtra(TYPES_EXTRA);
            Call<GoogleApiResult> googleApiResultCall = googlePlacesApiService.getPlaces(radius, name, types.toLowerCase(), loc);
            Response<GoogleApiResult> googleApiResultResponse = googleApiResultCall.execute();
            GoogleApiResult googleApiResult = googleApiResultResponse.body();
            ArrayList<Place> places = (ArrayList<Place>) googleApiResult.results;
            Intent broadcastIntent = new Intent(FETCH_FINISHED);
            broadcastIntent.putExtra(FETCH_FINISHED_RESULT, places);
            sendBroadcast(broadcastIntent);
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }

    }
}
