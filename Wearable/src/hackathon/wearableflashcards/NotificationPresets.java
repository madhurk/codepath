package hackathon.wearableflashcards;

import android.app.Notification;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.preview.support.wearable.notifications.WearableNotifications;
import android.support.v4.app.NotificationCompat;

/**
 * Collection of notification builder presets.
 */
public class NotificationPresets {

  private static WearableNotifications.Builder buildBasicNotification(Context context,
            NotificationPreset.BuildOptions options) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(context.getString(R.string.example_content_title))
                .setContentText(context.getString(R.string.example_content_text))
                .setSmallIcon(R.mipmap.ic_app_notification_studio)
                .setDeleteIntent(NotificationUtil.getExamplePendingIntent(
                        context, R.string.example_notification_deleted));
        WearableNotifications.Builder wearableBuilder = new WearableNotifications.Builder(builder);
        //wearableBuilder
        
        options.actionsPreset.apply(context, wearableBuilder);
        options.priorityPreset.apply(wearableBuilder);
        if (options.includeLargeIcon) {
            builder.setLargeIcon(BitmapFactory.decodeResource(
                    context.getResources(), R.drawable.example_large_icon));
        }
        if (options.isLocalOnly) {
            wearableBuilder.setLocalOnly(true);
        }
        if (options.hasContentIntent) {
            builder.setContentIntent(NotificationUtil.getExamplePendingIntent(context,
                    R.string.content_intent_clicked));
        }
                
        return wearableBuilder;
    }



    public static class MultiplePageNotificationPreset extends NotificationPreset {
        FlashCard card;
        public MultiplePageNotificationPreset(FlashCard card) {
            super(R.string.multiple_page_example);
            this.card = card;
        }

        @Override
        public Notification[] buildNotifications(Context context, BuildOptions options) {
            
            Notification secondPage = new NotificationCompat.Builder(context)
                    .setContentTitle(card.word)
                    .setContentText(card.getOptions())
                    .build();
            
            Notification notification = buildBasicNotification(context, options)
                    .addPage(secondPage)//.setGroup("abcd")
                    .build();
                        
            return new Notification[] { notification };
        }
    }
}
