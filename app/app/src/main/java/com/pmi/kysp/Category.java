package com.pmi.kysp;

public class Category {
    private int id;
    private String name;
    public boolean isChosen;

    public Category(int id, String name, boolean isChosen)
    {
        this.id = id;
        this.name = name;
        this.isChosen = isChosen;
    }

    public Category(int id, String name)
    {
        this(id, name, false);
    }

    public String getName()
    {
        return name;
    }

}
