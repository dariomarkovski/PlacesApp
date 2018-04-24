package com.finki.android.placesapp.service;

import com.finki.android.placesapp.model.GoogleApiResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GooglePlacesApiService {

    @GET("maps/api/place/nearbysearch/json?key=AIzaSyCqXTsZ14R3kkGiOlnMqoNCLlTTlogVn9Y")
    Call<GoogleApiResult> getPlaces(@Query("radius") double radius, @Query("keyword") String keyword, @Query("types") String types, @Query("location") String location);

}
