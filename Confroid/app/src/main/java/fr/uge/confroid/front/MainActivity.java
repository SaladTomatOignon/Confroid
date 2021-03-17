package fr.uge.confroid.front;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.WorkManager;

import android.content.Intent;
import android.os.Bundle;

import java.util.Objects;

import fr.uge.confroid.R;
import fr.uge.confroid.settings.AppSettings;
import fr.uge.confroid.web.Client;
import fr.uge.confroid.works.WorkRequests;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initApplication();

        findViewById(R.id.btn_explore).setOnClickListener(view -> {
           startActivity(new Intent(this, ConfigNamesActivity.class));
        });

        findViewById(R.id.btn_settings).setOnClickListener(view -> {
            startActivity(new Intent(this, SettingsActivity.class));
        });
    }

    /**
     * Initializes the application.
     */
    private void initApplication() {
        initSettings();
        initWebClient();
        initWorkers();
    }

    private void initSettings() {
        AppSettings.loadSettings(getApplicationContext());
    }

    private void initWebClient() {
        if (!Objects.isNull(AppSettings.getINSTANCE().getLogin()) &&
            !Objects.isNull(AppSettings.getINSTANCE().getPassword())) {
            Client.initClient(AppSettings.getINSTANCE().getLogin(),
                    AppSettings.getINSTANCE().getPassword(),
                    getApplicationContext(), null);
        }
    }

    private void initWorkers() {
        WorkManager.getInstance(getApplicationContext()).enqueue(
                WorkRequests.getUploadWorkRequest()
        );
    }
}