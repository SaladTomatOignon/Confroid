package fr.uge.confroid.front.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fr.uge.confroid.R;
import fr.uge.confroid.configuration.Configuration;
import fr.uge.confroid.configuration.Dictionary;
import fr.uge.confroid.configuration.Value;
import fr.uge.confroid.front.adapters.ConfigValueListAdapter;
import fr.uge.confroid.front.models.ConfigValueListItem;
import fr.uge.confroid.front.models.EditorSession;
import fr.uge.confroidlib.BundleUtils;

public class MapEditorFragment extends EditorFragment {
    private final ConfigValueListAdapter adapter = new ConfigValueListAdapter();
    private Value value;

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
    public void onReady(Value value) {
        this.value = value;

        Map<String, Value> map = value.getMap();
        if (map.isEmpty()) {
            return;
        }

        String className = map.get(BundleUtils.CLASS_KEYWORD).getString();
        // TODO check if className is a subtype of Map instead
        boolean readonly = !className.equals(HashMap.class.getName());

        List<ConfigValueListItem> items = (
                Configuration.filterKeywords(value)
                        .getMap()
                        .entrySet()
                        .stream()
                        .map(entry -> entryToListItem(entry, readonly))
                        .collect(Collectors.toList()
                        )
        );

        adapter.setItems(items);
    }

    private ConfigValueListItem entryToListItem(Map.Entry<String, Value> entry, boolean readonly) {
        ConfigValueListItem item = ConfigValueListItem.create(
            getActivity(),
            entry.getKey(),
            entry.getValue()
        );

        item.setOnEditListener(() -> {
            push(new EditorSession(entry.getKey(), entry.getValue()));
        });

        if (!readonly) {
            item.setOnDeleteListener(() -> {
                Map<String, Value> map = value.getMap();
                map.remove(entry.getKey());

                onReady(value);

                emitChange(new Dictionary(map));
            });

            item.setOnRenameListener(newName -> {
                Map<String, Value> map = value.getMap();
                map.remove(entry.getKey());
                map.put(newName, entry.getValue());

                onReady(value);

                emitChange(new Dictionary(map));
            });
        }

        return item;
    }

}
