package com.rostrade.foodwagon.foodwagon.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rostrade.foodwagon.foodwagon.R;
import com.rostrade.foodwagon.foodwagon.model.BasicCategory;
import com.rostrade.foodwagon.foodwagon.model.Category;
import com.rostrade.foodwagon.foodwagon.model.DynamicCategory;
import com.rostrade.foodwagon.foodwagon.model.Product;
import com.rostrade.foodwagon.foodwagon.model.ProductManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends ActionBarActivity {

    private View.OnClickListener mDrawerClickListener;
    private ActionBarDrawerToggle mToggle;
    private DrawerLayout mDrawerLayout;
    private ViewGroup mContainerView;
    private SearchView mSearchView;
    private TextView mBasketBadge;

    private MainAdapter mMainAdapter;
    private ProductManager mProductManager;
    private List<Product> mSearchableProducts;
    private List<Category> mCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContainerView = (ViewGroup) findViewById(R.id.menu_container);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new MainFragment())
                    .commit();
        }

        mProductManager = ProductManager.getInstance(this);
        mSearchableProducts = mProductManager.getAllProducts();
        mMainAdapter = new MainAdapter(new ArrayList<Product>(), this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mToggle);


        mDrawerClickListener = new View.OnClickListener() {
            View lastSeleted = null;

            @Override
            public void onClick(View v) {
                Category category = mCategories.get(Integer.parseInt(v.getTag().toString()));

                if (lastSeleted == null) {
                    lastSeleted = v;
                    lastSeleted.setSelected(true);
                } else {
                    lastSeleted.setSelected(false);
                    lastSeleted = v;
                    lastSeleted.setSelected(true);
                }

                if (category == BasicCategory.FAVORITES) {
                    mMainAdapter.setProducts(mProductManager.getProductsInCategory("-1"));
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                } else if (category == BasicCategory.CART) {
                    startActivity(new Intent(MainActivity.this, CartActivity.class));
                } else if (category == BasicCategory.SETTINGS) {
                    startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                } else if (category == BasicCategory.CONTACTS) {
                    startActivity(new Intent(MainActivity.this, ContactsActivity.class));
                } else if (category instanceof DynamicCategory) {
                    mMainAdapter.setProducts(mProductManager
                            .getProductsInCategory(String.valueOf(category.getId())));
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                }
            }
        };

        mCategories = mProductManager.getCategories();
        buildDrawer(mCategories);
    }

    private void buildDrawer(List<Category> categories) {
        for (int position = 0; position < categories.size(); position++) {
            addDrawerItem(position);
        }
    }

    private void addDrawerItem(int position) {
        ViewGroup itemView = (ViewGroup) LayoutInflater.from(this).inflate(
                R.layout.drawer_item, mContainerView, false);

        TextView categoryName = (TextView) itemView.findViewById(R.id.category_text);
        ImageView categoryIcon = (ImageView) itemView.findViewById(R.id.category_icon);

        Category currentCategory = mCategories.get(position);

        // Add divider
        if (position == BasicCategory.values().length) {
            ViewGroup dividerView = (ViewGroup) LayoutInflater.from(this).inflate(
                    R.layout.drawer_divider, mContainerView, false);
            mContainerView.addView(dividerView);
        }

        itemView.setTag(position);

        if (currentCategory instanceof BasicCategory) {
            switch ((BasicCategory) currentCategory) {
                case CART:
                    categoryIcon.setImageResource(R.drawable.ic_action_shopping_basket_grey600);
                    break;
                case FAVORITES:
                    categoryIcon.setImageResource(R.drawable.ic_action_favorite_grey600);
                    break;
                case PROMOS:
                    categoryIcon.setImageResource(R.drawable.ic_action_grade_grey600);
                    break;
                case CONTACTS:
                    categoryIcon.setImageResource(R.drawable.ic_action_assignment_grey600);
                    break;
                case SETTINGS:
                    categoryIcon.setImageResource(R.drawable.ic_action_settings_grey600);
                    break;
            }
        } else {
            categoryIcon.setImageResource(R.drawable.ic_action_brightness_1_grey600);
        }

        itemView.setOnClickListener(mDrawerClickListener);
        categoryName.setText(currentCategory.getName());
        mContainerView.addView(itemView);
    }

    public MainAdapter getMainAdapter() {
        return mMainAdapter;
    }

    @Override
    protected void onResume() {
        showCartBadge();
        super.onResume();
    }

    public void showCartBadge() {
        if (mBasketBadge != null) {
            int productCount = mProductManager.getOrderFromDb().getOrderItemsCount();
            if (productCount > 0) {
                mBasketBadge.setText(String.valueOf(productCount));
                mBasketBadge.setVisibility(View.VISIBLE);
            } else {
                mBasketBadge.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        View cartItem = menu.findItem(R.id.action_cart).getActionView();
        mBasketBadge = (TextView) cartItem.findViewById(R.id.menu_badge_text);

        if (mBasketBadge != null) {
            int productCount = mProductManager.getOrderFromDb().getOrderItemsCount();
            if (productCount > 0) {
                mBasketBadge.setText(String.valueOf(productCount));
                mBasketBadge.setVisibility(View.VISIBLE);
            } else {
                mBasketBadge.setVisibility(View.INVISIBLE);
            }
        }

        cartItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CartActivity.class));
            }
        });

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final MainAdapter adapter = mMainAdapter;
        final AtomicBoolean isExpanded = new AtomicBoolean();

        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setQueryHint(getResources().getString(R.string.search_hint));

        MenuItemCompat.setOnActionExpandListener(searchItem,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        adapter.setProducts(mProductManager.getProductsInCategory(
                                String.valueOf(mProductManager.getCurrentSelectedCategory())));
                        adapter.notifyDataSetChanged();
                        isExpanded.set(false);
                        return true;  // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        isExpanded.set(true);
                        adapter.setProducts(new ArrayList<Product>());
                        adapter.notifyDataSetChanged();
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
                List<Product> products = new ArrayList<>();

                if (newText.length() > 0) {
                    for (Product product : mSearchableProducts) {
                        if (product.getName().toLowerCase().contains(newText)
                                || product.getDescription().toLowerCase().contains(newText)) {
                            products.add(product);
                        }
                    }

                    if (isExpanded.get()) {
                        adapter.setProducts(products);
                        adapter.notifyDataSetChanged();
                    }
                }
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) return true;
        if (item.getItemId() == R.id.action_cart) {
            startActivity(new Intent(this, CartActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mToggle.onConfigurationChanged(newConfig);
    }
}