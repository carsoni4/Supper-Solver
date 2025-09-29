package com.example.suppersolver;

public class MRecipe {
    public int ID, userID;
    private MUser user;
    public String name, description;

    public MRecipe(int ID, String name, String description, int userID)
    {
        this.ID = ID;
        this.userID = userID;
        this.description = description;
        this.name = name;
    }

}
