package fr.uge.confroid.front.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import fr.uge.confroid.R;
import fr.uge.confroid.front.holders.ConfigVersionItemHolder;
import fr.uge.confroid.front.models.ConfigVersionItem;

public class ConfigVersionsAdapter extends RecyclerView.Adapter<ConfigVersionItemHolder> {
    private List<ConfigVersionItem> items = new ArrayList();

    public ConfigVersionsAdapter(List<ConfigVersionItem> items) {
        this.items = Objects.requireNonNull(items);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public ConfigVersionItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConfigVersionItemHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_config_version, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ConfigVersionItemHolder holder, int position) {
        holder.bind(items.get(position));
    }
}
