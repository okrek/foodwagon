package com.rostrade.foodwagon.foodwagon.database;

import com.raizlabs.android.dbflow.runtime.TransactionManager;
import com.raizlabs.android.dbflow.runtime.transaction.process.ProcessModelInfo;
import com.raizlabs.android.dbflow.runtime.transaction.process.SaveModelTransaction;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Where;
import com.rostrade.foodwagon.foodwagon.model.Category;
import com.rostrade.foodwagon.foodwagon.model.ProductCategory;
import com.rostrade.foodwagon.foodwagon.model.OrderItem;
import com.rostrade.foodwagon.foodwagon.model.OrderItem_Table;
import com.rostrade.foodwagon.foodwagon.model.Product;
import com.rostrade.foodwagon.foodwagon.model.Product_Table;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by frankie on 25.12.2015.
 */
public class DBFlowManager {

    public static void saveCategories(List<ProductCategory> categories) {
        ProcessModelInfo<ProductCategory> processModelInfo = ProcessModelInfo
                .withModels(categories);
        TransactionManager.getInstance()
                .addTransaction(new SaveModelTransaction<ProductCategory>(processModelInfo));
    }

    public static void saveProducts(List<Product> products) {
        ProcessModelInfo<Product> processModelInfo = ProcessModelInfo
                .withModels(products);
        TransactionManager.getInstance()
                .addTransaction(new SaveModelTransaction<Product>(processModelInfo));
    }

    public static List<Product> getAllProducts() {
        return SQLite.select().from(Product.class).queryList();
    }

    public static void toggleFavorite(Product product) {
        product.toggleFavorite();
        product.save();
    }

    public static List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();
        categories.addAll(SQLite.select().from(ProductCategory.class).queryList());

        return categories;
    }

    public static void clearFavorites() {
        Where<Product> update = SQLite.update(Product.class)
                .set(Product_Table.mIsFavorite.is(false))
                .where(Product_Table.mIsFavorite.is(true));
        update.queryClose();
    }

    public static void addToOrder(Product product) {
        OrderItem orderItem = SQLite.select()
                .from(OrderItem.class)
                .where(OrderItem_Table.productId.eq(product.getId()))
                .querySingle();

        if (orderItem != null) {
            orderItem.setQuantity(orderItem.getQuantity() + 1);
        } else {
            orderItem = new OrderItem();
            orderItem.setProductId(product.getId());
            orderItem.setQuantity(1);
        }

        orderItem.save();
    }

    public static List<OrderItem> getOrderItems() {
        return SQLite.select().from(OrderItem.class).queryList();
    }
}
