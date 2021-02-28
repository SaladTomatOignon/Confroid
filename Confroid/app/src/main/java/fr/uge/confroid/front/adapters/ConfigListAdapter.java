package fr.uge.confroid.front.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import fr.uge.confroid.R;
import fr.uge.confroid.front.holders.ConfigListItemHolder;
import fr.uge.confroid.front.models.ConfigListItem;

public class ConfigListAdapter extends RecyclerView.Adapter<ConfigListItemHolder> {
    private List<ConfigListItem> items = new ArrayList();

    public ConfigListAdapter(List<ConfigListItem> items) {
        this.items = Objects.requireNonNull(items);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public ConfigListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConfigListItemHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_config_list, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ConfigListItemHolder holder, int position) {
        holder.bind(items.get(position));
    }
}
