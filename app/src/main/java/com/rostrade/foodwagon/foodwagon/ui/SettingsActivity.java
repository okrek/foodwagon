package com.rostrade.foodwagon.foodwagon.ui;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.rostrade.foodwagon.foodwagon.R;
import com.rostrade.foodwagon.foodwagon.model.BasicCategory;
import com.rostrade.foodwagon.foodwagon.model.Category;
import com.rostrade.foodwagon.foodwagon.model.ProductManager;

import java.util.List;

public class SettingsActivity extends ActionBarActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFragmentManager().beginTransaction()
                .replace(R.id.settings_container, new SettingsFragment()).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);

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

    public static class SettingsFragment extends PreferenceFragment
            implements Preference.OnPreferenceChangeListener {

        private ProductManager mProductManager;
        private Preference mClearFavsPreference;
        private ListPreference mListPreference;
        private CharSequence[] mEntryValues;
        private CharSequence[] mEntries;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            mProductManager = ProductManager.getInstance(getActivity());

            mClearFavsPreference = findPreference("clearFavorites");
            mClearFavsPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    mProductManager.clearFavorites();
                    return false;
                }
            });

            mListPreference = (ListPreference) findPreference("defaultCategory");
            List<Category> categories = mProductManager.getDynamicCategories();
            categories.add(0, BasicCategory.FAVORITES);

            mEntries = new CharSequence[categories.size()];
            mEntryValues = new CharSequence[categories.size()];
            for (int i = 0; i < categories.size() ; i++) {
                mEntries[i] = categories.get(i).getName();
                mEntryValues[i] = String.valueOf(categories.get(i).getId());
            }

            mListPreference.setEntries(mEntries);
            mListPreference.setEntryValues(mEntryValues);
            mListPreference.setOnPreferenceChangeListener(this);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {

            switch (preference.getKey()) {
                case "defaultCategories":
                    int position = mListPreference.findIndexOfValue((String) newValue);
                    mListPreference.setSummary(mEntries[position]);
                    break;
            }

            return true;
        }
    }
}
