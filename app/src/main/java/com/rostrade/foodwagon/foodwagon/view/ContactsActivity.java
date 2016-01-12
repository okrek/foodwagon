package com.rostrade.foodwagon.foodwagon.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rostrade.foodwagon.foodwagon.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ContactsActivity extends AppCompatActivity {

    @Bind(R.id.toolbar) Toolbar toolbar;

    @Bind(R.id.phone_number) TextView phoneNumber;
    @Bind(R.id.address_text) TextView addressText;
    @Bind(R.id.company_url) TextView websiteTextView;
    @Bind(R.id.mail_text) TextView emailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        phoneNumber.setText(R.string.company_phone_number);
        addressText.setText(R.string.company_address);
        websiteTextView.setText(R.string.company_url);
        emailTextView.setText(R.string.company_email);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.contacts_call)
    void dialNumber() {
        String telephoneNumber = phoneNumber.getText().toString();
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + telephoneNumber));
        startActivity(intent);
    }

    @OnClick(R.id.contacts_map)
    void showOnMap() {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("geo:56.310412, 43.997617?z=18"));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @OnClick(R.id.contacts_web)
    void showWebsite() {
        String url = websiteTextView.getText().toString();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @OnClick(R.id.contacts_email)
    void sendEmail() {
        String mailTo = emailTextView.getText().toString();
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + mailTo));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
