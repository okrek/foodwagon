package com.rostrade.foodwagon.foodwagon.network.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.rostrade.foodwagon.foodwagon.model.Product;

import java.lang.reflect.Type;

/**
 * Created by frankie on 07.01.2016.
 */
public class ProductDeserializer implements JsonDeserializer<Product> {
    @Override
    public Product deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonProduct = json.getAsJsonObject();

        Product product = new Product();
        product.setId(jsonProduct.get("id").getAsString());
        product.setCategory(jsonProduct.get("catalog_id").getAsString());
        product.setName(jsonProduct.get("name").getAsString());
        product.setDescription(jsonProduct.get("description").getAsString().trim());
        product.setPrice(jsonProduct.get("price").getAsString());
        product.setWeight(jsonProduct.get("mass").getAsString());
        product.setImageUrl("http://www.foodwagon.ru"
                + jsonProduct.get("thumbs_url").getAsString());
        product.setFromJson(true);

        return product;
    }
}
