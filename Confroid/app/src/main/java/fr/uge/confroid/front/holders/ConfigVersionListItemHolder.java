package fr.uge.confroid.front.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import fr.uge.confroid.R;
import fr.uge.confroid.front.models.ConfigVersionListItem;

public class ConfigVersionListItemHolder extends RecyclerView.ViewHolder {
    private final ImageView imageIcon;
    private final TextView labelDate;
    private final TextView labelVersion;

    public ConfigVersionListItemHolder(@NonNull View itemView) {
        super(itemView);
        imageIcon = itemView.findViewById(R.id.image_config_icon);
        labelDate = itemView.findViewById(R.id.label_config_date);
        labelVersion = itemView.findViewById(R.id.label_config_version);
    }

    public void bind(ConfigVersionListItem item) {
        labelDate.setText(item.getDate());
        labelVersion.setText(item.getVersion());
        imageIcon.setImageDrawable(item.getIcon());
        itemView.setOnClickListener(__ -> item.click());
    }
}
