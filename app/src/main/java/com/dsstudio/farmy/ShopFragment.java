package com.dsstudio.farmy;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ShopFragment extends Fragment {
    RecyclerView recyclerView;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://plant-diseases-classifier-default-rtdb.firebaseio.com/");
    ShopAdapter shopAdapter;
    ArrayList<Shop> list;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_shop, container, false);

        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        list = new ArrayList<>();
        shopAdapter = new ShopAdapter(getContext(), list);
        recyclerView.setAdapter(shopAdapter);


        databaseReference.child("shops").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Shop shop = new Shop();
                    shop.setContactNumber(dataSnapshot.child("contact").getValue().toString());
                    shop.setShopDescription(dataSnapshot.child("description").getValue().toString());
                    shop.setShopLocation(dataSnapshot.child("location").getValue().toString());
                    shop.setShopName(dataSnapshot.child("name").getValue().toString());
                    list.add(shop);
                }
                shopAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return v;
    }
}