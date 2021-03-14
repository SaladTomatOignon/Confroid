package fr.uge.confroid.front;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.WorkManager;

import android.content.Intent;
import android.os.Bundle;

import fr.uge.confroid.R;
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
    }

    /**
     * Initializes the application.
     */
    private void initApplication() {
        initWebClient();
        initWorkers();
    }

    private void initWebClient() {
        // TODO : Rendre parametrable les informations de loggin
        Client.initClient("username", "password", getApplicationContext());
    }

    private void initWorkers() {
        WorkManager.getInstance(getApplicationContext()).enqueue(
                WorkRequests.getUploadWorkRequest()
        );
    }
}