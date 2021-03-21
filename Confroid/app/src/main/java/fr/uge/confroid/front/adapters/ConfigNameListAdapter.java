package fr.uge.confroid.front.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import fr.uge.confroid.R;
import fr.uge.confroid.front.holders.ConfigNameListItemHolder;
import fr.uge.confroid.front.models.ConfigNameListItem;


public class ConfigNameListAdapter extends RecyclerView.Adapter<ConfigNameListItemHolder> {
    private List<ConfigNameListItem> items = new ArrayList();

    public ConfigNameListAdapter(List<ConfigNameListItem> items) {
        this.items = Objects.requireNonNull(items);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public ConfigNameListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConfigNameListItemHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_config_list, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ConfigNameListItemHolder holder, int position) {
        holder.bind(items.get(position));
    }
}
