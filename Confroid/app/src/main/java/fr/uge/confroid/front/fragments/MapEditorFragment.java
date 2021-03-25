package fr.uge.confroid.front.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fr.uge.confroid.R;
import fr.uge.confroid.configuration.MapValue;
import fr.uge.confroid.configuration.Value;
import fr.uge.confroid.front.adapters.ConfigValueListAdapter;
import fr.uge.confroid.front.models.ConfigValueListItem;
import fr.uge.confroid.front.models.EditorPage;

public class MapEditorFragment extends EditorFragment {
    private final ConfigValueListAdapter adapter = new ConfigValueListAdapter();

    private MapValue mapValue;
    private Map<String, Value> entries;

    public static MapEditorFragment newInstance(Value value) {
        if (value.isMap()) {
            return new MapEditorFragment();
        }
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map_editor, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        RecyclerView recycler = view.findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(adapter);
        registerForContextMenu(recycler);
    }

    @Override
    public void onUpdateValue(Value value) {
        entries = value.getMap();
        mapValue = (MapValue) value;

        List<ConfigValueListItem> items = mapValue
                .editableEntries()
                .stream()
                .map(this::entryToListItem)
                .collect(Collectors.toList());

        adapter.setItems(items);
    }

    private ConfigValueListItem entryToListItem(Map.Entry<String, Value> entry) {
        ConfigValueListItem item = ConfigValueListItem.create(
            getContext(),
            entry.getKey(),
            entry.getValue()
        );

        item.setOnEditListener(() -> {
            push(new EditorPage(entry.getKey(), entry.getValue()));
        });

        if (mapValue.isSubClassOfMap()) {
            item.setOnDeleteListener(() -> {
                entries.remove(entry.getKey());
                updateAndRefresh(new MapValue(entries));
            });

            item.setOnRenameListener(newName -> {
                entries.remove(entry.getKey());
                entries.put(newName, entry.getValue());
                updateAndRefresh(new MapValue(entries));
            });
        }

        return item;
    }
}
