package com.rostrade.foodwagon.foodwagon.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.rostrade.foodwagon.foodwagon.R;
import com.rostrade.foodwagon.foodwagon.model.NavigationCategory;
import com.rostrade.foodwagon.foodwagon.model.Category;
import com.rostrade.foodwagon.foodwagon.presenter.IDrawerPresenter;
import com.rostrade.foodwagon.foodwagon.presenter.impl.DrawerPresenter;
import com.rostrade.foodwagon.foodwagon.view.IDrawerActivityView;
import com.rostrade.foodwagon.foodwagon.view.fragments.MainFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

// TODO: restyle toolbar, add onDismiss in splashScreenActivity
public class DrawerActivity extends BaseActivity implements IDrawerActivityView {

    private IDrawerPresenter presenter;

    @Bind(R.id.toolbar) Toolbar toolbar;
    private TextView mBasketBadge;
    private Drawer mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new MainFragment())
                    .commit();
        }

        setSupportActionBar(toolbar);
        presenter = new DrawerPresenter(this);
        presenter.onDrawerRequested();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        View cartItem = menu.findItem(R.id.action_cart).getActionView();
        mBasketBadge = (TextView) cartItem.findViewById(R.id.menu_badge_text);

        cartItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onCartClicked();
            }
        });

        presenter.onMenuInflated();

        return true;
    }

    @Override
    public void buildDrawer(List<Category> categories) {
        mDrawer = new DrawerBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(false)
                .withActionBarDrawerToggle(true)
                .withToolbar(toolbar)
                .withDrawerItems(getDrawerItems(categories))
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        presenter.onDrawerItemClicked((Category) drawerItem.getTag());

                        return false;
                    }
                })
                .build();
    }

    @Override
    public void selectDrawerItemForCategory(Category category) {
        mDrawer.setSelection(mDrawer.getDrawerItem(category));
    }

    @Override
    public void showCartBadge(int itemsCount) {
        if (mBasketBadge != null) {

            if (itemsCount > 0) {
                mBasketBadge.setText(String.valueOf(itemsCount));
                mBasketBadge.setVisibility(View.VISIBLE);
            } else {
                mBasketBadge.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void showCart() {
        startActivity(new Intent(this, CartActivity.class));
    }

    @Override
    public void showContacts() {
        startActivity(new Intent(this, ContactsActivity.class));
    }

    @Override
    public void showSettings() {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    private ArrayList<IDrawerItem> getDrawerItems(List<Category> categories) {
        ArrayList<IDrawerItem> drawerItems = new ArrayList<>();

        for (NavigationCategory navigationCategory : NavigationCategory.values()) {
            drawerItems.add(new PrimaryDrawerItem()
                    .withIcon(navigationCategory.getId())
                    .withName(navigationCategory.getName())
                    .withTag(navigationCategory));
        }

        drawerItems.add(new DividerDrawerItem());

        for (Category category : categories) {
            drawerItems.add(new PrimaryDrawerItem()
                    .withIcon(R.drawable.ic_action_brightness_1_grey600)
                    .withName(category.getName())
                    .withTag(category));
        }

        return drawerItems;
    }

}