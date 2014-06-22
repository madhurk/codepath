package hackathon.wearableflashcards;

import hackathon.wearableflashcards.ActionsPresets.ReplyWithChoicesActionPreset;
import hackathon.wearableflashcards.NotificationPresets.MultiplePageNotificationPreset;
import hackathon.wearableflashcards.PriorityPresets.SimplePriorityPreset;

import java.util.List;

import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.preview.support.v4.app.NotificationManagerCompat;
import android.view.View;

public class FlashcardActivity extends Activity {

  List<FlashCard> cards;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_flashcard);
    CardsCreator creator = new CardsCreator();
    cards = creator.create();
  }
  
  @Override
  protected void onResume() {
      super.onResume();
  }

  public void onGameStart(View v){
    postNotifications(0);
//    postNotifications(1);    
//    postNotifications(2);    
//    postNotifications(3);    
//    postNotifications(4);    

  }
  
  /**
   * Post the sample notification(s) using current options.
   */
  private void postNotifications(int cardNum) {
      sendBroadcast(new Intent()
              .setClass(this, NotificationIntentReceiver.class));

      NotificationPreset preset = new MultiplePageNotificationPreset(cards.get(cardNum));
      
      PriorityPreset priorityPreset = new SimplePriorityPreset(R.string.high_priority, Notification.PRIORITY_HIGH);
      ActionsPreset actionsPreset = new ReplyWithChoicesActionPreset();
      NotificationPreset.BuildOptions options = new NotificationPreset.BuildOptions(
              priorityPreset,
              actionsPreset,
              true,
              false,
              true);
      
      Notification[] notifications = preset.buildNotifications(this, options);

      
      // Post new notifications
//      for (int i = 0; i < notifications.length; i++) {
          Notification thisNote = notifications[0];  
          NotificationManagerCompat.from(this).notify(cardNum, thisNote);
   //   }
  }

}
