package com.rostrade.foodwagon.foodwagon.model;

public class DynamicCategory implements Category {
    private String name;
    private int id;

    public DynamicCategory(String name, int id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
}
