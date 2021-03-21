package fr.uge.confroid.front.holders;

import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import fr.uge.confroid.R;
import fr.uge.confroid.front.models.ConfigValueListItem;

/**
 * Representation of a row of {@link fr.uge.confroid.front.adapters.ConfigValueListAdapter}.
 */
public class ConfigValueListItemHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
    private final ImageView imageType;
    private final TextView labelName;
    private final TextView labelType;
    private final TextView labelPreview;

    private ConfigValueListItem item;

    public ConfigValueListItemHolder(@NonNull View itemView) {
        super(itemView);
        imageType = itemView.findViewById(R.id.image_type);
        labelName = itemView.findViewById(R.id.label_name);
        labelType = itemView.findViewById(R.id.label_type);
        labelPreview = itemView.findViewById(R.id.label_preview);

        itemView.setOnCreateContextMenuListener(this);
    }

    public void bind(ConfigValueListItem item) {
        this.item = item;

        imageType.setImageDrawable(item.getIcon());
        labelName.setText(item.getName());
        labelType.setText(item.getType());
        labelPreview.setText(item.getPreview());
        itemView.setOnClickListener(__ -> item.edit());
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (item.canBeRenamed()) {
            MenuItem edit = menu.add(Menu.NONE, 1, 1, R.string.label_rename);
            edit.setOnMenuItemClickListener(__ -> {
                item.rename();
                return true;
            });
        }

        if (item.canBeDeleted()) {
            MenuItem delete = menu.add(Menu.NONE, 2, 2, R.string.label_delete);
            delete.setOnMenuItemClickListener(__ -> {
                item.delete();
                return true;
            });
        }
    }
}
