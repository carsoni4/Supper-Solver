package com.example.suppersolver;

public class MUser {
    private int ID, imageID;
    private String name, username, password, bio;
    private boolean isAdmin, isAdvertiser;

    public MUser(int ID, String name, String username, String password){
        this.ID = ID;
        this.name = name;
        this.username = username;
        this.password = password;
        this.isAdmin = false;
        this.isAdvertiser = false;
    }

    public MUser(int ID, String name, String username, String password, boolean isAdmin, boolean isAdvertiser, String bio, int imageID)
    {
        this.ID = ID;
        this.name = name;
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.isAdvertiser = isAdvertiser;
        this.bio = bio;
        this.imageID = imageID;
    }

}
