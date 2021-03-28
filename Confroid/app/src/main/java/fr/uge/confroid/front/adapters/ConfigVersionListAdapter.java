package fr.uge.confroid.front.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import fr.uge.confroid.R;
import fr.uge.confroid.front.holders.ConfigVersionListItemHolder;
import fr.uge.confroid.front.models.ConfigVersionListItem;

public class ConfigVersionListAdapter extends RecyclerView.Adapter<ConfigVersionListItemHolder> {
    private List<ConfigVersionListItem> items;
    private int position;

    public ConfigVersionListAdapter(List<ConfigVersionListItem> items) {
        this.items = Objects.requireNonNull(items);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public ConfigVersionListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConfigVersionListItemHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_config_version, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ConfigVersionListItemHolder holder, int position) {
        holder.bind(items.get(position));
        holder.itemView.setOnLongClickListener(v -> {
            setPosition(holder.getAdapterPosition());
            return false;
        });
    }

    @Override
    public void onViewRecycled(@NonNull ConfigVersionListItemHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
