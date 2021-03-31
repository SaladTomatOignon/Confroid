package fr.uge.confroid.front.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fr.uge.confroid.R;
import fr.uge.confroid.configuration.ArrayValue;
import fr.uge.confroid.configuration.Value;
import fr.uge.confroid.front.adapters.ConfigValueListAdapter;
import fr.uge.confroid.front.models.ConfigValueListItem;
import fr.uge.confroid.front.models.EditorArgs;

public class ArrayEditorFragment extends EditorFragment {
    private final ConfigValueListAdapter adapter = new ConfigValueListAdapter();
    private ArrayValue arrayValue;
    private List<Value> entries;

    private MenuItem menuAdd;
    private boolean menuEnabled = false;


    public static ArrayEditorFragment newInstance(Value value) {
        if (value.isArray()) {
            return new ArrayEditorFragment();
        }
        return null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_array_editor, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        RecyclerView recycler = view.findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_map_editor, menu);
        menuAdd = menu.findItem(R.id.menu_action_add);
        menuAdd.setEnabled(menuEnabled);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_action_add) {
            entries.add(entries.get(entries.size()-1).deepCopy());
            updateAndRefresh();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onUpdateArgs(EditorArgs args) {
        arrayValue = (ArrayValue) args.getValue();
        entries = arrayValue.editableEntries();

        setMenuEnabled(false);

        List<ConfigValueListItem> items = IntStream.range(0, entries.size())
            .mapToObj(i -> entryToListItem(i, entries.get(i)))
            .collect(Collectors.toList());

        adapter.setItems(items);
    }

    private ConfigValueListItem entryToListItem(int index, Value entry) {
        setMenuEnabled(true);

        ConfigValueListItem item = ConfigValueListItem.create(
            getContext(),
            Integer.toString(index),
            entry
        );

        item.setOnEditListener(() -> {
            pushEditor(Integer.toString(index), entry);
        });

        item.setOnDeleteListener(() -> {
            entries.remove(index);
            updateAndRefresh();
        });

        return item;
    }

    private void updateAndRefresh() {
        entries.addAll(arrayValue.nonEditableEntries());
        updateValue(new ArrayValue(entries.toArray(new Value[0])), true);
    }

    private void setMenuEnabled(boolean enabled) {
        menuEnabled = enabled;
        if (menuAdd != null) {
            menuAdd.setEnabled(enabled);
        }
    }
}


