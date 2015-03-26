package com.rostrade.foodwagon.foodwagon.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "foodWagonData.db";
    private static final int DATABASE_VERSION = 1;

    private static DatabaseHelper sInstance = null;

    public static DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }

        return sInstance;
    }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String PRODUCTS_SQL_QUERY = "CREATE TABLE " + DataContract.ProductEntry.TABLE_PRODUCTS
                + " (" + DataContract.ProductEntry.KEY_ID + " INTEGER PRIMARY KEY, "
                + DataContract.ProductEntry.KEY_NAME + " TEXT NOT NULL, "
                + DataContract.ProductEntry.KEY_PRICE + " INTEGER NOT NULL,"
                + DataContract.ProductEntry.KEY_WEIGHT + " INTEGER NOT NULL,"
                + DataContract.ProductEntry.KEY_DESCRIPTION + " TEXT NOT NULL,"
                + DataContract.ProductEntry.KEY_CATEGORY_ID + " INTEGER NOT NULL, "
                + DataContract.ProductEntry.KEY_IMAGE_URL + " TEXT NOT NULL, "
                + DataContract.ProductEntry.KEY_MODIFICATIONS + " TEXT, "
                + DataContract.ProductEntry.KEY_FAVORITE + " NUMERIC NOT NULL DEFAULT 0)";

        final String ORDER_SQL_QUERY = "CREATE TABLE " + DataContract.OrderEntry.TABLE_CART + " ("
                + DataContract.OrderEntry.ID + " INTEGER, "
                + DataContract.OrderEntry.MODIFICATION_ID + " INTEGER, "
                + DataContract.OrderEntry.QUANTITY + " INTEGER DEFAULT 0)";

        final String CATEGORY_SQL_QUERY = "CREATE TABLE "
                + DataContract.CategoryEntry.TABLE_CATEGORIES + " ("
                + DataContract.CategoryEntry.CATEGORY_ID + " INTEGER PRIMARY KEY, "
                + DataContract.CategoryEntry.CATEGORY_NAME + " TEXT NOT NULL)";

        db.execSQL(PRODUCTS_SQL_QUERY);
        db.execSQL(CATEGORY_SQL_QUERY);
        db.execSQL(ORDER_SQL_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DataContract.ProductEntry.TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + DataContract.OrderEntry.TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + DataContract.CategoryEntry.TABLE_CATEGORIES);
        onCreate(db);
    }
}
