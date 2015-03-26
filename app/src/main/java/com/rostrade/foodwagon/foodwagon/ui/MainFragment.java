package com.rostrade.foodwagon.foodwagon.ui;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rostrade.foodwagon.foodwagon.R;
import com.rostrade.foodwagon.foodwagon.model.Product;
import com.rostrade.foodwagon.foodwagon.model.ProductManager;
import com.rostrade.foodwagon.foodwagon.utility.BadgeChangeListener;

import java.util.List;


public class MainFragment extends Fragment {

    private static final String DEFAULT_CATEGORY_PREF_KEY = "defaultCategory";
    private static final String DEFAULT_CATEGORY_PREF_DEF_VALUE = "20";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_main, container, false);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String defaultCategory =
                preferences.getString(DEFAULT_CATEGORY_PREF_KEY, DEFAULT_CATEGORY_PREF_DEF_VALUE);

        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view);
        final List<Product> products = ProductManager.getInstance(getActivity())
                .getProductsInCategory(defaultCategory);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        MainAdapter adapter = ((MainActivity) getActivity()).getMainAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setLayoutManager(layoutManager);
        adapter.setBadgeChangeListener(new BadgeChangeListener() {
            @Override
            public void notifyBadge() {
                TextView basketBadge = (TextView) getActivity().findViewById(R.id.menu_badge_text);
                int productCount = ProductManager.getInstance(getActivity()).getOrderFromDb()
                        .getOrderItemsCount();

                if (productCount > 0) {
                    basketBadge.setText(String.valueOf(productCount));
                    basketBadge.setVisibility(View.VISIBLE);
                } else {
                    basketBadge.setVisibility(View.INVISIBLE);
                }

            }
        });

        adapter.setProducts(products);
        return root;
    }
}

