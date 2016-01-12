package com.rostrade.foodwagon.foodwagon.view.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.rostrade.foodwagon.foodwagon.ProductListItemClickListener;
import com.rostrade.foodwagon.foodwagon.R;
import com.rostrade.foodwagon.foodwagon.model.Product;
import com.rostrade.foodwagon.foodwagon.presenter.IProductListPresenter;
import com.rostrade.foodwagon.foodwagon.presenter.impl.ProductListPresenter;
import com.rostrade.foodwagon.foodwagon.view.IProductListView;
import com.rostrade.foodwagon.foodwagon.view.adapters.ProductListAdapter;

import java.util.ArrayList;
import java.util.List;


public class MainFragment extends Fragment implements IProductListView, ProductListItemClickListener<Product> {

    private ProductListAdapter adapter;
    private IProductListPresenter presenter;

    private SearchView mSearchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(null);

        adapter = new ProductListAdapter(new ArrayList<Product>(), getActivity());
        recyclerView.setAdapter(adapter);

        adapter.setProductListItemClickListener(this);

        presenter = new ProductListPresenter(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_product_list, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setQueryHint(getResources().getString(R.string.search_hint));

        MenuItemCompat.setOnActionExpandListener(searchItem,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        presenter.onSearchCollapsed();
                        return true;  // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        presenter.onSearchTextChanged("");
                        return true;  // Return true to expand action view
                    }
                });

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                presenter.onSearchTextChanged(newText);
                return false;
            }
        });
    }

    @Override
    public void onItemClicked(View v, Product item) {
        presenter.onListItemClicked(item);
    }

    @Override
    public void buyButtonClicked(View v, Product item) {
        presenter.onBuyButtonClicked(item);
    }

    @Override
    public void showProducts(List<Product> products) {
        adapter.setProducts(products);
    }

    @Override
    public void showProductDetails(Product product) {

    }

    @Override
    public Context getContext() {
        return getActivity().getApplicationContext();
    }
}

