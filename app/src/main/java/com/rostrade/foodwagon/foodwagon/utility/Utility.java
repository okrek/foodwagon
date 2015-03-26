package com.rostrade.foodwagon.foodwagon.utility;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.text.SpannableStringBuilder;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.rostrade.foodwagon.foodwagon.ui.RoubleSpan;
import com.rostrade.foodwagon.foodwagon.model.Category;
import com.rostrade.foodwagon.foodwagon.model.DynamicCategory;
import com.rostrade.foodwagon.foodwagon.model.Modification;
import com.rostrade.foodwagon.foodwagon.model.Product;
import com.rostrade.foodwagon.foodwagon.model.ProductManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class Utility {

    private static final String CATEGORIES_URL = "http://nn.foodwagon.ru/catalogs.json";
    private static final String PRODUCTS_URL = "http://nn.foodwagon.ru/products.json";
    private static Utility mInstance;
    private SyncListener mSyncListener;
    private Typeface mRoubleSupportedTypeface;
    private ProductManager mProductManager;
    private RequestQueue mQueue;
    private Context mContext;

    private Utility(Context context) {
        mContext = context;
        mProductManager = ProductManager.getInstance(context);
        mQueue = NetworkHelper.getInstance(mContext).getRequestQueue();
        mRoubleSupportedTypeface =
                Typeface.createFromAsset(mContext.getAssets(), "fonts/rouble2.ttf");
    }

    public static synchronized Utility getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Utility(context.getApplicationContext());
        }
        return mInstance;
    }

    public void fetchDatabase() {
        makeApiRequest(CATEGORIES_URL);
    }

    private void makeApiRequest(final String url, final String... params) {
        JSONObject jsonParams = new JSONObject();
        if (params.length > 1) {
            try {
                jsonParams.put(params[0], params[1]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonParams,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (params.length < 2) {
                    mSyncListener.onTaskStarted(response.length());
                    parseCategories(response);
                } else {
                    VolleyLog.v(params[1], response);
                    new AsyncTask<JSONObject, Void, Void>() {
                        @Override
                        protected Void doInBackground(JSONObject... params) {
                            parseProducts(params[0]);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            mSyncListener.onTaskUpdated();
                        }
                    }.execute(response);
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mSyncListener.onError();
                    }
                });
        mQueue.add(request);
    }

    private List<Modification> parseModifications(JSONObject jsonObject) {
        List<Modification> modifications = new ArrayList<>();

        try {
            JSONObject productModifications = jsonObject.getJSONObject("info").getJSONObject("mod");
            Iterator iterator = productModifications.keys();
            while (iterator.hasNext()) {
                String modId = (String) iterator.next();
                JSONObject currentModification = productModifications.getJSONObject(modId);

                String price = currentModification.getJSONObject("vl").getString("price");
                String weight = currentModification.getJSONObject("vl").getString("mass");
                String modName = currentModification.getJSONArray("mod_description")
                        .getJSONObject(0).getString("name");
                String modValue = currentModification.getJSONArray("mod_description")
                        .getJSONObject(0).getString("value");

                Modification addModification = new Modification();
                addModification.setModId(modId);
                addModification.setModPrice(Integer.parseInt(price));
                addModification.setModWeight(weight);
                addModification.setModName(modName);
                addModification.setModValue(modValue);
                modifications.add(addModification);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return modifications;
    }

    private void parseCategories(JSONObject response) {
        ArrayList<Category> categories = new ArrayList<>();
        JSONArray categoriesList = new JSONArray();

        try {
            Iterator iterator = response.keys();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                categoriesList.put(response.get(key));
            }

            for (int i = 0; i < categoriesList.length(); i++) {
                JSONObject currentItem = categoriesList.getJSONObject(i);
                String name = currentItem.getString("name");
                int id = currentItem.getInt("id");
                Category currentCat = new DynamicCategory(name, id);
                categories.add(currentCat);

                makeApiRequest(PRODUCTS_URL, "ctg_id", String.valueOf(id));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mProductManager.updateCategories(categories);

    }

    private void parseProducts(JSONObject response) {
        List<Product> products = new ArrayList<>();

        try {
            JSONObject jsonProducts = response.getJSONObject("products");
            Iterator iterator = jsonProducts.keys();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                JSONObject currentItem = jsonProducts.getJSONObject(key);
                JSONArray modifications = currentItem.getJSONArray("modifications");

                Product currentProduct = new Product();
                currentProduct.setId(currentItem.getString("id"));
                currentProduct.setCategory(currentItem.getString("catalog_id"));
                currentProduct.setName(currentItem.getString("name"));
                currentProduct.setDescription(currentItem.getString("description").trim());
                currentProduct.setPrice(currentItem.getString("price"));
                currentProduct.setWeight(currentItem.getString("mass"));
                currentProduct.setImageUrl("http://www.foodwagon.ru"
                        + currentItem.getString("thumbs_url"));

                if (modifications.length() > 1) {
                    currentProduct.setModifications(parseModifications(currentItem));
                }

                products.add(currentProduct);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Collections.sort(products);
        mProductManager.updateDB(products);
    }

    public void setSyncListener(SyncListener mSyncListener) {
        this.mSyncListener = mSyncListener;
    }

    public SpannableStringBuilder getRoubleSign(String line) {
        SpannableStringBuilder resultSpan = new SpannableStringBuilder(line + '\u20BD');
        RoubleSpan roubleTypefaceSpan = new RoubleSpan(mRoubleSupportedTypeface);
        resultSpan.setSpan(roubleTypefaceSpan, resultSpan.length() - 1, resultSpan.length(), 0);

        return resultSpan;
    }

    public File getProductImage(Product product) {
        String fileName = product.getImageUrl().substring(product.getImageUrl()
                .lastIndexOf('/') + 1, product.getImageUrl().length());
        String path = mContext.getFilesDir().getPath();

        return new File(path + "/" + fileName);
    }
}
