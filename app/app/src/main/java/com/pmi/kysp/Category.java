package com.pmi.kysp;

public class Category {
    private String name;
    public boolean isChosen;

    public Category(String name, boolean isChosen)
    {
        this.name = name;
        this.isChosen = isChosen;
    }

    public String getName()
    {
        return name;
    }

}
