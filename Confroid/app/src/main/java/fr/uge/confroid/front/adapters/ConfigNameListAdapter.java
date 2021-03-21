package fr.uge.confroid.front.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import fr.uge.confroid.R;
import fr.uge.confroid.front.holders.ConfigNameHolder;
import fr.uge.confroid.front.models.ConfigName;


public class ConfigNamesAdapter extends RecyclerView.Adapter<ConfigNameHolder> {
    private List<ConfigName> items = new ArrayList();

    public ConfigNamesAdapter(List<ConfigName> items) {
        this.items = Objects.requireNonNull(items);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @Override
    public ConfigNameHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConfigNameHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_config_list, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ConfigNameHolder holder, int position) {
        holder.bind(items.get(position));
    }
}
