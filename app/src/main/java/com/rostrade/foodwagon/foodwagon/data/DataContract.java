package com.rostrade.foodwagon.foodwagon.data;

import android.provider.BaseColumns;

public class DataContract {
    public static class ProductEntry implements BaseColumns {
        public static final String TABLE_PRODUCTS = "products";

        // PRODUCTS Table - column names
        public static final String KEY_ID = "_id";
        public static final String KEY_NAME= "name";
        public static final String KEY_PRICE = "price";
        public static final String KEY_WEIGHT = "weight";
        public static final String KEY_DESCRIPTION = "description";
        public static final String KEY_CATEGORY_ID = "category_id";
        public static final String KEY_IMAGE_URL = "image_url";
        public static final String KEY_MODIFICATIONS = "modifications";
        public static final String KEY_FAVORITE = "in_favorites_list";

    }

    public static class OrderEntry implements BaseColumns {
        public static final String TABLE_CART = "cart";

        // CART Table - column names
        public static final String ID = "_id";
        public static final String MODIFICATION_ID = "mod_id";
        public static final String QUANTITY = "quantity";

    }

    public static class CategoryEntry implements BaseColumns {
        public static final String TABLE_CATEGORIES = "categories";

        // CATEGORY Table - column names
        public static final String CATEGORY_NAME = "name";
        public static final String CATEGORY_ID = "category_id";
    }
}
