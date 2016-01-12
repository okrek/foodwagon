package com.rostrade.foodwagon.foodwagon.model;


import com.rostrade.foodwagon.foodwagon.R;

public enum NavigationCategory implements Category {

    // TODO: change hardcoded text to string ref
    CART ("Корзина", R.drawable.ic_action_shopping_basket_grey600),
    FAVORITES("Избранное", R.drawable.ic_action_favorite_grey600),
    CONTACTS("Контакты", R.drawable.ic_action_assignment_grey600),
    SETTINGS("Настройки", R.drawable.ic_action_settings_grey600),
    PROMOS("Акции", R.drawable.ic_action_grade_grey600);

    private final String name;
    private final int id;

    NavigationCategory(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
}
