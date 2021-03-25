package fr.uge.confroid.front.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fr.uge.confroid.R;
import fr.uge.confroid.configuration.ArrayValue;
import fr.uge.confroid.configuration.Value;
import fr.uge.confroid.front.adapters.ConfigValueListAdapter;
import fr.uge.confroid.front.models.ConfigValueListItem;

public class ArrayEditorFragment extends EditorFragment {
    private RecyclerView recycler;
    private ConfigValueListAdapter adapter = new ConfigValueListAdapter();
    private ArrayValue arrayValue;
    private List<Value> entries;

    public static ArrayEditorFragment newInstance(Value value) {
        if (value.isArray()) {
            return new ArrayEditorFragment();
        }
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_array_editor, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        recycler = view.findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(adapter);
    }

    @Override
    public void onUpdateValue(Value value) {
        arrayValue = (ArrayValue) value;
        entries = this.arrayValue.editableEntries();

        List<ConfigValueListItem> items = IntStream.range(0, entries.size())
            .mapToObj(i -> entryToListItem(i, entries.get(i)))
            .collect(Collectors.toList());

        adapter.setItems(items);
    }

    private ConfigValueListItem entryToListItem(int index, Value entry) {
        ConfigValueListItem item = ConfigValueListItem.create(
            getContext(),
            Integer.toString(index),
            entry
        );

        item.setOnDeleteListener(() -> {
            entries.remove(index);
            mergeAndUpdate();
        });

        return item;
    }

    private void mergeAndUpdate() {
        /* for (Value v : arrayValue.getArray()) {
            if (!entries.contains(v)) {
                entries.add(v);
            }
        }
        */

        update(new ArrayValue(entries.toArray(new Value[0])));
    }

}


