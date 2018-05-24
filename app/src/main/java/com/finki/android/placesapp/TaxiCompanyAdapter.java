package com.finki.android.placesapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.finki.android.placesapp.model.TaxiCompany;

import java.util.List;

public class TaxiCompanyAdapter extends RecyclerView.Adapter<TaxiCompanyAdapter.MyViewHolder> {

    private List<TaxiCompany> taxiCompanies;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTaxi, numberTaxi;
        public Button callButton;

        public MyViewHolder(View view) {
            super(view);
            nameTaxi = view.findViewById(R.id.nameTaxi);
            numberTaxi = view.findViewById(R.id.numberTaxi);
            callButton = view.findViewById(R.id.callButton);
        }
    }

    public TaxiCompanyAdapter(List<TaxiCompany> taxiCompanies, Context context) {
        this.taxiCompanies = taxiCompanies;
        this.context = context;
    }

    @NonNull
    @Override
    public TaxiCompanyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.taxi_company_view, parent, false);
        return new TaxiCompanyAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final TaxiCompany taxiCompany = taxiCompanies.get(position);
        holder.nameTaxi.setText(taxiCompany.name);
        holder.numberTaxi.setText("Number: " + taxiCompany.number);
        holder.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + taxiCompany.number));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return taxiCompanies.size();
    }

    public TaxiCompany getTaxiCompany(int position) {
        return taxiCompanies.get(position);
    }

}
