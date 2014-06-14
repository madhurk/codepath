package hackathon.wearableflashcards;

public class FlashCard {
  private String word;
  private String[] options;
  private String answer;
  
  public FlashCard(String word, String[] options, String answer){
    this.word = word;
    this.options = options;
    this.answer = answer;
  }
  
  public boolean verify(int userChoice){
    if(userChoice < 0 || userChoice > options.length-1)
      return false;
    
    return options[userChoice].equalsIgnoreCase(answer);
  }
}
