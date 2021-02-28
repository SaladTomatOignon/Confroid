package fr.uge.confroid.front.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import fr.uge.confroid.R;
import fr.uge.confroid.front.holders.ConfigNameItemHolder;
import fr.uge.confroid.front.models.ConfigNameItem;


public class ConfigNamesAdapter extends RecyclerView.Adapter<ConfigNameItemHolder> {
    private List<ConfigNameItem> items = new ArrayList();

    public ConfigNamesAdapter(List<ConfigNameItem> items) {
        this.items = Objects.requireNonNull(items);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public ConfigNameItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConfigNameItemHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_config_list, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ConfigNameItemHolder holder, int position) {
        holder.bind(items.get(position));
    }
}
