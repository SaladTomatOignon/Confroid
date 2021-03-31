package fr.uge.confroidlib.annotations;

import android.os.Bundle;
import android.view.View;

import java.util.function.Consumer;

public interface CustomizableView {

    /**
     *
     * @return A custom view
     */
    View getView();

    /**
     * Updates the view according to the given value.
     *
     * @param value The bundle containing the value information
     */
    void update(Bundle value);

    /**
     * Function called when a change has been done in the view.
     *
     * @param value The value which has changed
     */
    void onChange(Consumer<Bundle> value);
}
