package fr.uge.confroid.front;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import java.util.concurrent.TimeUnit;

import fr.uge.confroid.R;
import fr.uge.confroid.front.fragments.RootSettingsFragment;
import fr.uge.confroid.settings.AppSettings;
import fr.uge.confroid.settings.Language;

public class SettingsActivity extends AppCompatActivity implements
        PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new RootSettingsFragment())
                    .commit();
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.root_fragment_title);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Listen preferences on change
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this::preferenceOnChange);
    }

    private void preferenceOnChange(SharedPreferences preferences, String key) {
        switch (key) {
            case "language":
                String selected = preferences.getString(key, "").toUpperCase();
                AppSettings.getINSTANCE().setLanguage(getApplicationContext(), Language.valueOf(selected));
                break;
            case "base_address":
                AppSettings.getINSTANCE().setBaseAddress(preferences.getString(key, ""));
                break;
            case "login":
                AppSettings.getINSTANCE().setLogin(preferences.getString(key, ""));
                break;
            case "password":
                AppSettings.getINSTANCE().setPassword(preferences.getString(key, ""));
                break;
            case "enable_data_sync":
                AppSettings.getINSTANCE().setEnableDataSync(preferences.getBoolean(key, true));
                break;
            case "upload_interval":
                AppSettings.getINSTANCE().setRepeatIntervalTimeUnit(TimeUnit.DAYS);
                switch (preferences.getString(key, "")) {
                    case "day":
                        AppSettings.getINSTANCE().setRepeatInterval(1);
                        break;
                    case "week":
                        AppSettings.getINSTANCE().setRepeatInterval(7);
                        break;
                    case "month":
                        AppSettings.getINSTANCE().setRepeatInterval(30);
                        break;
                    case "year":
                        AppSettings.getINSTANCE().setRepeatInterval(365);
                        break;
                }
                break;
            case "allow_cellular_data":
                AppSettings.getINSTANCE().setAllowCellularData(preferences.getBoolean(key, false));
                break;
            case "requires_charging":
                AppSettings.getINSTANCE().setRequiresCharging(preferences.getBoolean(key, false));
                break;
        }
    }

    @Override
    public boolean onPreferenceStartFragment(PreferenceFragmentCompat caller, Preference pref) {
        Bundle args = pref.getExtras();

        Fragment fragment = getSupportFragmentManager().getFragmentFactory().instantiate(
                getClassLoader(),
                pref.getFragment());
        fragment.setArguments(args);
        fragment.setTargetFragment(caller, 0);

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(
                        R.anim.slide_in,  // enter
                        R.anim.fade_out,  // exit
                        R.anim.fade_in,   // popEnter
                        R.anim.slide_out  // popExit
                )
                .replace(R.id.settings, fragment)
                .addToBackStack(null)
                .commit();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}