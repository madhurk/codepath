package madhur.codepath.imagesearcher;

import android.content.SearchRecentSuggestionsProvider;

public class MySuggestionProvider extends SearchRecentSuggestionsProvider {
  
  public final static String AUTHORITY = "madhur.codepath.imagesearcher.MySuggestionProvider";
  public final static int MODE = DATABASE_MODE_QUERIES;

  public MySuggestionProvider() {
      setupSuggestions(AUTHORITY, MODE);
  }

}
