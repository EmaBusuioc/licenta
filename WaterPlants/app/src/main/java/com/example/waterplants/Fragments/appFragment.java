package com.example.waterplants.Fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.waterplants.Adapter.FlowerAdapter;
import com.example.waterplants.AlertReceiver;
import com.example.waterplants.FirstChild;
import com.example.waterplants.Model.Flower;
import com.example.waterplants.PlantProfileActivity;
import com.example.waterplants.R;
import com.example.waterplants.SwipeController;
import com.example.waterplants.SwipeControllerActions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class appFragment extends Fragment {

    private RecyclerView recyclerView;
    private FlowerAdapter flowerAdapter;
    public static  List<Flower> flowers = null;
    SwipeController swipeController = null;
    DatabaseReference DeleteDB;
    private String pltName;
    private int id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_app,container,false);
        recyclerView=view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        flowers=new ArrayList<>();

        readFlowers();
        flowerAdapter= new FlowerAdapter(getContext(),flowers);

        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                cancelAlarm((int)flowers.get(position).getId(),flowers.get(position).getFloare());
                DeleteDB=FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getUid())
                        .child("qFlowers").child(flowers.get(position).getFloare());
                DeleteDB.removeValue();
                toastMessage(flowers.get(position).getFloare()+" a fost ștearsă");
                flowerAdapter.mFlowers.remove(position);
                flowerAdapter.notifyItemRemoved(position);
                flowerAdapter.notifyItemRangeChanged(position, flowerAdapter.getItemCount());
            }
        });
        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
        return view;
    }

    private void readFlowers() {
        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance()
                .getReference(firebaseUser.getUid()).child("qFlowers");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                flowers.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Flower flower=snapshot.getValue(Flower.class);
                    assert flower !=null;
                   flowers.add(flower);
                   recyclerView.setAdapter(flowerAdapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void toastMessage(String message){
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }
    private void cancelAlarm(int id,String nm){
        //toastMessage("Cancel id:"+id);
        AlarmManager alarmManager=(AlarmManager)getContext().getSystemService(PlantProfileActivity.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlertReceiver.class);
        intent.putExtra("plantName",nm);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(getContext(),id,intent,0);
        //interv*1000*60*24
        alarmManager.cancel(pendingIntent);
    }


}
