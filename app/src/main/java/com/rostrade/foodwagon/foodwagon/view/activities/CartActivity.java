package com.rostrade.foodwagon.foodwagon.view.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.rostrade.foodwagon.foodwagon.R;
import com.rostrade.foodwagon.foodwagon.view.fragments.CartFragment;
import com.rostrade.foodwagon.foodwagon.view.fragments.OrderFormFragment;


public class CartActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFragmentManager().beginTransaction().replace(R.id.cart_container, new CartFragment())
                .commit();

        Button buttonProcess = (Button) findViewById(R.id.button_process);
        Button buttonBack = (Button) findViewById(R.id.button_back);
        buttonProcess.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0){
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_process:
                Fragment currentFragment = getFragmentManager().findFragmentById(R.id.cart_container);
                if (currentFragment instanceof CartFragment) {
                    getFragmentManager().beginTransaction()
                            .setCustomAnimations(R.animator.slide_in_right,
                                    R.animator.slide_out_left,
                                    R.animator.slide_in_left,
                                    R.animator.slide_out_right)
                            .replace(R.id.cart_container, OrderFormFragment.newInstance())
                            .addToBackStack(null)
                            .commit();
                } else if (currentFragment instanceof OrderFormFragment) {
//                    String orderData = mProductManager.getOrderFromDb().buildJsonOrder();
                    String customerData = ((OrderFormFragment) currentFragment)
                            .buildUserDataRequest();
                }
                break;
            case R.id.button_back:
                getFragmentManager().popBackStack();
                break;
        }
    }
}
