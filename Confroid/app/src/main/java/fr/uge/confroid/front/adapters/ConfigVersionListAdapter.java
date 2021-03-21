package fr.uge.confroid.front.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import fr.uge.confroid.R;
import fr.uge.confroid.front.holders.ConfigVersionHolder;
import fr.uge.confroid.front.models.ConfigVersionListItem;

public class ConfigVersionsAdapter extends RecyclerView.Adapter<ConfigVersionHolder> {
    private List<ConfigVersionListItem> items;

    public ConfigVersionsAdapter(List<ConfigVersionListItem> items) {
        this.items = Objects.requireNonNull(items);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public ConfigVersionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConfigVersionHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_config_version, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ConfigVersionHolder holder, int position) {
        holder.bind(items.get(position));
    }
}
