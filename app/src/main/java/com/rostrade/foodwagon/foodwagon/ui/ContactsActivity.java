package com.rostrade.foodwagon.foodwagon.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rostrade.foodwagon.foodwagon.R;


public class ContactsActivity extends ActionBarActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout makeCall = (LinearLayout) findViewById(R.id.contacts_call);
        LinearLayout showOnMap = (LinearLayout) findViewById(R.id.contacts_map);
        LinearLayout goToWebsite = (LinearLayout) findViewById(R.id.contacts_web);
        LinearLayout sendEmail = (LinearLayout) findViewById(R.id.contacts_email);

        TextView phoneNumber = (TextView) findViewById(R.id.phone_number);
        TextView addressText = (TextView) findViewById(R.id.address_text);
        TextView webText = (TextView) findViewById(R.id.company_url);
        TextView emailText = (TextView) findViewById(R.id.mail_text);

        phoneNumber.setText(R.string.company_phone_number);
        addressText.setText(R.string.company_address);
        webText.setText(R.string.company_url);
        emailText.setText(R.string.company_email);

        makeCall.setOnClickListener(this);
        showOnMap.setOnClickListener(this);
        goToWebsite.setOnClickListener(this);
        sendEmail.setOnClickListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contacts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.contacts_call:
                String telephoneNumber = ((TextView) v.findViewById(R.id.phone_number))
                        .getText().toString();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + telephoneNumber));
                startActivity(intent);
                break;
            case R.id.contacts_map:
                intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("geo:56.310412, 43.997617?z=18"));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                break;
            case R.id.contacts_web:
                String url = ((TextView) v.findViewById(R.id.company_url)).getText().toString();
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                break;
            case R.id.contacts_email:
                String mailTo = ((TextView) v.findViewById(R.id.mail_text)).getText().toString();
                intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + mailTo));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                break;
        }
    }
}
