package com.finki.android.placesapp.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;

@Entity(tableName = "places")
public class Place implements Serializable {
    @Embedded
    public Geometry geometry;

    @PrimaryKey
    @NonNull
    public String id;

    public String name;

    public double rating;
}
