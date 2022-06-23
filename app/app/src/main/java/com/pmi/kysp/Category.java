package com.pmi.kysp;

public class Category {
    private String name;
    private boolean isChosen;

    public Category(String name, boolean isChosen)
    {
        this.name = name;
        this.isChosen = isChosen;
    }

    public String getName()
    {
        return name;
    }

    public boolean isChosen()
    {
        return isChosen;
    }

}
