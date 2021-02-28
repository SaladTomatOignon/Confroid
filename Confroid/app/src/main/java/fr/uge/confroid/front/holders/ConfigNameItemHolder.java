package fr.uge.confroid.front.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import fr.uge.confroid.R;
import fr.uge.confroid.front.models.ConfigNameItem;

public class ConfigNameItemHolder extends RecyclerView.ViewHolder {
    private final ImageView imageIcon;
    private final TextView labelName;

    public ConfigNameItemHolder(@NonNull View itemView) {
        super(itemView);
        imageIcon = itemView.findViewById(R.id.image_config_icon);
        labelName = itemView.findViewById(R.id.label_config_name);
    }

    public void bind(ConfigNameItem item) {
        labelName.setText(item.getName());
        imageIcon.setImageDrawable(item.getIcon());
        itemView.setOnClickListener(__ -> {
            item.click();
        });
    }
}
