package fr.uge.confroid.front.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fr.uge.confroid.R;
import fr.uge.confroid.configuration.Value;
import fr.uge.confroid.front.adapters.ConfigValueListAdapter;
import fr.uge.confroid.front.models.ConfigValueListItem;

public class ArrayEditorFragment extends EditorFragment {
    private RecyclerView recycler;
    private ConfigValueListAdapter adapter = new ConfigValueListAdapter();


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
    public void onReady(Value value) {
        Log.v(".....", value.toString());
        Value[] array = value.getArray();
        List<ConfigValueListItem> items = IntStream.range(0, array.length)
            .mapToObj(i -> ConfigValueListItem.create(
                getActivity(),
                Integer.toString(i),
                array[i]
            )).collect(Collectors.toList());
        adapter.setItems(items);
    }
}
