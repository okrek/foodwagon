package com.rostrade.foodwagon.foodwagon.model;

public class Modification {

    private String modId;
    private String modWeight;
    private String modName;
    private String modValue;
    private int modPrice;

    public void setModId(String modId) {
        this.modId = modId;
    }

    public void setModWeight(String modWeight) {
        this.modWeight = modWeight;
    }

    public void setModName(String modName) {
        this.modName = modName;
    }

    public void setModValue(String modValue) {
        this.modValue = modValue;
    }

    public void setModPrice(int modPrice) {
        this.modPrice = modPrice;
    }

    public String getModId() {
        return modId;
    }

    public String getModWeight() {
        return modWeight;
    }

    public String getModName() {
        return modName;
    }

    public String getModValue() {
        return modValue;
    }

    public int getModPrice() {
        return modPrice;
    }
}
