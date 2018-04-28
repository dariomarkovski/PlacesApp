package com.finki.android.placesapp.persistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.finki.android.placesapp.model.Place;

import java.util.List;

@Dao
public interface PlaceDao {

    @Query("SELECT * FROM Places")
    List<Place> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Place place);

    @Delete
    void delete(Place place);
}
