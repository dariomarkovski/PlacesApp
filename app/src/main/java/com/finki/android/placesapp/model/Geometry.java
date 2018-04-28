package com.finki.android.placesapp.model;

import android.arch.persistence.room.Embedded;

import java.io.Serializable;

public class Geometry implements Serializable {
    @Embedded
    public CustomLocation location;
}
