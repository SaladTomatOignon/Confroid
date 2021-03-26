package fr.uge.confroid.front.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import java.util.Objects;

import fr.uge.confroid.R;
import fr.uge.confroid.settings.AppSettings;
import fr.uge.confroid.web.Client;

public class CloudAccessSettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context ctx = getActivity().getApplicationContext();

        boolean connected = AppSettings.getINSTANCE().isConnected();
        ((SwitchPreference)findPreference("server_status")).setChecked(connected);
        ((SwitchPreference)findPreference("server_status")).setSummary(
                connected ?
                        R.string.server_connected :
                        R.string.server_disconnected
        );

        findPreference("test_connexion_btn").setOnPreferenceClickListener(preference -> {
            if (!fieldsValid()) {
                return false;
            }

            getActivity().findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
            Client.initClient(
                    AppSettings.getINSTANCE().getLogin(),
                    AppSettings.getINSTANCE().getPassword(),
                    ctx,
                    (success) -> {
                        if (success) {
                            ((SwitchPreference)findPreference("server_status")).setChecked(true);
                            ((SwitchPreference)findPreference("server_status")).setSummary(R.string.server_connected);
                            Toast.makeText(ctx, R.string.test_connexion_ok, Toast.LENGTH_SHORT).show();
                        } else {
                            ((SwitchPreference)findPreference("server_status")).setChecked(false);
                            ((SwitchPreference)findPreference("server_status")).setSummary(R.string.server_disconnected);
                            Toast.makeText(ctx, R.string.test_connexion_ko, Toast.LENGTH_SHORT).show();
                        }
                        if (!Objects.isNull(getActivity())) {
                            getActivity().findViewById(R.id.progressbar).setVisibility(View.GONE);
                        }
                    });

            return true;
        });
    }

    private boolean fieldsValid() {
        Context ctx = getActivity().getApplicationContext();
        EditTextPreference baseAddressPref = (EditTextPreference)findPreference("base_address");
        EditTextPreference loginPref = (EditTextPreference)findPreference("login");
        EditTextPreference passwordPref = (EditTextPreference)findPreference("password");

        String txt = baseAddressPref.getText();
        if (Objects.isNull(baseAddressPref.getText())) {
            Toast.makeText(ctx, R.string.base_address_empty_error, Toast.LENGTH_SHORT).show();
            return false;
        }

        txt = loginPref.getText();
        if (Objects.isNull(loginPref.getText())) {
            Toast.makeText(ctx, R.string.login_empty_error, Toast.LENGTH_SHORT).show();
            return false;
        }

        txt = passwordPref.getText();
        if (Objects.isNull(passwordPref.getText())) {
            Toast.makeText(ctx, R.string.password_empty_error, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.cloud_access_preferences, rootKey);
    }
}