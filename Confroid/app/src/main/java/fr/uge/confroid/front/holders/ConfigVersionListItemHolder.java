package fr.uge.confroid.front.holders;

import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import fr.uge.confroid.R;
import fr.uge.confroid.front.models.ConfigVersionListItem;
import fr.uge.confroid.settings.AppSettings;

public class ConfigVersionListItemHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
    private final ImageView imageIcon;
    private final TextView labelDate;
    private final TextView labelVersion;

    public ConfigVersionListItemHolder(@NonNull View itemView) {
        super(itemView);
        imageIcon = itemView.findViewById(R.id.image_config_icon);
        labelDate = itemView.findViewById(R.id.label_config_date);
        labelVersion = itemView.findViewById(R.id.label_config_version);

        itemView.setOnCreateContextMenuListener(this);
    }

    public void bind(ConfigVersionListItem item) {
        labelDate.setText(item.getDate());
        labelVersion.setText(item.getVersion());
        imageIcon.setImageDrawable(item.getIcon());
        itemView.setOnClickListener(__ -> item.click());
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle(R.string.export_config_title);
        menu.add(Menu.NONE, R.id.ctx_menu_export_cloud, Menu.NONE, R.string.export_config_cloud)
                .setEnabled(AppSettings.getINSTANCE().isConnected());
        menu.add(Menu.NONE, R.id.ctx_menu_export_new_file, Menu.NONE, R.string.export_config_new_file);
        menu.add(Menu.NONE, R.id.ctx_menu_export_existing_file, Menu.NONE, R.string.export_config_existing_file);
    }
}
