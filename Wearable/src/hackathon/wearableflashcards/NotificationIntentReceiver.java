package hackathon.wearableflashcards;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Broadcast receiver to post toast messages in response to notification intents firing.
 */
public class NotificationIntentReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
      String message = intent.getStringExtra(NotificationUtil.EXTRA_MESSAGE);
      String replyMessage = intent.getStringExtra(NotificationUtil.EXTRA_REPLY);
      if (replyMessage != null) {
          message = message + ": \"" + replyMessage + "\"";
      }
      Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
