package com.example.waterplants;

public class User {
    public String username;
    public String email;
    public int pompa;
    //private ArrayList<Floare>=new ArrayList<Floare>();

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email, int pompa) {
        this.username=username;
        this.email=email;
        this.pompa=pompa;
    }

//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public int getPompa() {
//        return pompa;
//    }
//
//    public void setPompa(int pompa) {
//        this.pompa = pompa;
//    }
}
