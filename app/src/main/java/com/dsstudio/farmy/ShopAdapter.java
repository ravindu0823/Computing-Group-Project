package com.dsstudio.farmy;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.MyViewHolder> {
    Context context;
    ArrayList<Shop> list;

    public ShopAdapter(Context context, ArrayList<Shop> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.shop_item, parent, false);


        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Shop shop = list.get(position);

        holder.shopName.setText(shop.getShopName());
        holder.shopDescription.setText(shop.getShopDescription());
        holder.shopLocation.setText(shop.getShopLocation());
        holder.contactNumber.setText(shop.getContactNumber());

        holder.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(android.net.Uri.parse("tel:" + shop.getContactNumber()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView shopName, shopDescription, shopLocation, contactNumber;
        Button callButton;

        public MyViewHolder(@NonNull View view) {
            super(view);

            shopName = view.findViewById(R.id.shop_name);
            shopDescription = view.findViewById(R.id.shop_description);
            shopLocation = view.findViewById(R.id.shop_location);
            contactNumber = view.findViewById(R.id.shop_contact);
            callButton = view.findViewById(R.id.shop_contact_button);

        }
    }
}