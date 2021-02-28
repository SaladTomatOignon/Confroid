package fr.uge.confroid.front;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fr.uge.confroid.R;
import fr.uge.confroid.front.adapters.ConfigListAdapter;
import fr.uge.confroid.front.models.ConfigListItem;
import fr.uge.confroid.storage.ConfroidDatabase;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import java.util.List;
import java.util.stream.Collectors;

public class ConfigListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_list);

        getSupportActionBar().setTitle(R.string.explore);

        RecyclerView recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        View emptyState = findViewById(R.id.empty_state);
        emptyState.setVisibility(View.GONE);

        ConfroidDatabase.exec(this, dao -> {
            List<ConfigListItem> items = dao.findAllNames()
                .stream()
                .map(name -> ConfigListItem.fromConfigName(this, name))
                .collect(Collectors.toList());

            if (items.isEmpty()) {
                emptyState.setVisibility(View.VISIBLE);
            }
            recycler.setAdapter(new ConfigListAdapter(items));
        });
    }
}