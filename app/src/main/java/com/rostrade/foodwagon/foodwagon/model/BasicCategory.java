package com.rostrade.foodwagon.foodwagon.model;

public enum BasicCategory implements Category {
    CART ("Корзина", -1),
    FAVORITES("Избранное", -1),
    CONTACTS("Контакты", -1),
    SETTINGS("Настройки", -1),
    PROMOS("Акции", -1);

    private final String name;
    private final int id;

    BasicCategory(String name, int id) {
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
