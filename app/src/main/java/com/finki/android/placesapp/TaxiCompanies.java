package com.finki.android.placesapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.finki.android.placesapp.model.TaxiCompany;

import java.util.ArrayList;
import java.util.List;

public class TaxiCompanies extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taxi_companies);

        List<TaxiCompany> taxiCompanyList = new ArrayList<>();
        taxiCompanyList.add(new TaxiCompany("Vardar Taxi", "+38972302222"));
        taxiCompanyList.add(new TaxiCompany("Global Taxi", "+38978415180"));
        taxiCompanyList.add(new TaxiCompany("In Taxi", "+38978215551"));
        taxiCompanyList.add(new TaxiCompany("Vodno Taxi", "+38978215191"));

        TaxiCompanyAdapter taxiCompanyAdapter = new TaxiCompanyAdapter(taxiCompanyList, getApplicationContext());
        RecyclerView recyclerView = findViewById(R.id.taxi_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(taxiCompanyAdapter);

    }
}
