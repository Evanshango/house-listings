package com.evans.multiimageupload.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.evans.multiimageupload.DetailActivity;
import com.evans.multiimageupload.R;
import com.evans.multiimageupload.adapters.HouseAdapter;
import com.evans.multiimageupload.models.House;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.evans.multiimageupload.helpers.Constants.HOUSES;

public class HouseFragment extends Fragment implements HouseAdapter.HouseItemClick {

    private static final String TAG = "HouseFragment";
    private RecyclerView houseRecycler;
    private List<House> mHouses = new ArrayList<>();
    private DatabaseReference houseRef;
    private HouseAdapter mHouseAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_house, container, false);

        initViews(view);

        return view;
    }

    private void initViews(View view) {
        houseRecycler = view.findViewById(R.id.houseRecycler);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        houseRef = FirebaseDatabase.getInstance().getReference(HOUSES);

        mHouseAdapter = new HouseAdapter(mHouses, this);

        getHouses();
    }

    private void getHouses() {
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        houseRecycler.setHasFixedSize(true);
        houseRecycler.setLayoutManager(manager);

        houseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mHouses.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    House house = snapshot.getValue(House.class);
                    mHouses.add(house);
                }
                houseRecycler.setAdapter(mHouseAdapter);
                mHouseAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: Error " + databaseError);
            }
        });
    }

    @Override
    public void itemClick(House house) {
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra("house", house);
        startActivity(intent);
    }
}
