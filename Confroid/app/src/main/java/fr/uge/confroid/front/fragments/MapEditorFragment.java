package fr.uge.confroid.front.fragments;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fr.uge.confroid.R;
import fr.uge.confroid.configuration.MapValue;
import fr.uge.confroid.configuration.Value;
import fr.uge.confroid.front.adapters.ConfigValueListAdapter;
import fr.uge.confroid.front.models.ConfigValueListItem;
import fr.uge.confroid.front.models.EditorArgs;
import fr.uge.confroid.front.models.EditorOpener;

public class MapEditorFragment extends EditorFragment {
    public static class Opener implements EditorOpener {
        @Override
        public boolean canHandle(EditorArgs args) {
            return args.getValue().isMap();
        }

        @Override
        public Fragment createEditor() {
            return new MapEditorFragment();
        }
    }

    private final ConfigValueListAdapter adapter = new ConfigValueListAdapter();
    private MapValue mapValue;
    private Map<String, Value> entries;
    private Value lastValue;
    private MenuItem menuAdd;
    private boolean menuEnabled = false;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_map_editor, menu);
        menuAdd = menu.findItem(R.id.menu_action_add);
        menuAdd.setEnabled(menuEnabled);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_action_add) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(R.string.title_dialog_create_value);

            final EditText input = new EditText(getContext());
            input.setText("");
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            builder.setPositiveButton(R.string.label_create, (dialog, which) -> {
                String newName = input.getText().toString();
                if (!newName.isEmpty()) {
                    if (entries.containsKey(newName)) {
                        Toast.makeText(getContext(), R.string.label_already_exists, Toast.LENGTH_SHORT).show();
                    } else {
                        entries.put(newName, lastValue.deepCopy());
                        updateValue(new MapValue(entries), true);
                    }
                }
            });
            builder.setNegativeButton(R.string.label_cancel, (dialog, which) -> dialog.cancel());
            builder.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onUpdateArgs(EditorArgs args) {
        mapValue = (MapValue) args.getValue();
        entries = mapValue.getMap();

        setMenuEnabled(false);

        List<ConfigValueListItem> items = mapValue
                .editableEntries()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(this::entryToListItem)
                .collect(Collectors.toList());

        adapter.setItems(items);
    }

    private ConfigValueListItem entryToListItem(Map.Entry<String, Value> entry) {
        lastValue = entry.getValue();

        List<Annotation> annotations = mapValue.annotations(entry.getKey());

        ConfigValueListItem item = ConfigValueListItem.create(
            getContext(),
            entry.getKey(),
            entry.getValue(),
            annotations
        );

        item.setOnEditListener(() -> {
            pushEditor(entry.getKey(), entry.getValue(), annotations);
        });

        if (mapValue.isSubClassOfMap()) {
            setMenuEnabled(true);

            item.setOnDeleteListener(() -> {
                entries.remove(entry.getKey());
                updateValue(new MapValue(entries), true);
            });

            item.setOnRenameListener(newName -> {
                if (entries.containsKey(newName)) {
                    Toast.makeText(getContext(), R.string.label_already_exists, Toast.LENGTH_SHORT).show();
                    return;
                }

                entries.remove(entry.getKey());
                entries.put(newName, entry.getValue());
                updateValue(new MapValue(entries), true);
            });
        }

        return item;
    }

    private void setMenuEnabled(boolean enabled) {
        menuEnabled = enabled;
        if (menuAdd != null) {
            menuAdd.setEnabled(enabled);
        }
    }
}
