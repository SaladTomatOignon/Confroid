package fr.uge.confroid.front;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fr.uge.confroid.R;
import fr.uge.confroid.front.adapters.ConfigVersionListAdapter;
import fr.uge.confroid.front.models.ConfigVersionListItem;
import fr.uge.confroid.providers.ProviderType;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;
import java.util.stream.Collectors;

public class ConfigVersionsActivity extends AppCompatActivity {
    private static final String TAG = "ConfigVersionsActivity";
    private static final String EXTRA_NAME = "EXTRA_NAME";
    private static final String SOURCE_KEYWORD = "EXTRA_DATA_SOURCE";

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

        View emptyState = findViewById(R.id.empty_state);
        emptyState.setVisibility(View.GONE);

        providerType.getProvider().getPackagesByName(name, this, versions -> {
                    List<ConfigVersionListItem> items = versions
                            .stream()
                            .sorted((a, b) -> b.getDate().compareTo(a.getDate()))
                            .map(version -> ConfigVersionListItem.create(this, version, providerType))
                            .collect(Collectors.toList());

                    if (items.isEmpty()) {
                        emptyState.setVisibility(View.VISIBLE);
                    }

                    recycler.setAdapter(new ConfigVersionListAdapter(items));
                },
                error -> Toast.makeText(this, error, Toast.LENGTH_SHORT).show());
    }
}