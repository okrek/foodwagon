package com.rostrade.foodwagon.foodwagon.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rostrade.foodwagon.foodwagon.data.DataContract.*;
import com.rostrade.foodwagon.foodwagon.data.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ProductManager {

    private static ProductManager sInstance;
    private SQLiteDatabase mDatabase;
    private AtomicInteger mCurrentSelectedCategory;
    private List<Product> mAllProducts;

    private ProductManager(Context context) {
        mDatabase = DatabaseHelper.getInstance(context).getWritableDatabase();
        mCurrentSelectedCategory = new AtomicInteger();
    }

    public static synchronized ProductManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ProductManager(context);
        }
        return sInstance;
    }

    public Product getProductById(String id) {
        Cursor cursor = mDatabase.query(ProductEntry.TABLE_PRODUCTS, null,
                ProductEntry.KEY_ID + "=" + id, null, null, null, null);

        List<Product> product = getProductsFromCursor(cursor);
        if (product.size() > 0) return product.get(0);

        return null;
    }

    public List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();
        Collections.addAll(categories, BasicCategory.values());
        categories.addAll(getDynamicCategories());

        return categories;
    }

    public List<Category> getDynamicCategories() {
        Cursor cursor = mDatabase.query(CategoryEntry.TABLE_CATEGORIES, null
                , null, null, null, null, null);
        cursor.moveToFirst();

        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < cursor.getCount(); i++) {
            int nameColumn = cursor.getColumnIndex(CategoryEntry.CATEGORY_NAME);
            int idColumn = cursor.getColumnIndex(CategoryEntry.CATEGORY_ID);

            Category currentCategory = new DynamicCategory(cursor.getString(nameColumn),
                    cursor.getInt(idColumn));
            categories.add(currentCategory);
            cursor.moveToNext();
        }

        cursor.close();
        return categories;
    }

    public List<Product> getAllProducts() {
        if (mAllProducts == null) {
            mAllProducts = new ArrayList<>();
            Cursor cursor = mDatabase.query(ProductEntry.TABLE_PRODUCTS, null, null, null,
                    null, null, null);
            mAllProducts = getProductsFromCursor(cursor);
        }

        return mAllProducts;
    }

    /**
     * @return list of products for given {@param category_id}
     */
    public List<Product> getProductsInCategory(String category_id) {
        Cursor cursor = mDatabase.query(ProductEntry.TABLE_PRODUCTS, null,
                ProductEntry.KEY_CATEGORY_ID + "=" + category_id, null, null, null, null);

        List<Product> products = getProductsFromCursor(cursor);
        mCurrentSelectedCategory.set(Integer.parseInt(category_id));

        return products;
    }

    public List<Product> getFavorites() {
        List<Product> favoritesList = new ArrayList<>();
        Cursor cursor = mDatabase.query(FavoriteEntry.TABLE_FAVORITES, null,
                null, null, null, null, null);
        cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount(); i++) {
            String productId = cursor.getString(cursor.getColumnIndex(FavoriteEntry.ID));
            Product currentProduct = getProductById(productId);
            if (currentProduct != null) {
                favoritesList.add(currentProduct);
            } else {
                mDatabase.delete(FavoriteEntry.TABLE_FAVORITES,
                        FavoriteEntry.ID + "=" + productId, null);
            }

            cursor.moveToNext();
        }

        return favoritesList;
    }

    public void clearFavorites() {
        mDatabase.execSQL("DELETE FROM " + FavoriteEntry.TABLE_FAVORITES);
    }

    public Order getOrderFromDb() {
        Cursor cursor = mDatabase.query(OrderEntry.TABLE_CART, null,
                null, null, null, null, null);
        cursor.moveToFirst();

        Order order = new Order();

        for (int i = 0; i < cursor.getCount(); i++) {
            String productId = cursor.getString(cursor.getColumnIndex(OrderEntry.ID));
            int quantity = cursor.getInt(cursor.getColumnIndex(OrderEntry.QUANTITY));
            String modId = cursor.getString(cursor.getColumnIndex(OrderEntry.MODIFICATION_ID));

            Product currentProduct = getProductById(productId);

            if (currentProduct != null) {
                if (modId != null) {
                    Modification selectedModification = currentProduct.getModificationById(modId);
                    currentProduct.setPrice(selectedModification.getModPrice() + "");
                    currentProduct.setWeight(selectedModification.getModWeight());
                    currentProduct.setSelectedModification(selectedModification);
                }

                order.addItem(currentProduct, quantity);
            } else {
                String sql = "DELETE FROM " + OrderEntry.TABLE_CART + " WHERE "
                        + OrderEntry.ID + "=" + productId;
                mDatabase.execSQL(sql);
            }
            cursor.moveToNext();
        }

        cursor.close();
        return order;
    }

    public int getCurrentSelectedCategory() {
        return mCurrentSelectedCategory.get();
    }


    public void updateDB(List<Product> products) {
        // TODO: clear products table on update,
        for (Product product : products) {
            ContentValues values = new ContentValues();
            values.put(ProductEntry.KEY_ID, product.getId());
            values.put(ProductEntry.KEY_NAME, product.getName());
            values.put(ProductEntry.KEY_PRICE, product.getPrice());
            values.put(ProductEntry.KEY_WEIGHT, product.getWeight());
            values.put(ProductEntry.KEY_DESCRIPTION, product.getDescription());
            values.put(ProductEntry.KEY_CATEGORY_ID, product.getCategory());
            values.put(ProductEntry.KEY_IMAGE_URL, product.getImageUrl());

            List<Modification> modifications = product.getModifications();
            if (!modifications.isEmpty()) {
                JSONArray modificationsArray = new JSONArray();
                for (Modification modification : modifications) {
                    JSONObject currentMod = new JSONObject();
                    try {
                        currentMod.put("id", modification.getModId());
                        currentMod.put("name", modification.getModName());
                        currentMod.put("value", modification.getModValue());
                        currentMod.put("price", modification.getModPrice());
                        currentMod.put("weight", modification.getModWeight());
                        modificationsArray.put(currentMod);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                values.put(ProductEntry.KEY_MODIFICATIONS, modificationsArray.toString());
            }
            mDatabase.insertWithOnConflict(ProductEntry.TABLE_PRODUCTS, null,
                    values, SQLiteDatabase.CONFLICT_REPLACE);
        }

    }

    public void updateCategories(List<Category> categories) {
        for (Category category : categories) {
            ContentValues values = new ContentValues();
            values.put(CategoryEntry.CATEGORY_ID, category.getId());
            values.put(CategoryEntry.CATEGORY_NAME, category.getName());
            mDatabase.replace(CategoryEntry.TABLE_CATEGORIES, null, values);
        }
    }

    public void updateFavorites(Product product) {
        ContentValues values = new ContentValues();
        values.put(FavoriteEntry.ID, product.getId());
        if (product.isFavorite()) {
            mDatabase.insertWithOnConflict(FavoriteEntry.TABLE_FAVORITES, null,
                    values, SQLiteDatabase.CONFLICT_IGNORE);
        } else {
            mDatabase.delete(FavoriteEntry.TABLE_FAVORITES,
                    FavoriteEntry.ID + "=" + product.getId(), null);
        }

    }

    public void setOrderItemQuantity(Order order, Product product, String quantity) {
        if (product.getSelectedModificationId() != null) {
            mDatabase.execSQL("UPDATE " + OrderEntry.TABLE_CART
                    + " SET " + OrderEntry.QUANTITY + " = " + quantity
                    + " WHERE _id = " + product.getId()
                    + " AND mod_id = " + product.getSelectedModificationId());
        } else {
            mDatabase.execSQL("UPDATE cart SET quantity = " + quantity
                    + " WHERE _id = " + product.getId());
        }

        order.setItemQuantity(product, quantity);
    }

    public void addToCart(Product product) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(OrderEntry.ID, product.getId());

        if (product.getSelectedModificationId() != null) {
            String[] values = new String[]{
                    String.valueOf(product.getId()), product.getSelectedModificationId()};

            Cursor cursor = mDatabase.query(OrderEntry.TABLE_CART, null,
                    OrderEntry.ID + "=? AND " + OrderEntry.MODIFICATION_ID + "=?",
                    values, null, null, null);

            if (cursor.getCount() != 0) {
                mDatabase.execSQL("UPDATE " + OrderEntry.TABLE_CART
                        + " SET " + OrderEntry.QUANTITY + " = " + OrderEntry.QUANTITY
                        + " + 1 WHERE _id = " + product.getId()
                        + " AND " + OrderEntry.MODIFICATION_ID
                        + "= " + product.getSelectedModificationId());
            } else {
                contentValues.put(OrderEntry.MODIFICATION_ID, product.getSelectedModificationId());
                mDatabase.insertWithOnConflict(OrderEntry.TABLE_CART, null,
                        contentValues, SQLiteDatabase.CONFLICT_IGNORE);
                mDatabase.execSQL("UPDATE " + OrderEntry.TABLE_CART
                        + " SET " + OrderEntry.QUANTITY + " = " + OrderEntry.QUANTITY
                        + " + 1 WHERE _id = " + product.getId()
                        + " AND " + OrderEntry.MODIFICATION_ID
                        + "= " + product.getSelectedModificationId());
            }

            cursor.close();
        } else {
            Cursor cursor = mDatabase.query(OrderEntry.TABLE_CART, null,
                    OrderEntry.ID + " = " + product.getId(), null, null, null, null);

            if (cursor.getCount() != 0) {
                mDatabase.execSQL("UPDATE " + OrderEntry.TABLE_CART
                        + " SET " + OrderEntry.QUANTITY + " = " + OrderEntry.QUANTITY
                        + " + 1 WHERE _id = " + product.getId());
            } else {
                mDatabase.insertWithOnConflict(OrderEntry.TABLE_CART, null,
                        contentValues, SQLiteDatabase.CONFLICT_IGNORE);
                mDatabase.execSQL("UPDATE " + OrderEntry.TABLE_CART
                        + " SET " + OrderEntry.QUANTITY + " = " + OrderEntry.QUANTITY
                        + " + 1 WHERE _id = " + product.getId());
            }

            cursor.close();
        }
    }


    public void removeFromCart(Product product) {
        String sql;
        if (product.getSelectedModificationId() != null) {
            sql = "DELETE FROM " + OrderEntry.TABLE_CART + " WHERE "
                    + OrderEntry.MODIFICATION_ID + "=" + product.getSelectedModificationId();
        } else {
            sql = "DELETE FROM " + OrderEntry.TABLE_CART + " WHERE "
                    + OrderEntry.ID + "=" + product.getId();
        }
        mDatabase.execSQL(sql);
    }

    private List<Product> getProductsFromCursor(Cursor cursor) {
        cursor.moveToFirst();
        List<Product> products = new ArrayList<>();

        if (cursor.getCount() > 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                Product product = new Product();
                product.setId(cursor.getString(
                        cursor.getColumnIndex(ProductEntry.KEY_ID)));
                product.setCategory(cursor.getString(
                        cursor.getColumnIndex(ProductEntry.KEY_CATEGORY_ID)));
                product.setFavorite(isFavorite(product));
                product.setImageUrl(cursor.getString(
                        cursor.getColumnIndex(ProductEntry.KEY_IMAGE_URL)));
                product.setName(cursor.getString(
                        cursor.getColumnIndex(ProductEntry.KEY_NAME)));
                product.setPrice(cursor.getString(
                        cursor.getColumnIndex(ProductEntry.KEY_PRICE)));
                product.setDescription(cursor.getString(
                        cursor.getColumnIndex(ProductEntry.KEY_DESCRIPTION)));
                product.setWeight(cursor.getString(
                        cursor.getColumnIndex(ProductEntry.KEY_WEIGHT)));

                String modifications = cursor.getString(
                        cursor.getColumnIndex(ProductEntry.KEY_MODIFICATIONS));

                if (modifications != null) {
                    List<Modification> modificationList = new ArrayList<>();
                    try {
                        JSONArray modArray = new JSONArray(modifications);
                        for (int j = 0; j < modArray.length(); j++) {
                            JSONObject mod = modArray.getJSONObject(j);
                            Modification currentModification = new Modification();
                            currentModification.setModValue(mod.getString("value"));
                            currentModification.setModId(mod.getString("id"));
                            currentModification.setModWeight(mod.getString("weight"));
                            currentModification.setModName(mod.getString("name"));
                            currentModification.setModPrice(mod.getInt("price"));
                            modificationList.add(currentModification);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    product.setModifications(modificationList);
                }

                products.add(product);
                cursor.moveToNext();
            }
        }
        cursor.close();

        return products;
    }

    private boolean isFavorite(Product product) {
        Cursor cursor = mDatabase.query(FavoriteEntry.TABLE_FAVORITES, null,
                FavoriteEntry.ID + "=" + product.getId(), null, null, null, null);

        if (cursor.getCount() != 0) return true;
        return false;
    }

    public void clearCart() {
        mDatabase.execSQL("DELETE FROM " + OrderEntry.TABLE_CART);
    }
}