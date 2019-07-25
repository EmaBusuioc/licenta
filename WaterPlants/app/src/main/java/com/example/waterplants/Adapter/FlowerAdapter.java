package com.example.waterplants.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.waterplants.Model.Flower;
import com.example.waterplants.PlantProfileActivity;
import com.example.waterplants.R;
import com.example.waterplants.SwipeController;

import java.util.Calendar;
import java.util.List;

public class FlowerAdapter extends RecyclerView.Adapter<FlowerAdapter.FlowerViewHolder>{
    public  Context mContext;
    public List<Flower> mFlowers;

    public class FlowerViewHolder extends RecyclerView.ViewHolder{
        public TextView flwname;
        public ImageView flwprofile;
        public TextView ultima_udare;
        public TextView interva;

        public FlowerViewHolder (View view){
            super(view);
            flwname=itemView.findViewById(R.id.flower_name);
            flwprofile=itemView.findViewById(R.id.flower_image);
            ultima_udare=itemView.findViewById(R.id.last_wt);
            interva=itemView.findViewById(R.id.inT);
        }
    }

    public FlowerAdapter(Context mContext,List<Flower>flowers){
        this.mFlowers=flowers;
        this.mContext=mContext;
    }
    @Override
    public FlowerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.flower_item, parent, false);

        return new FlowerViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(FlowerViewHolder holder, int position) {
        final Flower flower = mFlowers.get(position);
        holder.flwname.setText(flower.getFloare());
        holder.interva.setText(Integer.toString(flower.getInterval()));
        if(flower.getLast_watering()!=null){
            Calendar c= Calendar.getInstance();
            c.setTime(flower.getLast_watering());
            int zi=c.get(Calendar.DAY_OF_MONTH);
            int luna=c.get(Calendar.MONTH)+1;
            int an=c.get(Calendar.YEAR);
            int ora=c.get(Calendar.HOUR_OF_DAY);
            int minute=c.get(Calendar.MINUTE);
            if(minute<10){
                holder.ultima_udare.setText(zi+"/"+luna+"/"+an +" "+ora+":0"+minute);
            }else{
                holder.ultima_udare.setText(zi+"/"+luna+"/"+an +" "+ora+":"+minute);
            }

        }else{
            holder.ultima_udare.setText("-/-/-");
        }

        if(flower.getImageUrl().equals("default")){
            holder.flwprofile.setImageResource(R.drawable.icon_f);
        }else
        {
            Glide.with(mContext).load(flower.getImageUrl()).into(holder.flwprofile);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, PlantProfileActivity.class);
                intent.putExtra("plantName",flower.getFloare());
                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mFlowers.size();
    }


}
