package com.example.waterplants.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.waterplants.Adapter.ExtraAdapter;
import com.example.waterplants.ExtraFlower;
import com.example.waterplants.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thefinestartist.finestwebview.FinestWebView;

import java.util.ArrayList;
import java.util.List;


public class ExtraFragment extends Fragment {

    private ListView listView;
    private static List<ExtraFlower> extraFlowers=null;
    private ExtraAdapter extraAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_extra, container, false);
        listView= view.findViewById(R.id.list_view);
        extraFlowers=new ArrayList<>();
        readFlowers();
        extraAdapter=new ExtraAdapter(getContext(),extraFlowers);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ExtraFlower fl=extraFlowers.get(i);
                new FinestWebView.Builder(getContext()).show(fl.getSite());
            }
        });
        return view;

    }
    private void readFlowers() {

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("ExtraFlowers");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                extraFlowers.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    ExtraFlower extraFlower=snapshot.getValue(ExtraFlower.class);
                    assert extraFlower !=null;
                    extraFlowers.add(extraFlower);
                    listView.setAdapter(extraAdapter);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

}
