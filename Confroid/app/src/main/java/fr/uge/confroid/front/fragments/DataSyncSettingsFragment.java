package fr.uge.confroid.front.fragments;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import fr.uge.confroid.R;

public class DataSyncSettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.data_sync_preferences, rootKey);
    }
}