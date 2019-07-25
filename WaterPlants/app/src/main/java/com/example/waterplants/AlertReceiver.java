package com.example.waterplants;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.waterplants.Model.Flower;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;


public class AlertReceiver extends BroadcastReceiver {
    private String plant_name;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference mDatabase;
    private Calendar cal;




    int interv=0;
    private static int id;
    private Date mDate;
    @Override
    public void onReceive(final Context context, Intent intent) {

        cal=Calendar.getInstance();
        cal.set(Calendar.SECOND,0);

        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = auth.getCurrentUser();
        Bundle bun = intent.getExtras();
        plant_name=bun.getString("plantName");
        String inte=bun.getString("Inter");
        id=Integer.parseInt(bun.getString("ID"));
        interv=Integer.parseInt(inte);

        NotificationHelper notificationHelper=new NotificationHelper(context, id);
        if(interv==1){
            NotificationCompat.Builder nb=notificationHelper.getChannelNotification(plant_name,
                    "Udarea a inceput, se va relua in "+interv+" zi");
            notificationHelper.getManager().notify(id,nb.build());
        }else{
            NotificationCompat.Builder nb=notificationHelper.getChannelNotification(plant_name,
                    "Udarea a inceput, se va relua in "+interv+" zile");
            notificationHelper.getManager().notify(id,nb.build());
        }

        cal.set(Calendar.SECOND,0);
        mDate=cal.getTime();
        mDatabase.child(user.getUid()).child("qFlowers").child(plant_name).child("last_watering").setValue(mDate);
        cal.add(Calendar.DATE, interv);
        //Toast.makeText(context, id+"=ID", Toast.LENGTH_SHORT).show();
        mDate=cal.getTime();
        mDatabase.child(user.getUid()).child("qFlowers").child(plant_name).child("next_watering").setValue(mDate);
        mDatabase.child(user.getUid()).child("pompa").setValue(1);

        new Thread(){
            public void run(){
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mDatabase.child(user.getUid()).child("pompa").setValue(0);
            }
        }.start();
    }



}
