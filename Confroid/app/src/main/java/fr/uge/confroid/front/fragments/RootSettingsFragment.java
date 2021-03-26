package fr.uge.confroid.front.fragments;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import fr.uge.confroid.R;

public class RootSettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}