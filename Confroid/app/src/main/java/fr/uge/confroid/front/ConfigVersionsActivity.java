package fr.uge.confroid.front;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fr.uge.confroid.R;
import fr.uge.confroid.front.adapters.ConfigVersionsAdapter;
import fr.uge.confroid.front.models.ConfigVersionItem;
import fr.uge.confroid.storage.ConfroidDatabase;
import fr.uge.confroid.storage.ConfroidPackage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigVersionsActivity extends AppCompatActivity {
    private static final String EXTRA_NAME = "EXTRA_NAME";

    public static void present(Context context, String name) {
        Intent intent = new Intent(context, ConfigVersionsActivity.class);
        intent.putExtra(EXTRA_NAME, name);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_versions);

        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }

        String name = intent.getStringExtra(EXTRA_NAME);
        if (name == null || name.trim().length() == 0) {
            finish();
            return;
        }

        getSupportActionBar().setTitle(name);


        RecyclerView recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        View emptyState = findViewById(R.id.empty_state);
        emptyState.setVisibility(View.GONE);

        ConfroidDatabase.exec(this, dao -> {
            List<ConfroidPackage> versions = dao.findAllVersions(name);
            List<ConfigVersionItem> items = versions
                    .stream()
                    .sorted((a, b) -> b.getDate().compareTo(a.getDate()))
                    .map(version -> ConfigVersionItem.create(this, version))
                    .collect(Collectors.toList());

            if (items.isEmpty()) {
                emptyState.setVisibility(View.VISIBLE);
            }

            recycler.setAdapter(new ConfigVersionsAdapter(items));
        });
    }
}