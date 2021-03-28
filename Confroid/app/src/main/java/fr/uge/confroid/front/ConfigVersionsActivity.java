package fr.uge.confroid.front;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fr.uge.confroid.R;
import fr.uge.confroid.front.adapters.ConfigVersionListAdapter;
import fr.uge.confroid.front.models.ConfigVersionListItem;
import fr.uge.confroid.providers.ProviderType;
import fr.uge.confroid.settings.AppSettings;
import fr.uge.confroid.storage.ConfroidPackage;
import fr.uge.confroid.storage.ConfroidStorage;
import fr.uge.confroid.web.Client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ConfigVersionsActivity extends AppCompatActivity {
    private static final String TAG = "ConfigVersionsActivity";
    private static final String EXTRA_NAME = "EXTRA_NAME";
    private static final String SOURCE_KEYWORD = "EXTRA_DATA_SOURCE";
    private ConfigVersionListAdapter adapter;
    private List<ConfroidPackage> confroidPackages;
    private ConfroidPackage packageToExport;

    public static void present(Context context, String name, ProviderType source) {
        Intent intent = new Intent(context, ConfigVersionsActivity.class);
        intent.putExtra(EXTRA_NAME, name);
        intent.putExtra(SOURCE_KEYWORD, source.toString());
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_versions);

        Intent intent = getIntent();

        String name = intent.getStringExtra(EXTRA_NAME);
        if (name == null || name.trim().length() == 0) {
            Log.e(TAG, "Missing extra " + EXTRA_NAME);
            finish();
            return;
        }

        String dataSourceName = intent.getStringExtra(SOURCE_KEYWORD);
        if (dataSourceName == null || dataSourceName.trim().length() == 0) {
            Log.e(TAG, "Missing extra " + SOURCE_KEYWORD);
            finish();
            return;
        }
        ProviderType providerType = ProviderType.valueOf(dataSourceName);

        getSupportActionBar().setTitle(name);

        RecyclerView recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        registerForContextMenu(recycler);

        View emptyState = findViewById(R.id.empty_state);
        emptyState.setVisibility(View.GONE);

        confroidPackages = new ArrayList<>();
        providerType.getProvider().getPackagesByName(name, this, versions -> {
                    List<ConfigVersionListItem> items = versions
                            .stream()
                            .sorted((a, b) -> b.getDate().compareTo(a.getDate()))
                            .map(version -> ConfigVersionListItem.create(this, version, providerType))
                            .collect(Collectors.toList());

                    if (items.isEmpty()) {
                        emptyState.setVisibility(View.VISIBLE);
                    }

                    confroidPackages.addAll(versions);
                    adapter = new ConfigVersionListAdapter(items);

                    recycler.setAdapter(adapter);
                },
                error -> Toast.makeText(this, error, Toast.LENGTH_SHORT).show());
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = -1;
        try {
            position = adapter.getPosition();
        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage(), e);
            return super.onContextItemSelected(item);
        }

        switch (item.getItemId()) {
            case R.id.ctx_menu_export_cloud:
                exportConfigToCloud(confroidPackages.get(position));
                break;
            case R.id.ctx_menu_export_new_file:
                exportToNewFile(confroidPackages.get(position));
                break;
            case R.id.ctx_menu_export_existing_file:
                exportToExistingFile(confroidPackages.get(position));
                break;
        }

        return super.onContextItemSelected(item);
    }

    private void exportConfigToCloud(ConfroidPackage pkg) {
        if (!AppSettings.getINSTANCE().isConnected()) {
            Toast.makeText(this, R.string.web_client_not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        Client.getInstance().sendConfig(
                pkg.getConfig(),
                pkg.getName(),
                pkg.getVersion(),
                pkg.getTag(),
                pkg.getDate().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime(),
                null, null);
    }

    private void exportToNewFile(ConfroidPackage pkg) {
        this.packageToExport = pkg;
        ConfroidStorage.createFile(this);
    }

    private void exportToExistingFile(ConfroidPackage pkg) {
        this.packageToExport = pkg;
        ConfroidStorage.performFileSearch(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ConfroidStorage.OPEN_DOCUMENT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Opening an existing document and add configuration into it
            if (!Objects.isNull(data)) {
                try {
                    ConfroidStorage.writeConfig(this.packageToExport, data.getData(), this);
                } catch (IOException e) {
                    Log.e(TAG, "Exportation to existing file failed", e);
                }
                this.packageToExport = null;
            }
        } else if (requestCode == ConfroidStorage.CREATE_DOCUMENT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Create a new file and add the configuration into it
            if (!Objects.isNull(data)) {
                try {
                    ConfroidStorage.writeConfig(this.packageToExport, data.getData(), this);
                } catch (IOException e) {
                    Log.e(TAG, "Exportation to new file failed", e);
                }
                this.packageToExport = null;
            }
        }
    }
}