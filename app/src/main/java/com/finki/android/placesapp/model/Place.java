package com.finki.android.placesapp.model;

import java.io.Serializable;

public class Place implements Serializable {
    public Geometry geometry;
    public String id;
    public String name;
    public String icon;
    public double rating;
}
