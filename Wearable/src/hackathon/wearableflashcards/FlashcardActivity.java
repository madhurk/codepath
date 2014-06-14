package hackathon.wearableflashcards;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class FlashcardActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_flashcard);
  }
  
  public void onStart(View v){
    CardsCreator creator = new CardsCreator();
    List<FlashCard> cards = creator.create();
  }
}
