package com.example.waterplants;


import com.example.waterplants.Model.Flower;

import java.lang.reflect.Array;
import java.util.List;

public class User {
    private String username;
    private String email;
    private int pompa;
    private String ImageURL;
    private List<Flower> qFlowers=null;
    private int nrFlori=0;

    public  int getNrFlori() {
        return nrFlori;
    }

    public void setNrFlori(int nrFlori) {
        this.nrFlori = nrFlori;
    }



    public User(){}

    public User(String username, String email, int pompa,String img,int nrFlori) {
        this.username=username;
        this.email=email;
        this.pompa=pompa;
        ImageURL=img;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPompa() {
        return pompa;
    }

    public void setPompa(int pompa) {
        this.pompa = pompa;
    }
    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }


}
