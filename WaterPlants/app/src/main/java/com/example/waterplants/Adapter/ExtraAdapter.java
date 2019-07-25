package com.example.waterplants.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.waterplants.ExtraFlower;
import com.example.waterplants.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ExtraAdapter extends BaseAdapter {

    private Context context;
    private List<ExtraFlower> models;

    public ExtraAdapter(Context context, List<ExtraFlower> models) {
        this.context = context;
        this.models = models;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Object getItem(int i) {
        return models.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null){
            view=View.inflate(context, R.layout.list_items,null);
        }

        CircleImageView images=view.findViewById(R.id.IV_specie);
        TextView specie=view.findViewById(R.id.plant_name);
        TextView interval=view.findViewById(R.id.tvInterval);
        TextView temperatura=view.findViewById(R.id.tvTemp);
        ExtraFlower extraFlower=models.get(i);
        Glide.with(context).load(extraFlower.getImageUrl()).into(images);
        interval.setText(Integer.toString(extraFlower.getInterval()));
        temperatura.setText(extraFlower.getTemperatură());
        specie.setText(extraFlower.specie);



        return view;
    }
}
