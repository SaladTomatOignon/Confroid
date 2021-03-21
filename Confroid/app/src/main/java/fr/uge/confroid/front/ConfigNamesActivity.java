package fr.uge.confroid.front;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fr.uge.confroid.R;
import fr.uge.confroid.front.adapters.ConfigNameListAdapter;
import fr.uge.confroid.front.models.ConfigNameListItem;
import fr.uge.confroid.storage.ConfroidDatabase;

import android.os.Bundle;
import android.view.View;

import java.util.List;
import java.util.stream.Collectors;

public class ConfigNamesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_names);

        getSupportActionBar().setTitle(R.string.title_explore);

        RecyclerView recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        View emptyState = findViewById(R.id.empty_state);
        emptyState.setVisibility(View.GONE);

        ConfroidDatabase.exec(this, dao -> {
            List<ConfigNameListItem> items = dao.findAllNames()
                .stream()
                .map(name -> ConfigNameListItem.create(this, name))
                .collect(Collectors.toList());

            if (items.isEmpty()) {
                emptyState.setVisibility(View.VISIBLE);
            }
            recycler.setAdapter(new ConfigNameListAdapter(items));
        });
    }
}