package com.finki.android.placesapp.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class TypeConverter {

    @android.arch.persistence.room.TypeConverter
    public String typesToString(List<String> types) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {}.getType();
        String json = gson.toJson(types, type);
        return json;
    }

    @android.arch.persistence.room.TypeConverter
    public List<String> stringToTypes(String types) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {}.getType();
        List<String> list = gson.fromJson(types, type);
        return list;
    }

}
