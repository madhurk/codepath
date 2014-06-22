package hackathon.wearableflashcards;

import android.preview.support.wearable.notifications.WearableNotifications;

/**
 * Base class for notification priority presets.
 */
public abstract class PriorityPreset extends NamedPreset {
    public PriorityPreset(int nameResId) {
        super(nameResId);
    }

    /** Apply the priority to a notification builder */
    public abstract void apply(WearableNotifications.Builder builder);
}
