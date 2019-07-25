package com.example.waterplants;



public class ExtraFlower {


    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String site;
    public String specie;

    public String getSpecie() {
        return specie;
    }

    public void setSpecie(String specie) {
        this.specie = specie;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTemperatură() {
        return temperatură;
    }

    public void setTemperatură(String temperatură) {
        this.temperatură = temperatură;
    }

    public int interval;
    public String imageUrl="default";
    public String temperatură;

    public ExtraFlower(){}
    public ExtraFlower(String specie, int interval, String imageUrl, String temperatură,String site) {
        this.specie = specie;
        this.interval = interval;
        this.imageUrl = imageUrl;
        this.temperatură = temperatură;
        this.site=site;
    }






}
