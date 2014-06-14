package hackathon.wearableflashcards;

import java.util.ArrayList;
import java.util.List;

public class CardsCreator {
  
  private List<FlashCard> cards;
  
  public CardsCreator(){
    cards = new ArrayList<FlashCard>();
  }
  
  public List<FlashCard> create(){
    
    cards.add(new FlashCard("detest", new String[] {"love", "hate", "open", "close"}, "hate"));
    cards.add(new FlashCard("expensive", new String[] {"pricey", "cheap", "good", "bad"}, "pricey"));
    cards.add(new FlashCard("spendthrift", new String[] {"frugal", "stingy", "extravagant", "picky"}, "extravagant"));
    cards.add(new FlashCard("magnanimous", new String[] {"small", "large", "cheap", "generous"}, "generous"));
    cards.add(new FlashCard("talkative", new String[] {"reticent", "chatty", "silent", "loud"}, "chatty"));
    
    return cards;
  }
}
