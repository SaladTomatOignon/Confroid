package fr.uge.confroid.front.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import fr.uge.confroid.R;
import fr.uge.confroid.front.holders.ConfigValueListItemHolder;
import fr.uge.confroid.front.models.ConfigValueListItem;

public class ConfigValueListAdapter extends RecyclerView.Adapter<ConfigValueListItemHolder> {
    private List<ConfigValueListItem> items;

    public ConfigValueListAdapter() {
        this(new ArrayList<>());
    }

    public ConfigValueListAdapter(List<ConfigValueListItem> items) {
        this.items = Objects.requireNonNull(items);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public ConfigValueListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConfigValueListItemHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_config_value, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ConfigValueListItemHolder holder, int position) {
        holder.bind(items.get(position));
    }

    public void setItems(List<ConfigValueListItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }
}
