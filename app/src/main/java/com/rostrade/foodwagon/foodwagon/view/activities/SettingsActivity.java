package com.rostrade.foodwagon.foodwagon.view.activities;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.rostrade.foodwagon.foodwagon.Constants;
import com.rostrade.foodwagon.foodwagon.R;
import com.rostrade.foodwagon.foodwagon.database.DBFlowManager;
import com.rostrade.foodwagon.foodwagon.model.Category;
import com.rostrade.foodwagon.foodwagon.model.NavigationCategory;

import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private static final String CLEAR_FAVORITES = "clearFavorites";

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

        private Preference mClearFavsPreference;
        private ListPreference mListPreference;
        private CharSequence[] mEntryValues;
        private CharSequence[] mEntries;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);

            mClearFavsPreference = findPreference(CLEAR_FAVORITES);
            mClearFavsPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    DBFlowManager.clearFavorites();
                    return false;
                }
            });

            mListPreference = (ListPreference) findPreference(Constants.PREFS_KEY_DEFAULT_CATEGORY);
            List<Category> categories = DBFlowManager.getCategories();
            categories.add(0, NavigationCategory.FAVORITES);

            mEntries = new CharSequence[categories.size()];
            mEntryValues = new CharSequence[categories.size()];
            for (int i = 0; i < categories.size(); i++) {
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
                case Constants.PREFS_KEY_DEFAULT_CATEGORY:
                    int position = mListPreference.findIndexOfValue((String) newValue);
                    mListPreference.setSummary(mEntries[position]);
                    break;
            }

            return true;
        }
    }
}
