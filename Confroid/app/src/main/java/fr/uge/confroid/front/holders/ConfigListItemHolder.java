package fr.uge.confroid.front.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import fr.uge.confroid.R;
import fr.uge.confroid.front.models.ConfigListItem;

public class ConfigListItemHolder  extends RecyclerView.ViewHolder {
    private ImageView icon;
    private TextView label;

    public ConfigListItemHolder(@NonNull View itemView) {
        super(itemView);
        icon = itemView.findViewById(R.id.icon);
        label = itemView.findViewById(R.id.label);
    }

    public void bind(ConfigListItem item) {
        label.setText(item.getName());
        icon.setImageDrawable(item.getIcon());
        itemView.setOnClickListener(__ -> {
            item.click();
        });
    }
}
