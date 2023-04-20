package com.dsstudio.farmy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DiseaseAdapter extends RecyclerView.Adapter<DiseaseAdapter.MyViewHolder> {
    Context context;
    ArrayList<Disease> list;

    public DiseaseAdapter(Context context, ArrayList<Disease> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.disease_item, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Disease disease = list.get(position);

        holder.diseaseName.setText(disease.getDiseaseName());
        holder.scannedDate.setText(disease.getScannedDate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView diseaseName, scannedDate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            diseaseName = itemView.findViewById(R.id.disease_name);
            scannedDate = itemView.findViewById(R.id.scanned_date);
        }
    }
}
