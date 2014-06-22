package hackathon.wearableflashcards;

public class FlashCard {
  public String word;
  public String[] options;
  public String answer;
  
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
  
  public String getOptions(){
    StringBuilder sb = new StringBuilder();
    for(int i = 0; i < options.length; ++i){
      sb.append(i).append(".").append(options[i]).append("; ");
    }
    return sb.toString();
  }
}
