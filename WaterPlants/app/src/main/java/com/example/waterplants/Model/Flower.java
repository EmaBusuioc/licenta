package com.example.waterplants.Model;


import android.provider.ContactsContract;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class Flower {

    public String specie;
    public String floare;
    public Date next_watering;

    public Date getLast_watering() {
        return last_watering;
    }

    public void setLast_watering(Date last_watering) {
        this.last_watering = last_watering;
    }

    public Date last_watering;
    public int interval;
    public long id;
    public String imageUrl="default";

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSpecie() {
        return specie;
    }

    public void setSpecie(String specie) {
        this.specie = specie;
    }

    public String getFloare() {
        return floare;
    }

    public void setFloare(String floare) {
        this.floare = floare;
    }

    public Date getNext_watering() {
        return next_watering;
    }

    public void setNext_watering(Date next_watering) {
        this.next_watering = next_watering;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Flower(){

    }
    public Flower (String specie, String floare, Date next_watering, int interval, long id,String imageUrl){
        this.specie=specie;
        this.floare=floare;
        this.next_watering=next_watering;
        this.interval=interval;
        this.id=id;
        this.imageUrl=imageUrl;
    }
}
